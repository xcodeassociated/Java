import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FinderTest {
	
	// ten przelacznik wylacza pojawianie sie null-a w odpowiedzi na
	// getDoots - pomieszczenie zawiera co najmniej jedne drzwi - do siebie samego.
	private static final boolean NULL_BLOCKADE = false;
	
	private PlatypusAbstractFinder paf;
	private List<Room> lar;

	@Before
	public void createFinder() {
		paf = new PlatypusFinder();
		lar = new ArrayList<Room>();

		// tworzymy pewna liczbe gotowych do uzycia pomieszczen
		// w zadnym nie ma dziobaka i nie sa ze soba polaczone
		for (int i = 0; i < 30; i++) {
			lar.add(new Room());
			if ( NULL_BLOCKADE ) addRoomToRoom( i, i);
		}
	}

	private void addRoomToRoom(int i, int j) {
		lar.get(i).addRoom(lar.get(j));
	}

	private void addPlatypus(int i) {
		lar.get(i).PerryThePlatypusHasArrived();
	}

	private AbstractRoom getRoom(int i) {
		return lar.get(i);
	}

	private void simpleLabirinth() {
		addRoomToRoom(0, 1);
		addRoomToRoom(1, 2);
		addRoomToRoom(2, 3);
		addRoomToRoom(3, 4);
	}

	private void notSoSimpleLabirinth() {
		addRoomToRoom(0, 1);
		addRoomToRoom(1, 2);
		addRoomToRoom(2, 3);
		addRoomToRoom(3, 4);
		addRoomToRoom(3, 5);
		addRoomToRoom(4, 6);
		addRoomToRoom(6, 7);
		addRoomToRoom(6, 8);
		addRoomToRoom(5, 9);
		addRoomToRoom(9, 10);
	}

	private void withLoopsLabirinth() {
		addRoomToRoom(0, 1);
		addRoomToRoom(1, 2);
		addRoomToRoom(2, 3);
		addRoomToRoom(3, 4);
		addRoomToRoom(3, 5);
		addRoomToRoom(4, 6); // tu 6
		addRoomToRoom(2, 6); // i tu 6
		addRoomToRoom(6, 7);
		addRoomToRoom(6, 8);
		addRoomToRoom(6, 12);
		addRoomToRoom(6, 13);
		addRoomToRoom(13, 14);
		addRoomToRoom(14, 15); // tu 15
		addRoomToRoom(6, 9); // tu 9
		addRoomToRoom(5, 9); // i tu 9
		addRoomToRoom(5, 6); // i jeszcze raz 6
		addRoomToRoom(9, 10);
		addRoomToRoom(9, 11);
		addRoomToRoom(11, 15); // znowu 15
		addRoomToRoom(15, 16);
	}

	private void withLoopsLabirinth2() {
		addRoomToRoom(0, 1);
		addRoomToRoom(1, 2); // 1->2
		addRoomToRoom(2, 1); // 2->1
		addRoomToRoom(2, 3);
		addRoomToRoom(3, 4);
		addRoomToRoom(4, 4); // 4->4
		addRoomToRoom(4, 3); // 4->3
		addRoomToRoom(3, 5);
		addRoomToRoom(4, 6); // tu 6
		addRoomToRoom(2, 6); // i tu 6
		addRoomToRoom(6, 7);
		addRoomToRoom(6, 8);
		addRoomToRoom(6, 6); // 6->6
		addRoomToRoom(6, 12);
		addRoomToRoom(6, 13);
		addRoomToRoom(13, 14);
		addRoomToRoom(14, 15); // tu 15
		addRoomToRoom(6, 9); // tu 9
		addRoomToRoom(5, 9); // i tu 9
		addRoomToRoom(5, 6); // i jeszcze raz 6
		addRoomToRoom(9, 10);
		addRoomToRoom(9, 11);
		addRoomToRoom(11, 15); // znowu 15
		addRoomToRoom(15, 16);
		addRoomToRoom(16, 15); // 15->16->15
	}

	private AbstractRoom search() {
		AbstractRoom ar = null;
		try {
			paf.find(getRoom(0));
			ar = paf.getRoomWithPlatypus();
		} catch (Exception e) {
			fail("Doszlo do niespodziewanego wyjatku " + e.toString());
		}
		return ar;
	}

	private void test() {
		AbstractRoom ar = search();

		assertNull("Nie ma dziobaka, szuka agenta J", ar);
	}

	private void test(int roomWithPlatypus) {
		addPlatypus(roomWithPlatypus); // umieszczam w pokoju dziobaka
		AbstractRoom ar = search(); // poszukuje

		// sprawdzam
		assertNotNull("W jednym z pomieszczen jest dziobak, null niedozwolony",
				ar);

		assertTrue("Dziobak _jest_ w jednym z pomieszczen, ale nie w tym!",
				getRoom(roomWithPlatypus).equals(ar));
	}

	// TESTY
	// N - test na nie
	// Y - test na tak

	@Test(timeout = 1000)
	public void simpleTestNoLoopsY() {
		simpleLabirinth();
		test(4);
	}

	@Test(timeout = 1000)
	public void simpleTestNoLoopsN() {
		simpleLabirinth();
		test();
	}

	@Test(timeout = 1000)
	public void notSoSimpleTestNoLoopsN() {
		notSoSimpleLabirinth();
		test();
	}

	@Test(timeout = 1000)
	public void notSoSimpleTestNoLoopsY() {
		notSoSimpleLabirinth();
		test(9);
	}

	@Test(timeout = 1000)
	public void loopsY() {
		withLoopsLabirinth();
		test(16);
	}

	@Test(timeout = 1000)
	public void loopsN() {
		withLoopsLabirinth();
		test();
	}

	@Test(timeout = 1000)
	public void loops2Y() {
		withLoopsLabirinth2();
		test(16);
	}

	@Test(timeout = 1000)
	public void loops2N() {
		withLoopsLabirinth2();
		test();
	}
}
