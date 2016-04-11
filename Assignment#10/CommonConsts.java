public class CommonConsts {
	public static final float CRASH_RADIUS = 0.2f;
	public static final float CRASH_RADIUS_SQ = CRASH_RADIUS * CRASH_RADIUS;

	// to obszar rysowany przez GUI
	public static final float AIRCONTROLL_AREA_LENGTH = 65;
	public static final float AIRCONTROLL_AREA_LENGTH_2 = AIRCONTROLL_AREA_LENGTH / 2f;

	// to obszar, ktory jesli zostanie opuszczony, to samolot nie ukonczyl
	// poprawnie zadania
	public static final float SYSTEM_RANGE_SQ = AIRCONTROLL_AREA_LENGTH
			* AIRCONTROLL_AREA_LENGTH;

	// rozmiar celu podrozy
	public static final float DESTINATION_RADIUS = 1.5f;
	public static final float FROM_DEGREE_TO_RADIAN = (float) (Math.PI / 180f);
	public static final float FROM_RADIAN_TO_DEGREE = (float) (180f / Math.PI);

	// czas pomiedzy kolejnymi zapytaniami o kurs
	public static final long COMMUNICATION_PERIOD = 1500;
	// czas calkowania ruchu samolotu
	public static final long INTEGRATION_PERIOD = 27;

	// parametru anteny radaru
	public static final float RADAR_ANTENNA_ROTATION_STEP = 18;
	public static final long RADAR_ANTENNA_ROTATION_PERIOD = 66;
	public static final float RADAR_RANGE = 25;
	public static final float RADAR_RANGE_SQ = RADAR_RANGE * RADAR_RANGE;

	public static final float PLANE_SIZE = 0.7f;
	public static final float PLANE_SIZE_2 = PLANE_SIZE * 0.5f;

	public static class Helper {

		/**
		 * Metoda przeksztalca kat do zakresu 0-360 stopni.
		 * 
		 * @param angle
		 *            kat do przeksztalcenia
		 * @return kat z zakresu 0-360 stopni.
		 */
		public static float angleToRangeFrom0To360Degree(float angle) {

			while (angle < 0) {
				angle += 360;
			}

			int ratio = (int) (angle / 360f);

			return (angle - ratio * 360f);
		}

		public static float angleToRangeFrom0To2PI(float angle) {
			return angleToRangeFrom0To360Degree(angle * FROM_RADIAN_TO_DEGREE)
					* FROM_DEGREE_TO_RADIAN;
		}

		public static float turnDirection(float currentAngle, float newAngle) {
			float delta = angleToRangeFrom0To360Degree(FROM_RADIAN_TO_DEGREE
					* (newAngle - currentAngle));
			return (delta > 180) ? -1 : +1;
		}

	}
}
