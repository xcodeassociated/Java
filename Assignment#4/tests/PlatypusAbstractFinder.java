/**
 * Klasa abstrakcyjna systemu poszukiwania dziobaka.
 */
abstract public class PlatypusAbstractFinder {
	/**
	 * Metoda rozpoczyna przeszukiwanie od pokoju, ktory zostal wkazany
	 * jako wejscie.
	 * @param entranceToTheBuilding wejscie do budynku.
	 */
	abstract public void find( AbstractRoom entranceToTheBuilding );
	
	/**
	 * Metoda zwraca referencje do pomieszczenia, w ktorej znaleziono 
	 * dziobaka.
	 * @return pomieszczenie z dziobakiem lub null jesli dziobaka nigdzie nie ma.
	 */
	abstract public  AbstractRoom getRoomWithPlatypus();
}
