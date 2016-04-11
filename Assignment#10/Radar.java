import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Radar {
	private List<TransponderInterface> planeList = Collections
			.synchronizedList(new ArrayList<TransponderInterface>());
	private FlightControlInterface communicationInterface;
	private AtomicInteger antennaCurrentAngle = new AtomicInteger(0);
	
	public Radar(FlightControlInterface communicationInterface) {
		this.communicationInterface = communicationInterface;
		Timer tm = new Timer(true);
		tm.scheduleAtFixedRate(new Antenna(),
				CommonConsts.RADAR_ANTENNA_ROTATION_PERIOD,
				CommonConsts.RADAR_ANTENNA_ROTATION_PERIOD);
	}

	synchronized public void addObject(TransponderInterface pl) {
		planeList.add(pl);
	}

	synchronized public void removeObject(TransponderInterface pl) {
		planeList.remove(pl);
	}
	
	public int getAntennaCurrentAngle() {
		return antennaCurrentAngle.get();
	}

	private class Antenna extends TimerTask {
		private float antennaAngle = 0;
		private float nextAntennaAngle;
		private float distanceToObjectSQ;
		private Position pos;
		private float objectAngle;

		private float distanceSQ(Position pos) {
			return pos.x * pos.x + pos.y * pos.y;
		}
		
		@Override
		public void run() {
			nextAntennaAngle = antennaAngle
					+ CommonConsts.RADAR_ANTENNA_ROTATION_STEP;
			synchronized ( Radar.this ) {
				for (TransponderInterface ti : planeList) {
					pos = ti.getPosition();
					distanceToObjectSQ = distanceSQ(pos);
					if (distanceToObjectSQ < CommonConsts.RADAR_RANGE_SQ ) { // obiekt w zasiegu
																// radaru
						objectAngle = CommonConsts.Helper
								.angleToRangeFrom0To360Degree((float) (Math.atan2(
										pos.y, pos.x) * CommonConsts.FROM_RADIAN_TO_DEGREE));

						// obiekt jest widziany przez antene - system otrzymuje informacje o polozeniu
						if ((objectAngle >= antennaAngle)
								&& (objectAngle <= nextAntennaAngle)) {
							communicationInterface.planePosition(
									ti.getFlightNumber(), pos, ti.getDirection()
											* CommonConsts.FROM_RADIAN_TO_DEGREE);
						}

					}
				}				
			}
			antennaCurrentAngle.set( (int)( antennaAngle ) );
			antennaAngle = CommonConsts.Helper
					.angleToRangeFrom0To360Degree(nextAntennaAngle);
		}
	}
}
