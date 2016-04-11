/**
 * Abstarakcyjna klasa reprezentujaca pojedyncze pomieszczenie.
 * 
 * @author oramus
 *
 */
abstract public class AbstractRoom {
	/**
	 * Zwraca informacje czy w pokoju jest dziobak.
	 * @return true - dziobak jest w tym pokoju, false - nie ma dziobaka (szkoda)
	 */
	abstract public boolean hasPlatypus();
	
	/**
	 * Zwraca tablice zawierajaca referencje do pomieszczen, do ktorych mozna
	 * sie dostac z tego pokoju.
	 * 
	 * @return tablica pomieszczen, do ktorych mozna dotrzec z tego pokoju. 
	 * Jako wynik dzialania metody moze pojawic sie <tt>null</tt> - 
	 * oznacza to, ze z tego pomieszczenia nie mozna dotrzec do
	 * zadnego <b>nowego</b> pomieszczenia. Np. jesli w jakims ciagu wystepuja pomieszczenia:
	 * <br>
	 * 1->2->3->4
	 * <br>
	 * to getDoors w pokoju 4 moze albo zrocic jednoelementowa tablice zawierajaca
	 * referencje do pomieszczenia 3, albo po prostu <tt>null</tt> informujac, ze nic nowego
	 * na tej trasie sie nie napotka. 
	 */
	abstract public AbstractRoom[] getDoors();
}
