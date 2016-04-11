/**
 * Interfejs gry Wyjatkowy Saper &copy; Piotr Marek Oramus.
 * <br><br>
 * W grze komunikacja gracz-silnik gry odbywa sie w duzej mierze
 * za pomoca zwracanych wyjatkow.
 */
public interface WyjatkowySaperGameInterface {
	
	/**
	 * Klasa-wyjatek zawiera informacje o tym ile jest min w poblizu. Minimum 1
	 * maksimum 8 (pole otoczone przez miny - trzeba bardzo uwazac).
	 */
	class UwagaMiny extends Exception {
		private static final long serialVersionUID = -635029639090990600L;
		public final int ileMinWPoblizu;
		public UwagaMiny( int ile ) {
			ileMinWPoblizu = ile;
		}
	}

	/**
	 * Klasa-wyjatek. Na planszy do gry doszlo do wypadku. Trzeba wezwac kolejnego 
	 * sapera, bo ten aktualny, powiedzmy, lekko sie juz zuzyl (drugie z z kropka i przekreslone l).
	 */
	class Bum extends Exception {
		private static final long serialVersionUID = -8374901789124231073L;
	}	

	/**
	 * Klasa-wyjatek. Sygnalizacja szczesliwego zakonczenia gry. Po wystapieniu
	 * tego wyjatku nie wolno juz wykonywac jakichkolwiek dzialan poprzez
	 * metody interfejsu WyjatkowySaperGameInterface.
	 */
	class ToJestWyjscie extends Exception {
		private static final long serialVersionUID = 3587732308832887005L;
	}	

	/**
	 * Zaproponowany rozmiar planszy jest za duzy w porownaniu z tym prawdziwym.
	 */
	class ZaDuzy extends Exception {
		private static final long serialVersionUID = 7932915008650054060L;		
	}
	
	/**
	 * Zaproponowany rozmiar planszy jest za maly w porownaniu z tym prawdziwym.
	 */
	class ZaMaly extends Exception {
		private static final long serialVersionUID = -7790027885931520405L;		
	}

	/**
	 * Coz, przegrales. Nie ma juz wolnych saperow... nikt juz Ci nie pomoze...
	 * Szkoda...
	 */
	class NieMaJuzNikogo extends Exception {
		private static final long serialVersionUID = -1304418897911567979L;
	}
	
	/**
	 * Klasa-wyjatek. Pozwala dowiedziec sie iloma probami dysponuje gracz, aby ustalic
	 * poprawny rozmiar planszy. Liczba prob bedzie na pewno wystarczajaca by dokladnie poznac
	 * rozmiar planszy, oczywiscie o ile tylko zostana inteligentnie uzyte. 
	 */
	class PoczatkowaLiczbaProb extends Exception {
		private static final long serialVersionUID = 9075697896287543161L;
		public final int poczatkowaLiczbaProb;
		public PoczatkowaLiczbaProb( int ile ) {
			poczatkowaLiczbaProb = ile;
		}
	}
	
	/**
	 * Wzywamy na pomoc kolejnego sapera. Ich liczba jest ograniczona, ale na pewno wieksza niz 1.
	 *  
	 * @throws NieMaJuzNikogo wyjatek pojawi sie, gdy nie bedzie juz zadnego sapera, ktory moze pomoc
	 * w kontynuacji gry i wykryciu wyjscia. Tak, to juz koniec...
	 */
	void nastepnySaper() throws NieMaJuzNikogo;

	/**
	 * Metoda pozwalajaca wykryc/zgadnac rozmiar planszy. Plansza bedzie na pewno kwadratowa.
	 * Jesli zostanie przekroczony limit prob, metoda odpowiada losowo na pytania o 
	 * rozmiar planszy.
	 * 
	 * @param propozycja oto propozycja rozmiaru planszy, metoda odpowiada wyjatkami gdy nie jest ona prawdziwa
	 * @throws ZaDuzy zaproponownany rozmiar jest za duzy
	 * @throws ZaMaly zaproponownany rozmiar jest za maly
	 * @throws PoczatkowaLiczbaProb pierwsze wywolanie metody zawsze konczy sie zwroceniem tego wyjatku. W nim 
	 * znajdowac sie bedzie informacja o liczbie przyslugujacych prob wykrycia rozmiaru planszy. Trzeba ich uzyc 
	 * rozsadnie - na pewno wtedy wystarczy. 
	 */
	void rozmiarPlanszyDoGry( int propozycja ) throws ZaDuzy, ZaMaly, PoczatkowaLiczbaProb;
	
	/**
	 * Metoda pozwalajaca sprawdzic dane pole planszy. Mozna uzyskac informacje o:
	 * <ul>
	 * <li>braku min - brak wyjatkow
	 * <li>wykryciu w sasiedztwie okreslonej liczby min (wyjatek UwagaMiny)
	 * <li>wybuchu miny - ze znanym i typowym skutkiem dla aktualnego sapera (wyjatek Bum)
	 * <li>szczesliwym znalezieniu wyjscia (ToJestWyjscie)
	 * </ul>
	 * Uwaga w przypadku podania blednej pozycji na planszy (tj. pozycji spoza planszy) zawsze nastapi
	 * wybuch!
	 * <br>
	 * <br>
	 * @param index1 pierwszy indeks tablicy reprezentujacej plansze do gry 
	 * @param index2 drugi indeks tablicy reprezentujacej plansze do gry
	 * @throws UwagaMiny informacja o wykryciu w poblizu pewnej liczby min
	 * @throws Bum w pewnym sensie, to jest informacja o poprawnym odnalezieniu miny ;-)
	 * @throws ToJestWyjscie informacja o odnalezieniu wyjscia. Sukces! Brawo!!!
	 */
	void sprawdzam( int index1, int index2 ) throws UwagaMiny, Bum, ToJestWyjscie;
}
