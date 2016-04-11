import org.w3c.dom.Document;

public interface ObjectToDOMInterface {
	/**
	 * Metoda zwraca obiekt reprezentujacy strukture pol i ich wartosc dla przeslanego obiektu.
	 * @param o - analizowany obiekt
	 * @return Obiektowa reprezentacja przeslanego obiektu
	 */
	public Document getDocument( Object o );
}
