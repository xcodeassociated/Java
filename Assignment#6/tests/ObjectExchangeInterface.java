import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Super wazny interfejs systemu przechowywania obiektow. Tak naprawde to zadanie, na zabawe
 * ze zbiorami, listami, mapami i sortowaniem.
 * 
 * @author Piotr Marek Oramus &copy;
 */
public interface ObjectExchangeInterface {

	/**
	 * Metoda rejestruje uzytkownika w systemie. Poprawne nazwy uzytkownikow
	 * musza byc unikalne czyli nie moga sie powtarzac.
	 * 
	 * @param username
	 *            nazwa uzytkownika
	 * @return true - rejestracja przebiegla prawidlowo, false - uzytkownik o
	 *         podanym username juz jest zarejestrowany.
	 */
	boolean registerUser(String username);

	/**
	 * Zwraca zbior zarejestrowanych nazw uzytkownika
	 * 
	 * @return Zbior nazw zarejestrowanych uzytkownikow, null - brak
	 *         uzytkownikow systemu
	 */
	Set<String> getRegisteredUsers();

	/**
	 * Usuniecie uzytkownika z systemu. Wszystkie obiekty uzytkownika takze sa z
	 * systemu usuwane.
	 * 
	 * @param username
	 *            nazwa uzytkownika, ktory chce zakonczyc prace z systemem
	 * @return true - usunieto uzytkownika, false - uzytkownika nie bylo w
	 *         systemie
	 */
	boolean deregisterUser(String username);

	/**
	 * Metoda dodaje do systemu obiekt. Obiekt jest wiazany z nazwa uzytkownika.
	 * Dzieki <code>extends</code> dodawane obiekty <em>musza</em>
	 * byc zgodne z interfesjem <code>ValueI</code>
	 * Obiekty moga sie wielokrotnie w systemie powtarzac. Nawet jeden
	 * uzytkownik moze posiadac kilka takich samych obiektow.
	 * 
	 * @param username
	 *            nazwa uzytkownika-wlasciciela obiektu
	 * @param obj
	 *            obiekt do przekazania do systemu (tak, wiem, tak naprawde
	 *            system dostaje kopie referencji, ale jakos latwiej pisac, ze
	 *            dostaje obiekt).
	 * @return true - obiekt przyjeto do przechowania, false - bledna nazwa
	 *         uzytkownika, lub przyslano zamiast obiektu null
	 */
	<T extends ValueI> boolean addObjectToSystem(String username, T obj);

	/**
	 * Metoda zwraca posortowana <em>malejaco</em> liste umieszczonych w
	 * systemie obiektow.
	 * 
	 * @param username
	 *            nazwa wlasciciela obiektow
	 * @return lista posortowanych obiektow, null jesli nazwa uzytkownika nie
	 *         jest poprawna, albo gdy ten nie posiada w systemie zadanych
	 *         swoich obiektow
	 */
	List<ValueI> getSortedListOfObjects(String username);

	/**
	 * Metoda zwraca posortowana <em>malejaco</em> mape, w ktorej kluczami sa
	 * referencje do obiektow, zas wartosciami liczba ich powtorzen tego obiektu
	 * na liscie obiektow nalezacych do zadanego uzytkownika.
	 * 
	 * @param username
	 *            nazwa uzytkownika systemu
	 * @return mapa krotnosci wystepowania obiektow. Im wiecej powtorzen obiektu
	 *         tym jest on wczesniej do odnalezienia w przeslanej mapie.
	 *         <tt>null</tt> w przypadku podania blednej nazwy uzytkownika
	 *         lub braku obiektow dla wskazanego uzytkownika.
	 */
	Map<ValueI, Integer> getSortedMap(String username);
}
