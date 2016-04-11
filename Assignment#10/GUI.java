import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GUI implements FlightControlInterface {

	private Map<String, Position> Positions = new HashMap<String, Position>();
	private Map<String, Float> directions = new HashMap<String, Float>();
	private Map<String, Position> destinations = new HashMap<String, Position>();
	private Map<String, List<Position>> history = new HashMap<String, List<Position>>();
	private Map<String, Color> colors = new HashMap<String, Color>();
	private JFrame frame;
	private Position crash;
	private Radar radar;
	private static AtomicLong score = new AtomicLong();
	private static AtomicBoolean endFlag = new AtomicBoolean(false);
	private static AtomicBoolean noOneOnGUI = new AtomicBoolean(false);

	private static float positionOutOfRadarRange = (float) ((CommonConsts.RADAR_RANGE
			* Math.sin(Math.PI / 4) + CommonConsts.AIRCONTROLL_AREA_LENGTH_2) * 0.5f);
	private static Position upLeft = new Position(-positionOutOfRadarRange,
			positionOutOfRadarRange);
	private static Position upRight = new Position(positionOutOfRadarRange,
			positionOutOfRadarRange);
	private static Position downLeft = new Position(-positionOutOfRadarRange,
			-positionOutOfRadarRange);
	private static Position downRight = new Position(positionOutOfRadarRange,
			-positionOutOfRadarRange);
	private static Position airPort = new Position(0, 0);
	private static Position up = new Position(0, positionOutOfRadarRange * 1.2f);
	private static Position down = new Position(0,
			-positionOutOfRadarRange * 1.2f);
	private static Position left = new Position(
			-positionOutOfRadarRange * 1.2f, 0);
	private static Position right = new Position(
			+positionOutOfRadarRange * 1.2f, 0);

	private static class Colors {
		private static Color cs[] = { Color.RED, Color.GRAY, Color.GREEN,
				Color.ORANGE, Color.BLUE, Color.CYAN, Color.WHITE,
				Color.MAGENTA, Color.PINK, Color.YELLOW };

		private static int counter = 0;

		public static Color getColor() {
			return cs[counter++ % cs.length];
		}
	}

	public void setCrash(Position pos) {
		crash = pos;
		// frame.repaint();
	}

	private class JPanelWithPlains extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -815115874559495677L;
		private Graphics2D g2d;
		private Position ps;
		private List<Position> pos;
		private AffineTransform at;
		private Stroke stroke = new BasicStroke(0.2f);
		private Shape radarRange = new Ellipse2D.Float(
				-CommonConsts.RADAR_RANGE, -CommonConsts.RADAR_RANGE,
				2 * CommonConsts.RADAR_RANGE, 2 * CommonConsts.RADAR_RANGE);

		private int textPosition;

		private void drawPlane(Position ps) {
			g2d.draw(new Rectangle2D.Float(ps.x - CommonConsts.PLANE_SIZE_2,
					-ps.y - CommonConsts.PLANE_SIZE_2, CommonConsts.PLANE_SIZE,
					CommonConsts.PLANE_SIZE));
		}

		private void drawDestination(Position ps) {
			g2d.draw(new Ellipse2D.Float(
					ps.x - CommonConsts.DESTINATION_RADIUS, -ps.y
							- CommonConsts.DESTINATION_RADIUS,
					CommonConsts.DESTINATION_RADIUS * 2,
					CommonConsts.DESTINATION_RADIUS * 2));
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g2d = (Graphics2D) g;

			Font f = new Font("Dialog", Font.PLAIN, 30);
			g.setFont(f);
			textPosition = f.getSize();
			synchronized (GUI.this) {
				for (String fn : Positions.keySet()) {
					g2d.setColor(colors.get(fn));
					g2d.drawString(fn, 10, textPosition);
					textPosition += f.getSize();
				}				
			}

			g2d.setColor(Color.BLACK);

			at = new AffineTransform();

			at.scale((float) getWidth() / CommonConsts.AIRCONTROLL_AREA_LENGTH,
					(float) getHeight() / CommonConsts.AIRCONTROLL_AREA_LENGTH);
			at.translate(CommonConsts.AIRCONTROLL_AREA_LENGTH / 2f,
					CommonConsts.AIRCONTROLL_AREA_LENGTH / 2f);

			g2d.setTransform(at);

			g2d.setStroke(stroke);
			g2d.draw(radarRange);

			if (radar != null) {
				g2d.draw(new Arc2D.Float(-CommonConsts.RADAR_RANGE,
						-CommonConsts.RADAR_RANGE,
						2 * CommonConsts.RADAR_RANGE,
						2 * CommonConsts.RADAR_RANGE, radar
								.getAntennaCurrentAngle(),
						CommonConsts.RADAR_ANTENNA_ROTATION_STEP, Arc2D.PIE));
			}

			synchronized (GUI.this) {

				if (crash != null) {
					g2d.setColor(Color.RED);
					g2d.draw(new Ellipse2D.Float(crash.x - 1, -crash.y - 1, 2,
							2));
				}

				for (String id : Positions.keySet()) {

					pos = history.get(id);

					g2d.setColor(colors.get(id));

					if ( pos.size() < 50 ) {
						for (Position ps : pos) {
							drawPlane(ps);
						}						
					} else {
						for ( int i = 0; i < pos.size(); i+= pos.size()/50 )
							drawPlane(pos.get( i ));
					}

					ps = Positions.get(id);
					drawPlane(ps);

					ps = destinations.get(id);
					if (ps != null) {
						drawDestination(ps);
					}
				}
			}
			g2d.dispose();
		}
	}

	static void incrementScore(String passwd, int inc) {
		if (passwd.equals("toSieZmieni")) {
			score.addAndGet(inc);
		}
	}

	static void endGame() {
		System.out.println( "endFlaf.set(true)!");
		endFlag.set(true);
	}

	public GUI() {
		frame = new JFrame("Lotnisko");
		frame.setLayout(new BorderLayout());
		JPanel jp = new JPanelWithPlains();
		frame.getContentPane().add(jp, BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(800, 800));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		Timer tm = new Timer(75, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.repaint();
			}
		});
		tm.setRepeats(true);
		tm.setInitialDelay(100);
		tm.start();
	}

	public void setRadar(Radar r) {
		radar = r;
	}

	synchronized private boolean isFlightKnown(String flightNumber) {
		return Positions.containsKey(flightNumber);
	}

	@Override
	synchronized public float getFlightCourse(String flightNumber) {
		throw new RuntimeException();
	}

	@Override
	synchronized public void planeLanded(String flightNumber) {
		System.out.println("LANDED");
		if (isFlightKnown(flightNumber)) {
			Positions.remove(flightNumber);
			directions.remove(flightNumber);
			if (Positions.isEmpty()) {
				noOneOnGUI.set(true);
			}
			// frame.repaint();
		}
	}

	@Override
	synchronized public void planePosition(String flightNumber,
			Position currentPosition, float currentDirection) {

		if (!isFlightKnown(flightNumber)) {
			history.put(flightNumber, new ArrayList<Position>());
			colors.put(flightNumber, Colors.getColor());
		}

		Positions.put(flightNumber, currentPosition);
		noOneOnGUI.set(false);
		directions.put(flightNumber, currentDirection);
		history.get(flightNumber).add(currentPosition);

		// frame.repaint();
	}

	@Override
	synchronized public void planeTarget(String flightNumber, Position target) {
		destinations.put(flightNumber, target);
	}

	synchronized public void removePlane(String flightNumber) {
		if (isFlightKnown(flightNumber))
			Positions.remove(flightNumber);
		if (Positions.isEmpty()) {
			noOneOnGUI.set(true);
		}
	}

	private static class RefHolder {
		AirportCommunicationBroker acb;
		Radar rd;
		Arbiter ar;
		GUI g;
	}

	private static RefHolder startGame(FlightControlInterface fci) {
		RefHolder rh = new RefHolder();

		rh.g = new GUI();
		rh.acb = new AirportCommunicationBroker();
		rh.acb.addAirport(rh.g);
		rh.acb.addAirport(new SimpleAirport());
		rh.acb.addAirport(fci);
		rh.rd = new Radar(rh.acb);
		rh.g.setRadar(rh.rd);
		rh.ar = new Arbiter();
		rh.ar.setGUI(rh.g);

		return rh;
	}

	private static boolean testPlanes(List<Plane> planes, boolean setEndFlag) {
		boolean result = true;

		for (Plane p : planes) {
			result &= p.testOK(setEndFlag);
		}

		return result;
	}

	private static void sleep(long msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void waitNotLongerThan(long msec) {
		long maxTime = System.currentTimeMillis() + msec;
		noOneOnGUI.set(false); // tak na wszelki wypadek...
		do {
			sleep(250);
			System.out.println(">>>> still waitting");
			if (System.currentTimeMillis() > maxTime) {
				System.out.println("waitNotLongerThan - timeout");
				return;
			}
			if (noOneOnGUI.get()) {
				System.out
						.println("waitNotLongerThan - noOneOnGUI.get() == true");
				return;
			}
		} while (!endFlag.get());
		System.out.println("waitNotLongerThan - endFlag.get == true");
	}

	private static boolean eliminations(RefHolder rh) {
		GUI g = rh.g;
		AirportCommunicationBroker acb = rh.acb;
		Radar rd = rh.rd;
		Arbiter ar = rh.ar;
		List<Plane> planes = new ArrayList<>();

		float positionOutOfRadarRange = (float) ((CommonConsts.RADAR_RANGE
				* Math.sin(Math.PI / 4) + CommonConsts.AIRCONTROLL_AREA_LENGTH_2) * 0.5f);
		Position upLeft = new Position(positionOutOfRadarRange,
				-positionOutOfRadarRange);
		Position upRight = new Position(positionOutOfRadarRange,
				positionOutOfRadarRange);
		Position downLeft = new Position(-positionOutOfRadarRange,
				-positionOutOfRadarRange);
		Position downRight = new Position(-positionOutOfRadarRange,
				positionOutOfRadarRange);
		Position airPort = new Position(0, 0);

		Plane plane1 = new Plane("EFast1", 0.875f, 0.6f, downRight, airPort,
				360 - 45, acb, rd, ar);
		Plane plane2 = new Plane("EFast2", 0.875f, 0.6f, upLeft, airPort,
				180 - 45, acb, rd, ar);
		Plane plane5 = new Plane("EFast5", 0.875f, 0.6f, downLeft, airPort, 50,
				acb, rd, ar);
		plane1.setGUI(g);
		plane2.setGUI(g);
		plane5.setGUI(g);

		sleep(3000);

		Plane plane3 = new Plane("ESlow1", 0.54f, 0.15f, upRight, downLeft,
				180 + 45, acb, rd, ar);
		Plane plane4 = new Plane("ESlow2", 0.54f, 0.15f, downLeft, upRight, 45,
				acb, rd, ar);
		plane3.setGUI(g);
		plane4.setGUI(g);

		planes.add(plane1);
		planes.add(plane2);
		planes.add(plane3);
		planes.add(plane4);
		planes.add(plane5);

		// addPlanes07(rh, planes); // do szybkich testow zmeczeniowych

		long t0 = System.currentTimeMillis();
		waitNotLongerThan(250000);
		System.out.println("Eliminacje zakonczone po "
				+ (System.currentTimeMillis() - t0) + " msec");

		return testPlanes(planes, true);
	}

	private static void addPlane(Plane p, GUI g, List<Plane> planes) {
		noOneOnGUI.set(false);
		p.setGUI(g);
		planes.add(p);
	}

	private static void addPlanes04(RefHolder rh, List<Plane> planes) {
		GUI g = rh.g;
		AirportCommunicationBroker acb = rh.acb;
		Radar rd = rh.rd;
		Arbiter ar = rh.ar;
		addPlane(new Plane("F10", 0.875f, 0.6f, up, airPort, 272, acb, rd, ar),
				g, planes);
		addPlane(
				new Plane("F11", 0.875f, 0.6f, down, airPort, 89, acb, rd, ar),
				g, planes);
		sleep(3000);
		addPlane(new Plane("F12", 0.875f, 0.6f, up, airPort, 270, acb, rd, ar),
				g, planes);
		addPlane(
				new Plane("F13", 0.875f, 0.6f, down, airPort, 90, acb, rd, ar),
				g, planes);
		addPlane(new Plane("F14", 0.875f, 0.6f, left, airPort, 0, acb, rd, ar),
				g, planes);
		addPlane(new Plane("F15", 0.875f, 0.6f, right, airPort, 180, acb, rd,
				ar), g, planes);
	}

	private static long lastTime( Map<Position,Long> map, Position key ) {
		if ( !  map.containsKey( key ) ) return 0;
		return map.get( key );
	}
	
	private static void waitAndUpdateMap( Map<Position,Long> lastTimeMap, Position key, long period ) {
		long time = System.currentTimeMillis();
		
		long delta = time - lastTime( lastTimeMap, key );
		
		if ( delta < period ) {
			delta = period - delta;
			for ( int i = 0; i < delta / 100; i++ ) {
				sleep( 100 );
				if ( endFlag.get() ) return;
			}
		}
		lastTimeMap.put( key, System.currentTimeMillis() );
	}
	
	private static void addPlanes00( RefHolder rh, List<Plane> planes ) {
		GUI g = rh.g;
		AirportCommunicationBroker acb = rh.acb;
		Radar rd = rh.rd;
		Arbiter ar = rh.ar;
		Plane plane4 = new Plane("Slow2", 0.54f, 0.15f, downLeft, airPort, 45,
				acb, rd, ar);
		plane4.setGUI(g);

		sleep(2000);

		Plane plane3 = new Plane("Slow1", 0.54f, 0.15f, upRight, airPort,
				180 + 45, acb, rd, ar);
		plane3.setGUI(g);

		sleep(9000);
		Plane plane1 = new Plane("Fast1", 0.875f, 0.6f, downRight, airPort,
				180 - 45, acb, rd, ar);
		Plane plane2 = new Plane("Fast2", 0.875f, 0.6f, upLeft, airPort,
				360 - 45, acb, rd, ar);
		plane1.setGUI(g);
		plane2.setGUI(g);

		sleep(15000);
		Plane plane5 = new Plane("Fast5", 0.875f, 0.6f, downLeft, airPort, 46,
				acb, rd, ar);
		plane5.setGUI(g);

		planes.add(plane1);
		planes.add(plane2);
		planes.add(plane3);
		planes.add(plane4);
		planes.add(plane5);		
	}
	
	private static void addPlanes01( RefHolder rh, List<Plane> planes ) {
		GUI g = rh.g;
		AirportCommunicationBroker acb = rh.acb;
		Radar rd = rh.rd;
		Arbiter ar = rh.ar;
		
		Plane plane6 = new Plane("Fast6", 0.875f, 0.6f, downLeft, upRight,
				45, acb, rd, ar);
		Plane plane7 = new Plane("Fast7", 0.875f, 0.6f, upRight, downLeft,
				180 + 45, acb, rd, ar);
		plane6.setGUI(g);
		plane7.setGUI(g);

		planes.add(plane6);
		planes.add(plane7);

		sleep(8000);
		Plane plane8 = new Plane("Fast8", 0.875f, 0.6f, upLeft, downRight,
				360 - 45, acb, rd, ar);
		Plane plane9 = new Plane("Fast9", 0.875f, 0.6f, downRight, upLeft,
				90 + 45, acb, rd, ar);
		plane8.setGUI(g);
		plane9.setGUI(g);

		planes.add(plane8);
		planes.add(plane9);
	}
	
	private static boolean game(RefHolder rh) {
		List<Plane> planes = new ArrayList<>();

		addPlanes00(rh, planes);
		waitNotLongerThan(120000);

		if (!endFlag.get()) {
			addPlanes01(rh, planes);
			waitNotLongerThan(120000);
		}
		if (!endFlag.get()) {
			addPlanes04(rh, planes);
			waitNotLongerThan(180000);
		}
// tu sa jeszcze kolejne testy, ostatni z nich losuje starty samolotow
		boolean result = testPlanes(planes, true);
		if (result)
			score.addAndGet(25000); // extra bonus

		return result;
	}

	private static void showVerdict(String message, boolean result,
			boolean showScore) {
		System.out.println("-----------------------------------------");
		System.out.println(">>>>>>>>>  " + message);
		System.out.println("Zakonczona " + (result ? "SUKCESEM" : "PORAZKA"));
		if (showScore)
			System.out.println("SCORE      " + score.get());
		System.out.println("-----------------------------------------");
	}

	public static void main(String[] args) {
		RefHolder rh = null;
		boolean result = true;
		rh = startGame(new FlightControl());

		
		if ( args.length == 0 ) {
			try {
				result = false;
				result = eliminations(rh);
			} catch (Exception e) {
				e.getStackTrace();
				System.out
						.println("Blad: na etapie eliminacji pojawil sie wyjatek !!!!!");
				System.exit(1);
			}

			showVerdict("Misja eliminacje do turnieju", result, false);
			if (!result) {
				System.exit(1);
			}			
		}

		try {
			score.set(0);
			result = game(rh);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Blad: pojawil sie wyjatek !!!!!");
		}

		showVerdict("Misja gra", result, true);

		System.exit(1);

	}

}
