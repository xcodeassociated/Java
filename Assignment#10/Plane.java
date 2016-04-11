import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Idea synchronizacji - zmiany kierunku synchronizowane sa na obiekcie Plane, zmiany
 * polozenia samolotu synchronizowane sa na obiekcie Arbitra - dzieki temu
 * uzytkownik synchronicznych metod arbitra chwilowo ustala stan polozen
 * wszystkich samolotow.
 */

public class Plane implements TransponderInterface {
	private float speed;
	private Position currentPosition;
	private Position destinationPosition;
	private float currentDirection;
	private float newDirection;
	private float turnDirection;
	private float maxTurnAngle;
	private AtomicBoolean continuationFlag = new AtomicBoolean(true);
	private AtomicBoolean planeReachedDestination = new AtomicBoolean(false);
	private AtomicBoolean crash = new AtomicBoolean(false);
	private boolean firstCommunication = true;
	private FlightControlInterface communicationInterface;
	private String flightNumber;
	private Radar radar;
	private Arbiter arbiter;
	private GUI gui;
	private static int BONUS = 50000;

	public Plane(String flightNumber, float speed, float maxTurnAngle,
			Position initialPosition, Position destinationPositions,
			float initialDirection, FlightControlInterface communicationInterface,
			Radar radar, Arbiter arbiter) {
		this.speed = speed / 1000f; // predkosc przeliczona na msec
		this.maxTurnAngle = maxTurnAngle * CommonConsts.FROM_DEGREE_TO_RADIAN; // przeliczamy
																				// na
																				// miare
																				// lukowa
		this.currentPosition = new Position(initialPosition.x,
				initialPosition.y);
		this.currentDirection = initialDirection
				* CommonConsts.FROM_DEGREE_TO_RADIAN; // przeliczamy na miare
														// lukowa
		this.flightNumber = flightNumber;
		this.communicationInterface = communicationInterface;
		this.destinationPosition = destinationPositions;
		this.radar = radar;
		this.arbiter = arbiter;

		arbiter.addPlane(this);
		radar.addObject(this);
		communicationInterface.planeTarget(flightNumber, destinationPositions);
		startThreads();
	}
	
	public boolean testOK( boolean setEndFlag ) {
		
		if ( setEndFlag ) end();
		
		if ( crash.get() ) return false;
		if ( planeReachedDestination.get() == false ) return false;
		return true;
	}

	public void crash() {
		crash.set(true);
		removeFromGUI();
	}

	public void setGUI( GUI gui ) {
		this.gui = gui;
	}
	
	@Override
	public String getFlightNumber() {
		return flightNumber;
	}

	@Override
	public Position getPosition() {
		synchronized (arbiter) {
			return new Position(currentPosition.x, currentPosition.y);
		}
	}

	@Override
	synchronized public float getDirection() {
		return currentDirection;
	}

	private void end() {
		continuationFlag.set(false);
		radar.removeObject(this);
		arbiter.removePlane(this);
		removeFromGUI();		
	}
	
	synchronized private void checkDestination() {
		float distance, dx, dy;
		dx = destinationPosition.x - currentPosition.x;
		dy = destinationPosition.y - currentPosition.y;
		distance = (float) Math.sqrt(dx * dx + dy * dy);

		// sprawdzamy czy dotarlismy do polozenia docelowego
		if (distance < CommonConsts.DESTINATION_RADIUS) {
			if (Math.sqrt(currentPosition.x * currentPosition.x
					+ currentPosition.y * currentPosition.y) < CommonConsts.DESTINATION_RADIUS) {
				if ( arbiter.landingTestOK() )
					communicationInterface.planeLanded(flightNumber);
			}
			planeReachedDestination.set(true);
			GUI.incrementScore("toSieZmieni", BONUS ); // bo
			end();
		}
		
		if ( arbiter.planeOutOfSystemRange( currentPosition ) ) {
			System.err.println( "Samolot " + flightNumber + " wyszedl poza zasieg systemu i jego misja nie zostala ukonczona poprawnie");
			end();
			GUI.endGame();
		}
	}

