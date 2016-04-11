import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.ReentrantLock;

public class PMO_StayClassFactory {
	final private AtomicIntegerArray numberOfOccupiedRooms;
	final private AtomicIntegerArray maxNumberOfOccupiedRooms;
	final private List<PMO_StayAtTheHotelAndTest> listOfCreatedStayObjects = Collections
			.synchronizedList(new ArrayList<PMO_StayAtTheHotelAndTest>());

	private boolean stayObjectTest() {
		for (PMO_StayAtTheHotelAndTest stayTest : listOfCreatedStayObjects) {
			if (!stayTest.test())
				return false;
		}
		return true;
	}

	private boolean occupiedRoomsTest(int[] avaiableRooms,
			int[] possibleOccuption) {
		for (int i = 1; i <= avaiableRooms.length; i++)
			if (maxNumberOfOccupiedRooms.get(i) < possibleOccuption[i - 1]) {
				PMO_SystemOutRedirect
						.println("Blad: wynajeto jednoczesnie "
								+ maxNumberOfOccupiedRooms.get(i)
								+ " pokoi o rozmiarze " + i
								+ " oczekiwano co najmniej "
								+ possibleOccuption[i - 1]);
				return false;
			}

		for (int i = 1; i <= avaiableRooms.length; i++) {
			if (maxNumberOfOccupiedRooms.get(i) > avaiableRooms[i - 1]) {
				PMO_SystemOutRedirect.println("Blad: wynajeto jednoczesnie "
						+ maxNumberOfOccupiedRooms.get(i)
						+ " pokoi, a hotel ma ich tylko "
						+ avaiableRooms[i - 1]);
				return false;
			}
		}
		for (int i = 1; i <= avaiableRooms.length; i++) {
			PMO_SystemOutRedirect
					.println("OK : wynajeto jednoczesnie maksymalnie "
							+ maxNumberOfOccupiedRooms.get(i)
							+ " pokoi, o rozmiarze " + i);
		}

		return true;
	}

	public boolean test(int[] availableRooms, int[] possibleOccuption) {
		PMO_SystemOutRedirect.println("StayClassFactory.test()");
		return stayObjectTest()
				&& occupiedRoomsTest(availableRooms, possibleOccuption);
	}

	public PMO_StayClassFactory(int maxRoomSize) {
		// + 1 bo nie uzywamy indeksu 0
		numberOfOccupiedRooms = new AtomicIntegerArray(maxRoomSize + 1);
		maxNumberOfOccupiedRooms = new AtomicIntegerArray(maxRoomSize + 1);
	}

	private void addRooms(Map<Integer, Integer> occupated) {
		for (Map.Entry<Integer, Integer> occupation : occupated.entrySet()) {
			int or = numberOfOccupiedRooms.addAndGet(occupation.getKey(),
					occupation.getValue()); // dodaje liczbe zajetych
											// pomieszczen
			if (or > maxNumberOfOccupiedRooms.get(occupation.getKey())) {
				synchronized (PMO_StayClassFactory.class) {
					if (or > maxNumberOfOccupiedRooms.get(occupation.getKey())) { // double
																					// check
						maxNumberOfOccupiedRooms.set(occupation.getKey(), or);
					}
				}
			}
		}
	}

	private void decrementRooms(Map<Integer, Integer> occupated) {
		for (Map.Entry<Integer, Integer> occupation : occupated.entrySet()) {
			numberOfOccupiedRooms.addAndGet(occupation.getKey(),
					-occupation.getValue());
		}
	}

	/**
	 * Zwraca obiekt StayAtHotel gotowy do przeslania do hotelu jako
	 * reprezentacja pobytu goscia w hotelu.
	 * 
	 * @param time
	 *            czas pobytu
	 * @param reservationRequirements
	 *            wymagania dotyczace liczby pomieszczen
	 * @return obiekt typu StayAtHotel
	 */
	public PMO_StayAtTheHotelAndTest getStayObject(final long time,
			final Map<Integer, Integer> reservationRequirements,
			final boolean enforceable) {
		PMO_StayAtTheHotelAndTest sath = new PMO_StayAtTheHotelAndTest() {

			private AtomicBoolean startExecuted = new AtomicBoolean(false);
			private AtomicBoolean stopExecuted = new AtomicBoolean(false);
			private AtomicBoolean stayTimeOK = new AtomicBoolean(false);

			private AtomicBoolean callFromMoreThenOneThread = new AtomicBoolean(
					false);

			private long ts;
			private long tf;

			private void show(String message, boolean result) {
				PMO_SystemOutRedirect.println("TEST > " + message + " "
						+ (result ? "OK" : "BLAD!"));
			}

			private boolean enforceability(boolean result) {
				if (enforceable)
					return result;
				else
					return !result;
			}

			@Override
			public boolean test() {
				// uwzgledniamy mozliwosc wykonania zlecenia
				boolean startE = enforceability(startExecuted.get());
				boolean stopE = enforceability(stopExecuted.get());
				boolean timeOK = stayTimeOK.get();
				boolean result;

				if (enforceable) {

					result = startE && stopE && timeOK
							&& (!callFromMoreThenOneThread.get());

					if (!result) {
						show("startExecuted", startE);
						show("stopExecuted", stopE);
						show("stayTimeOK", timeOK);
						show("call from more then one thread",
								!callFromMoreThenOneThread.get());
					}
				} else {

					result = startE && stopE
							&& (!callFromMoreThenOneThread.get());

					if (!result) {
						show("startExecuted", startE);
						show("stopExecuted", stopE);
						show("call from more then one thread",
								!callFromMoreThenOneThread.get());
					}
				}
				return result;

			}

			@Override
			public long getPeriodOfTime() {
				return time;
			}

			@Override
			public void start() {
				if (startExecuted.getAndSet(true)) {
					PMO_SystemOutRedirect
							.println("Blad: kolejny raz wywolano start() na tym samym obiekcie stayAtHotel");
					callFromMoreThenOneThread.set(true);
				}

				ts = System.currentTimeMillis();

				if (stopExecuted.get()) {
					PMO_SystemOutRedirect.println("Wykonano stop przed start");
				}

				addRooms(reservationRequirements);
			}

			@Override
			public void stop() {
				stopExecuted.set(true);
				tf = System.currentTimeMillis();

				decrementRooms(reservationRequirements);

				if (startExecuted.get()) {
					long delta = tf - ts;
					if ((delta >= time) && (delta <= (long) (1.1 * time))) {
						stayTimeOK.set(true);
						// PMO_SystemOutRedirect.println("Czas pobytu " + delta
						// + " powinno byc " + time + " OK!");
					} else {
						PMO_SystemOutRedirect
								.println("BLAD - bledny czas pobytu w hotelu ");
						PMO_SystemOutRedirect.println("BLAD - powinno byc "
								+ time + " jest " + delta);
						stayTimeOK.set(false);
					}
				} else {
					PMO_SystemOutRedirect
							.println("BLAD - nie wykonano start przed stop");
				}
			}
		};

		listOfCreatedStayObjects.add(sath);
		return sath;
	}

}
// TODO zliczanie zrealizowania wszystkich zadan
