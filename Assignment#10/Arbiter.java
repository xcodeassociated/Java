import java.util.ArrayList;
import java.util.List;

public class Arbiter {
	private List<Plane> planes = new ArrayList<Plane>();
	private GUI gui;

	public void setGUI(GUI g) {
		gui = g;
	}

	synchronized public void addPlane(Plane pl) {
		planes.add(pl);
	}

	synchronized public void removePlane(Plane pl) {
		planes.remove(pl);
	}

	private boolean testCollision(Position p1, Position p2) {
		float dx = p1.x - p2.x;
		float dy = p1.y - p2.y;

		if (dx * dx + dy * dy > CommonConsts.CRASH_RADIUS_SQ) {
			return false;
		}

		return true;
	}

	private float getDistanceSQ(Position pos) {
		return pos.x * pos.x + pos.y * pos.y;
	}

	public boolean planeOutOfRadarRange(Position pos) {
		if (getDistanceSQ(pos) > CommonConsts.RADAR_RANGE_SQ) {
			return true;
		}
		return false;
	}

	public boolean planeOutOfSystemRange(Position pos) {
		if (getDistanceSQ(pos) > CommonConsts.SYSTEM_RANGE_SQ) {
			return true;
		}
		return false;
	}

	synchronized public void collision(Plane planeToTest) {
		for (Plane pl : planes) {
			if (pl != planeToTest) {
				if (testCollision(pl.getPosition(), planeToTest.getPosition())) {
					pl.crash();
					planeToTest.crash();
					if (gui != null) {
						gui.setCrash(pl.getPosition());
					}
				}
			}
		}
	}

	synchronized public boolean landingTestOK() {
		List<Plane> planesNearAirport = new ArrayList<Plane>();
		Position ps;

		for (Plane pl : planes) {
			ps = pl.getPosition();
			if (Math.sqrt(ps.x * ps.x + ps.y * ps.y) < CommonConsts.DESTINATION_RADIUS) {
				planesNearAirport.add(pl);
			}
		}
		if (planesNearAirport.size() > 1) {
			for (Plane pl : planesNearAirport) {
				pl.crash();
				if (gui != null) {
					gui.setCrash(pl.getPosition());
				}

			}
			return false;
		}
		return true;
	}

}
