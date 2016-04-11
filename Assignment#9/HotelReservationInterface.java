import java.util.Map;

/**
 * Interfejs rownoleglego systemu zglaszania rezerwacji miejsc
 * hotelowych.
 */
public interface HotelReservationInterface {
	/**
	 * Metoda ustawia zasoby hotelu. Przekazana mapa zawiera informacje o rozmiarze pokoju (liczba
	 * osob) i liczbie takich pokoi w hotelu. Metoda ta wykonywana jest jednokrotnie
	 * i przed pierwsza rezerwacja miejsc.
	 * 
	 * @param sizeNumber Mapa klucz to rozmiar pokoju (N-osobowy), wartosc to liczba takich
	 * pokoi w hotelu.
	 */
	public void setNumberOfRooms( Map<Integer, Integer> sizeNumber );
	
	/**
	 * Interfejs obiektu reprezentujacego czasowy pobyt goscia w hotelu.
	 */
	public interface StayAtTheHotel {
		/**
		 * Okres czasu, w ktorym gosc chce przebywac w hotelu
		 * @return liczba milisekund okresu czasu, ktory gosc chce spedzic w hotelu.
		 */
		long getPeriodOfTime();
		
		/**
		 * Rozpoczecie pobytu goscia w hotelu.
		 */
		void start();
		
		/**
		 * Zakonczenie pobytu w hotelu. Metoda musi byc wykonana nie wczesniej niz
		 * po uplynieciu zadanego czasu pobytu. Metoda nie moze byc wykonana
		 * pozniej niz po uplynieciu 1.1 * wymaganego czasu pobytu.
		 */
		void stop();
	}
	
	/**
	 * Metoda do zgloszenia checi rezerwacji pokoi. Zglaszane sa wymogi dotyczace rezerwacji, czyli
	 * liczba pokoi o wymaganym rozmiarze. 
	 * Rezerwacje moze wykonywac zarowno pojedynczy gosc jak i manager wycieczki, ktory
	 * bedzie chcial zarezerwowac wiele pokoi i byc moze roznych rozmiarow. 
	 * Metoda przekazuje takze obiekt reprezentujacy
	 * pobyt goscia/gosci w hotelu przez pewien czas. Zarowno wieloosobowa wycieczka, jak i pojedynczy gosc
	 * hotelu, nazywani sa dalej po prostu "gosciem". 
	 * <br><br>
	 * Metoda moze byc wywolana przez 
	 * wielu rownoczesnych uzytkownikow systemu (osobne watki). W przypadku
	 * tymczasowego braku mozliwosc zarezerwowania wszystkich wymaganych pomieszczen i rozpoczecia
	 * pobytu tego "goscia", metoda wstrzymuje prace uzytkownika systemu do chwili
	 * opuszczenia hotelu przez innego/innych "gosci" i powrotu mozliwosci zaakceptowania 
	 * tej rezerwacji i rozpoczecia pobytu tego obiektu-"goscia" (wykonania 
	 * metody start() z interfejsu StayAtTheHotel). Aby rozpoczac pobyt "goscia", 
	 * musza byc spelnione wszystkie podane w wywolaniu metody wymagania.
	 * W przypadku permanentnego braku mozliwosci spelnienia wymagan (brak stosownych
	 * pomieszczen w hotelu np. pod wzgledem rozmiaru czy liczebnosci) metoda
	 * konczy prace natychmiast, obiekt typu StayAtTheHotel nie jest
	 * uzywany tj. nie sa na nim wykonywane zadne metody. 
	 * 
	 * @param requirements wymagane rozmiary pokoi i liczba takich pokoi. Kluczem jest rozmiar pokoju, wartoscia 
	 * wymagana liczba takich pokoi.
	 * @param obj obiekt reprezentujacy pobyt "goscia" w hotelu
	 */
	public void reservation( Map< Integer, Integer> requirements, StayAtTheHotel obj );
	
	/**
	 * Metoda zwraca posortowana rosnaco mape zawierajaca informacje o liczbie
	 * wolnych pomieszczen w hotelu. Mapa ma uwzgledniac wszystkie 
	 * rozmiary pomieszczen, ktore sa dostepne w hotelu. Kluczem w mapie
	 * jest rozmiar pokoju, wartoscia zas aktualna liczba wolnych pokoi.
	 * Metoda moze byc rownoczesnie wywolywana przez wielu uzytkownikow
	 * systemu. Metoda ma zwracac aktualne informacje i dzialac nawet w 
	 * trakcie oczekiwania innych uzytkownikow na rezerwacje miejsc.
	 * 
	 * @return posortowana mapa zawierajaca informacje o liczbie wolnych pokoi
	 * okreslonego rozmiaru
	 */
	public Map<Integer,Integer> getNumberOfFreeRooms();
}
