/**
 * Interfejs systemu kontroli lotu.
 *
 */
public interface FlightControlInterface {
	/**
	 * Metoda ta pozwala zglosic sie samolotowi i przekazac
	 * swoj identyfikator oraz pozycje, do ktorej zmierza.
	 * 
	 * @param flightNumber id samolotu
	 * @param target pozycja docelowa, polozenie [0,0] oznacza to lotnisko i chec ladowania na min.
	 */
	void planeTarget(String flightNumber, Position target);

	/**
	 * Metoda pozwala przekazac kontroli lotu informacje o aktualnym polozeniu i 
	 * kursie samolotu.
	 * @param flightNumber id samolotu
	 * @param currentPosition aktualne polozenie
	 * @param currentDirection aktualny kierunek lotu
	 */
	void planePosition(String flightNumber, Position currentPosition,
			float currentDirection);

	/**
	 * Metoda pozwala pobraz zalecany kurs dla danego lotu.
	 * 
	 * @param flightNumber id samolotu proszacego o kurs
	 * @return kurs, ktory samolot ma obrac
	 */
	float getFlightCourse(String flightNumber);

	/**
	 * Samolot potwierdza szczesliwe ladowanie.
	 * 
	 * @param flightNumber id samolotu, ktory wyladowal
	 */
	void planeLanded(String flightNumber);
} 
