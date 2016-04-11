import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Start {
	private static int testCounter = 1;

	private static Map<Integer, Integer> sizeGenerator(int[] rooms) {
		Map<Integer, Integer> hotelSize = new HashMap<>();

		for (int i = 0; i < rooms.length; i++) {
			hotelSize.put(i + 1, rooms[i]);
		}

		return hotelSize;
	}

	private static boolean join(List<Thread> threads, long maxWait) {

		long tend = System.currentTimeMillis() + maxWait;

		PMO_SystemOutRedirect.println("JOIN : Maksymalny czas oczekiwania to "
				+ maxWait + " msec");

		for (Thread th : threads) {
			try {
				th.join(maxWait / 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() > tend) {
				PMO_SystemOutRedirect
						.println("Blad: nie udalo sie doczekac na zakonczenie watku \""
								+ th.getName() + "\"");
				return false;
			}
		}

		return true;
	}

	private static List<Thread> createAndStartThreads(
			List<PMO_HotelGuest> guests) {
		List<Thread> threads = new ArrayList<Thread>(guests.size());
		for (PMO_HotelGuest guest : guests) {
			Thread th = new Thread(guest);
			th.setName("Watek dla goscia " + guests.indexOf(guest) + " " + guest.toString());
			th.setDaemon(true);
			th.start();
			threads.add(th);
		}

		return threads;
	}

	private static void setNumberOfRoomsAndTestIt(
			HotelReservationInterface hri, int[] avaiableRooms) {
		Map<Integer, Integer> hotelSize = sizeGenerator(avaiableRooms);
		hri.setNumberOfRooms(hotelSize);

		Map<Integer, Integer> hotelSizeReceived = hri.getNumberOfFreeRooms();

		for (Map.Entry<Integer, Integer> entry : hotelSize.entrySet()) {
			if (!hotelSizeReceived.containsKey(entry.getKey())) {
				PMO_SystemOutRedirect
						.println("BLAD krytyczny: hotel zeznaje brak pomieszczen o zapodanym rozmiarze");
				System.exit(0);
			}
			if (hotelSizeReceived.get(entry.getKey()) != entry.getValue()) {
				PMO_SystemOutRedirect
						.println("BLAD krytyczny: hotel zeznaje inna liczbe pomieszczen niz zapodana");
				System.exit(0);
			}
		}
	}

	private static void sleepUntil(long time) {

		long delta = time - System.currentTimeMillis();

		if (delta > 0) {
			PMO_SystemOutRedirect.println("Extra sleep " + delta + " msec");
			try {
				Thread.sleep(delta);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private static void setBarrier(List<PMO_HotelGuest> guests) {
		final CyclicBarrier barrier = new CyclicBarrier(guests.size(),
				new Runnable() {

					@Override
					public void run() {
						PMO_SystemOutRedirect
								.println("Rozpoczyna sie rezerwacja pokoi...");
					}
				});
		for (PMO_HotelGuest guest : guests)
			guest.setBarrier(barrier);
	}

	private static boolean allClientsHaveBeenServerved(int counter, int size) {
		if (counter != size) {
			PMO_SystemOutRedirect
					.println("BLAD: NIE obsluzono wszystkich klientow");
			return false;
		} else {
			PMO_SystemOutRedirect.println("OK: Obsluzono wszystkich klientow");
			return true;
		}
	}

	private static boolean startTestsAndWait(List<PMO_HotelGuest> guests,
			long timeout) {
		long time = System.currentTimeMillis() + timeout;
		boolean joinOK = join(createAndStartThreads(guests), timeout);

		sleepUntil(time);

		return joinOK;
	}

	private static boolean showReport(boolean joinOK, boolean otherTestsOK) {
		PMO_SystemOutRedirect.println("TEST " + testCounter + " - join "
				+ (joinOK ? "" : "NIE") + " w pelni zostal wykonany");
		PMO_SystemOutRedirect.println("TEST " + testCounter + " -      "
				+ (otherTestsOK ? "" : "NIE") + " ZOSTAL ZALICZONY");
		testCounter++;
		PMO_SystemOutRedirect
				.println("-------------------------------------------------------------------");
		return joinOK && otherTestsOK;
	}

	private static boolean test1() {

		// Tylko pojedyncze pokoje

		final int SINGLE_ROOMS = 10;
		final int GUESTS = 30;
		final long STAY_TIME = 250;
		final int[] avaiableRooms = new int[] { SINGLE_ROOMS };
		final List<PMO_HotelGuest> guests = new ArrayList<PMO_HotelGuest>(
				GUESTS);
		final PMO_StayClassFactory stayFactory = new PMO_StayClassFactory(
				avaiableRooms.length);
		final AtomicInteger guestsCounter = new AtomicInteger();
		final HotelReservationInterface hri = new HotelReservation();

		setNumberOfRoomsAndTestIt(hri, avaiableRooms);

		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 5,
					sizeGenerator(new int[] { 1 }), hri, true, guestsCounter));
		}

		setBarrier(guests);

		boolean joinOK = startTestsAndWait(guests, STAY_TIME * guests.size());
		boolean result = stayFactory.test(avaiableRooms, new int[] { 8 });
		result &= allClientsHaveBeenServerved(guestsCounter.get(),
				guests.size());

		return showReport(joinOK, result);
	}

	private static boolean test2() {

		// 3 rozne rozmiary pokoi, same wycieczki

		final int SINGLE_ROOMS = 10;
		final int GUESTS = 30;
		final long STAY_TIME = 250;
		final int[] avaiableRooms = new int[] { SINGLE_ROOMS, 2 * SINGLE_ROOMS,
				4 * SINGLE_ROOMS };
		final List<PMO_HotelGuest> guests = new ArrayList<PMO_HotelGuest>(
				GUESTS);
		final PMO_StayClassFactory stayFactory = new PMO_StayClassFactory(
				avaiableRooms.length);
		final AtomicInteger guestsCounter = new AtomicInteger();
		final HotelReservationInterface hri = new HotelReservation();

		setNumberOfRoomsAndTestIt(hri, avaiableRooms);

		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 5,
					sizeGenerator(new int[] { 1, 2, 4 }), hri, true,
					guestsCounter));
		}

		setBarrier(guests);

		boolean joinOK = startTestsAndWait(guests, STAY_TIME * guests.size());
		boolean result = stayFactory.test(avaiableRooms,
				new int[] { 8, 16, 32 });
		result &= allClientsHaveBeenServerved(guestsCounter.get(),
				guests.size());

		return showReport(joinOK, result);
	}

	private static boolean test3() {

		// 3 rozne rozmiary pokoi, wycieczki i pojedyncze wynajmy pokoi

		final int SINGLE_ROOMS = 10;
		final int GUESTS = 30;
		final long STAY_TIME = 150;
		final int[] avaiableRooms = new int[] { SINGLE_ROOMS, 2 * SINGLE_ROOMS,
				4 * SINGLE_ROOMS };
		final List<PMO_HotelGuest> guests = new ArrayList<PMO_HotelGuest>(
				GUESTS);
		final PMO_StayClassFactory stayFactory = new PMO_StayClassFactory(
				avaiableRooms.length);
		final AtomicInteger guestsCounter = new AtomicInteger();
		final HotelReservationInterface hri = new HotelReservation();

		setNumberOfRoomsAndTestIt(hri, avaiableRooms);

		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 1, 2, 4 }), hri, true,
					guestsCounter));
		}
		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 2, 4, 8 }), hri, true,
					guestsCounter));
		}
		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 2, 0, 0 }), hri, true,
					guestsCounter));
		}
		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 0, 4, 0 }), hri, true,
					guestsCounter));
		}
		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 0, 0, 8 }), hri, true,
					guestsCounter));
		}

		setBarrier(guests);

		boolean joinOK = startTestsAndWait(guests, STAY_TIME * guests.size());
		boolean result = stayFactory.test(avaiableRooms,
				new int[] { 8, 16, 32 });
		result &= allClientsHaveBeenServerved(guestsCounter.get(),
				guests.size());

		return showReport(joinOK, result);
	}

	private static boolean test4() {

		// 3 rozne rozmiary pokoi, wycieczki i pojedyncze wynajmy pokoi
		// do tego uzytkownicy, ktorych wymagan nie mozna zrealizowac

		final int SINGLE_ROOMS = 10;
		final int GUESTS = 30;
		final long STAY_TIME = 150;
		final int[] avaiableRooms = new int[] { SINGLE_ROOMS, 2 * SINGLE_ROOMS,
				4 * SINGLE_ROOMS };
		final List<PMO_HotelGuest> guests = new ArrayList<PMO_HotelGuest>(
				GUESTS);
		final PMO_StayClassFactory stayFactory = new PMO_StayClassFactory(
				avaiableRooms.length);
		final AtomicInteger guestsCounter = new AtomicInteger();
		final HotelReservationInterface hri = new HotelReservation();

		setNumberOfRoomsAndTestIt(hri, avaiableRooms);

		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 1, 2, 4 }), hri, true,
					guestsCounter));
		}
		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 2, 4, 8 }), hri, true,
					guestsCounter));
		}
		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 2, 0, 0 }), hri, true,
					guestsCounter));
		}
		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 0, 2 * SINGLE_ROOMS + 1, 0 }),
					hri, false, guestsCounter));
		}
		for (int i = 0; i < GUESTS; i++) {
			guests.add(new PMO_HotelGuest(stayFactory, STAY_TIME - i * 2,
					sizeGenerator(new int[] { 0, 0, 4 * SINGLE_ROOMS + 1 }),
					hri, false, guestsCounter));
		}

		setBarrier(guests);

		boolean joinOK = startTestsAndWait(guests, STAY_TIME * guests.size());
		boolean result = stayFactory.test(avaiableRooms,
				new int[] { 8, 16, 32 });
		result &= allClientsHaveBeenServerved(guestsCounter.get(),
				guests.size());

		return showReport(joinOK, result);
	}

	public static void main(String[] args) {
		PMO_SystemOutRedirect.startRedirectionToNull();
		if (test1())
			if (test2())
				if (test3())
					test4();
	}
}
