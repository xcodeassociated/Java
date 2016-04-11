/**
 * Interfejs programu-gracza w gre Wyjatkowy Saper.
 */
public interface WyjatkowySaperPlayerInterface {
	/**
	 * Metoda przekazuje referencje do obiektu odpowiadzialnego za
	 * obsluge rozgrywki
	 * 
	 * @param gi referecja do obiektu zgodnego z WyjatkowySaperGameInterface
	 */
	public void setGameInterface( WyjatkowySaperGameInterface gi );
	
	/**
	 * Metoda przekazuje polozenie pola, w ktorym na pewno nie ma miny i nie 
	 * w jego najblizszym sasiedztwie min takze nie ma.
	 * Od niego mozna bezpiecznie zaczac poszukiwanie wyjscia.
	 * 
	 * @param index1 pierwszy indeks tablicy reprezentujacej plansze do gry
	 * @param index2 drugi indeks tablicy reprezentujacej plansze do gry
	 */
	public void start( int index1, int index2 );
}
