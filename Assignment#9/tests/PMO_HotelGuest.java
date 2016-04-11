import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class PMO_HotelGuest implements Runnable {

	private CyclicBarrier barrier;
	private HotelReservationInterface hri;
	private HotelReservationInterface.StayAtTheHotel stayObj;
	private Map<Integer, Integer> reservationRequirements;
	private boolean hotelIsAbleToMeetRequirements;
	private AtomicInteger guestsServed;

	public PMO_HotelGuest(PMO_StayClassFactory stayFactory, long time,
			Map<Integer, Integer> reservationRequirements,
			HotelReservationInterface hri,
			boolean hotelIsAbleToMeetRequirements, AtomicInteger guestsServed ) {
		this.hri = hri;
		this.guestsServed = guestsServed;
		this.reservationRequirements = reservationRequirements;
		this.hotelIsAbleToMeetRequirements = hotelIsAbleToMeetRequirements;
		stayObj = stayFactory.getStayObject(time, reservationRequirements, hotelIsAbleToMeetRequirements );
	}

	public void setBarrier( CyclicBarrier barrier ) {
		this.barrier = barrier;
	}
	
	@Override
	public void run() {
		try {
			barrier.await(); // synchronizacja gosci hotelu
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		long ts = System.currentTimeMillis();
		hri.reservation(reservationRequirements, stayObj);
		guestsServed.incrementAndGet();
		long tf = System.currentTimeMillis(); 

		if (!hotelIsAbleToMeetRequirements) { // hotel nie moze spelnic wymaga!
			if (tf - ts > 100 ) {
				PMO_SystemOutRedirect
						.println("BLAD: wyjscie z metody reservation() dla goscia, ktorego wymagania nie sa spelnialne trwalo za dlugo "
								+ (tf - ts) + " msec");
			}
		}

	}
	
	@Override
	public String toString() {
		String tmp = "Klient rezerwujacy ";
		for ( Map.Entry< Integer, Integer > roomReq : reservationRequirements.entrySet() ) {
			tmp += roomReq.getKey() + "x" + roomReq.getValue() + ";";
		}
		return tmp;
	}
}
