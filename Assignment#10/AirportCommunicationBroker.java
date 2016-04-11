import java.util.ArrayList;
import java.util.List;

public class AirportCommunicationBroker implements FlightControlInterface {
	private List<FlightControlInterface> airports = new ArrayList<>();

	// rejestracja kontrolera
	public void addAirport(FlightControlInterface ap) {
		airports.add(ap);
	}

	@Override
	public void planeLanded(String flightNumber) {
		for (FlightControlInterface ap : airports) {
			ap.planeLanded(flightNumber);
		}

	}

	@Override
	public void planePosition(String flightNumber, Position currentPosition,
			float currentDirection) {
		for (FlightControlInterface ap : airports) {
			ap.planePosition(flightNumber, currentPosition, currentDirection);
		}
	}

	@Override
	public void planeTarget(String flightNumber, Position target) {
		for (FlightControlInterface ap : airports) {
			ap.planeTarget(flightNumber, target);
		}
	}

	@Override
	public float getFlightCourse(String flightNumber) {

		float course = 0;

		for (FlightControlInterface ap : airports) {
			try {
				course = ap.getFlightCourse(flightNumber);
			} catch (Exception e) {
			}
		}

		return course;
	}
}