	private void removeFromGUI() {
		// lot usuwa sie z gui
		if ( gui != null ) {
			gui.removePlane( flightNumber );	
			System.out.println( "Lot " + flightNumber + " usuniety z GUI");
		}
	}
	
	private class MoveThread extends TimerTask {
		float ds;
		long newT;
		long lastT = System.currentTimeMillis();

		private void crash() {
			System.err.println("crash() Zderzenie " + Plane.this.flightNumber
					+ " zakonczyl lot");
			System.err.println( "crash() radar.remove");
			radar.removeObject(Plane.this);
			System.err.println( "crash() arbiter.remove");
			arbiter.removePlane(Plane.this); 
			System.err.println( "crash() continuationFlag.set(false)");
			continuationFlag.set(false);
			System.err.println( "crash() removeFromGUI");
			removeFromGUI();
			System.err.println( "crash() GUI.endGame");
			GUI.endGame();
		}

		@Override
		public void run() {

			if (crash.get()) {
				crash();
			}
			
			if (!continuationFlag.get()) {
				cancel();
				return;
			}

			GUI.incrementScore("toSieZmieni", 1 );
			
			// tu tylko zmiana kierunku
			synchronized (this) {
				if (currentDirection != newDirection) {
					if (Math.abs(currentDirection - newDirection) < maxTurnAngle) {
						currentDirection = newDirection;
					} else {
						currentDirection += turnDirection * maxTurnAngle;
						currentDirection = CommonConsts.Helper.angleToRangeFrom0To2PI( currentDirection );
					}
				}
			}

			// tu tylko zmiana polozenia
			synchronized (arbiter) {
				newT = System.currentTimeMillis(); // czas pobierany jest dopiero po uzyskaniu blokady
				ds = speed * (newT - lastT);
				currentPosition.x += (float) (ds * Math.cos(currentDirection));
				currentPosition.y += (float) (ds * Math.sin(currentDirection));

				// test zderzenia - samolot znika z radaru...
				arbiter.collision(Plane.this); // koniec pracy watku
			}
			lastT = newT;			

			// jesli nie bylo wypadku, to sprawdzamy czy samolot nie dotarl do konca trasy
			if ( ! crash.get() ) checkDestination();
		}
	}

	private class CommunicationThread extends TimerTask {
		
		private Position pos;
		
		@Override
		public void run() {
			if (!continuationFlag.get()) {
				cancel();
				return;
			}

			pos = getPosition();
			
			// poza kontrola radaru
			if ( arbiter.planeOutOfRadarRange( pos ) ) {
				if ( gui != null ) gui.planePosition(flightNumber, pos, currentDirection);
			} else { // nadal w strefie kontroli
				
				if ( firstCommunication ) {
					communicationInterface.planePosition(flightNumber, pos, currentDirection);
					firstCommunication = false;
				}
				
				float newDir = communicationInterface.getFlightCourse(flightNumber);

				synchronized (this) {
					newDirection = CommonConsts.Helper
							.angleToRangeFrom0To360Degree(newDir
									* CommonConsts.FROM_DEGREE_TO_RADIAN);
					turnDirection = CommonConsts.Helper.turnDirection(
							currentDirection, newDirection);
				}	
			}
						
		}
	}

	private void startThreads() {
		Timer plainTask1 = new Timer(true);
		TimerTask tt1 = new MoveThread();
		plainTask1.scheduleAtFixedRate(tt1, CommonConsts.INTEGRATION_PERIOD,
				CommonConsts.INTEGRATION_PERIOD);
		Timer plainTask2 = new Timer(true);
		TimerTask tt2 = new CommunicationThread();
		plainTask2.scheduleAtFixedRate(tt2, CommonConsts.COMMUNICATION_PERIOD,
				CommonConsts.COMMUNICATION_PERIOD);
	}

}
