import java.util.ArrayList;
import java.util.List;


public class Room extends AbstractRoom {
	private final List<AbstractRoom> rooms = new ArrayList<AbstractRoom>();
	private boolean isPlatypusHere;
	private int id;
	private static int counter;
	
	/**
	 * Pepe Pan Dziobak przybywa do tego pokoju!
	 */
	public void PerryThePlatypusHasArrived() {
		isPlatypusHere = true;
	}
	
	public void addRoom( AbstractRoom ar ) {
		rooms.add( ar ); // lista pomieszczen rosnie
	}
	
	@Override
	public boolean hasPlatypus() {
		return isPlatypusHere;
	}

	@Override
	public AbstractRoom[] getDoors() {
		// jesli nie ma pomieszczen na liscie rooms to zwracamy null
		if ( rooms.size() == 0 ) return null;
		
		// jesli cos jest to lista zamieniana jest w tablice
		// w dodatku zwracana jest tablic AbstractRoom[] a ja
		// tu sobie generuje tablice Room[] i jest OK!
		return rooms.toArray( new Room[ rooms.size() ] );
	}
	
	public Room() {
		id = counter; // kazde pomieszczenie ma unikalne ID
		counter++;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj == null ) return false;
		if ( ! ( obj instanceof Room ) ) return false;
		return ( ((Room)obj).id == id );
	}
}
