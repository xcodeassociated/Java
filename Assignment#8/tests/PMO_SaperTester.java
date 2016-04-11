import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class PMO_SaperTester extends PMO_WyjatkowySaper {

	private PMO_WyjatkowySaper saper;
	private WyjatkowySaperPlayerInterface player;

	@Before
	public void createPlayer() {
		player = new WyjatkowySaper();
	}

	public PMO_SaperTester() {
		super(1, 1);
	}

	private void startMission(int i1, int i2) {
		PMO_SystemOutRedirect
				.println("------------S--T--A--R--T----------------");
		saper.show(i1, i2);
		if (saper.startCorrect(i1, i2)) {
			PMO_SystemOutRedirect.startRedirectionToNull();
			try {
				player.setGameInterface(saper);
				player.start(i1, i2);
			} catch (Exception e) {
				PMO_SystemOutRedirect.println("Wychwycono wyjatek "
						+ e.getMessage());
				PMO_SystemOutRedirect.returnToStandardStream();
				e.printStackTrace();
				fail("Wyjatek w trakcie testu");
			}
			PMO_SystemOutRedirect.returnToStandardStream();
			System.out.println( "---- Po zakonczeniu misji: ----");
			saper.show(i1, i2);
			assertTrue("Spodziewano sie sukcesu misji ",
					saper.getMissionAccomplishedFlag());
			assertFalse("W trakcie przeszukiwania nie powinien nastapic blad",
					saper.getErrorFlag());
		}
		PMO_SystemOutRedirect
				.println("---------------E-N-D---------------------");
	}

	@Test(timeout = 1000)
	public void verySimple() {
		saper = new PMO_WyjatkowySaper(2, 10);

		saper.setMine(3, 1);
		saper.setMine(5, 1);
		saper.setMine(7, 1);

		saper.setExit(9, 1);

		startMission(1, 1);
	}

	@Test(timeout = 1000)
	public void simple() {
		saper = new PMO_WyjatkowySaper(2, 10);

		saper.setMine(1, 1);
		saper.setMine(1, 2);
		saper.setMine(6, 5);
		saper.setMine(5, 7);
		saper.setMine(3, 7);
		saper.setMine(6, 4);
		saper.setMine(2, 3);
		saper.setMine(4, 9);
		saper.setExit(8, 8);

		startMission(5, 1);
	}

	@Test(timeout = 1000)
	public void notSoSimple3S() {
		saper = new PMO_WyjatkowySaper(3, 10);

		saper.setMine(1, 1);
		saper.setMine(1, 2);
		saper.setMine(5, 5);
		saper.setMine(5, 7);
		saper.setMine(3, 7);
		saper.setMine(6, 4);
		saper.setMine(2, 3);
		saper.setMine(4, 9);
		saper.setExit(8, 8);

		startMission(5, 1);
	}

	@Test(timeout = 1000)
	public void notSoSimple2S() {
		saper = new PMO_WyjatkowySaper(2, 10);

		saper.setMine(1, 1);
		saper.setMine(1, 2);
		saper.setMine(5, 5);
		saper.setMine(5, 7);
		saper.setMine(3, 7);
		saper.setMine(6, 4);
		saper.setMine(2, 3);
		saper.setMine(4, 9);
		saper.setExit(8, 8);

		startMission(5, 1);
	}	

	@Test(timeout = 2000)
	public void notSoSimple2_S() {
		saper = new PMO_WyjatkowySaper(2, 15);

		for ( int i = 0; i < 12; i++ ) {
			saper.setMine(3, i);			
			saper.setMine(11, i);			
		}
		for ( int i = 3; i < 15; i++ ) {
			saper.setMine(7, i);			
		}
		saper.setExit(13,1);

		startMission(1, 1);
	}	

}
