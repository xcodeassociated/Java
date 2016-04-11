
public class SimpleAirport implements FlightControlInterface {

	private String positionToString( Position pos ) {
		return "[ " + pos.x + ", " + pos.y + " ]";
	}
	
	
	@Override
	public void planeTarget(String flightNumber, Position target) {
		System.out.println( "SA Flight " + flightNumber + " to --> " + positionToString( target ) );
	}

	@Override
	public void planePosition(String flightNumber, Position currentPosition,
			float currentDirection) {
		System.out.println( "SA Flight " + flightNumber + " --> " + positionToString( currentPosition ) + " " + currentDirection );
	}

	@Override
	public float getFlightCourse(String flightNumber ) {
		System.out.println( "SA Flight " + flightNumber + " ???");
		throw new RuntimeException(); // zamiast kursu -> wyjatek
	}

	@Override
	public void planeLanded(String flightNumber) {
		System.out.println( "SA Flight " + flightNumber + " landed");
	}

}
