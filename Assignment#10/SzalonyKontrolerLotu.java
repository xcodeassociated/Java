import java.util.Random;


public class SzalonyKontrolerLotu implements FlightControlInterface {

	Random rnd = new Random();
	
	@Override
	public void planeTarget(String flightNumber, Position target) {
		// TODO Auto-generated method stub

	}

	@Override
	public void planePosition(String flightNumber, Position currentPosition,
			float currentDirection) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getFlightCourse(String flightNumber) {
		float dir = rnd.nextFloat()*360;
		System.out.println( "Wylosowany kierunek lotu : " + dir );
		return dir;
	}

	@Override
	public void planeLanded(String flightNumber) {
		// TODO Auto-generated method stub

	}

}
