import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RPNLogicTest {

	private RPNLogicInterface interfaceToTestedUnit;
	private boolean result;

	@Before
	public void createFinder() {
		interfaceToTestedUnit = new RPNLogic();
	}

	private boolean getResultAndTestNull(boolean nullExpected) {
		Boolean result = null;

		try {
			result = interfaceToTestedUnit.result();
		} catch (Exception e) {
			System.out.println("Result zwraca wyjatek: " + e.toString());
			e.printStackTrace();
			fail("Pojawil sie niespodziewany wyjatek" + e.toString());
		}

		if (nullExpected) {
			if ( result != null ) {
				System.out.println( "Powinien byc null, a metoda result zwraca " + result );
			}
			assertNull("Oczekiwano null, bo wystapil blad", result);
			return true; // tak trzeba bo inaczej blad w return result!
		} else {
			assertNotNull("Null nie moze sie pojawic", result);
		}

		return result;
	}

	private void send(boolean... values) {
		for (boolean v : values) {
			if (v)
				interfaceToTestedUnit.truee();
			else
				interfaceToTestedUnit.falsee();
		}
	}

	@Test(timeout = 1000)
	public void and1() {
		send(false, false);
		interfaceToTestedUnit.and();

		result = getResultAndTestNull(false);

		assertFalse("False & False -> False", result);
	}

	@Test(timeout = 1000)
	public void or1() {
		send(false, false);
		interfaceToTestedUnit.or();

		result = getResultAndTestNull(false);

		assertFalse("False or False -> False", result);
	}
	
	
	@Test(timeout = 1000)
	public void and2() {
		send(false, true);
		interfaceToTestedUnit.and();

		result = getResultAndTestNull(false);

		assertFalse("False & True -> False", result);
	}

	@Test(timeout = 1000)
	public void or2() {
		send(false, true);
		interfaceToTestedUnit.or();

		result = getResultAndTestNull(false);

		assertTrue("False or True -> True", result);
	}
	
	
	@Test(timeout = 1000)
	public void and3() {
		send(true, false );
		interfaceToTestedUnit.and();

		result = getResultAndTestNull(false);

		assertFalse("True & False -> False", result);
	}

	@Test(timeout = 1000)
	public void or3() {
		send(true, false);
		interfaceToTestedUnit.or();

		result = getResultAndTestNull(false);

		assertTrue("True or False -> True", result);
	}
	
	@Test(timeout = 1000)
	public void and4() {
		send(true, true );
		interfaceToTestedUnit.and();

		result = getResultAndTestNull(false);

		assertTrue("True & True -> True", result);
	}

	@Test(timeout = 1000)
	public void or4() {
		send(true, true);
		interfaceToTestedUnit.or();

		result = getResultAndTestNull(false);

		assertTrue("True or True -> True", result);
	}
	
	
	@Test(timeout = 1000)
	public void notF() {
		send(false);
		interfaceToTestedUnit.not();

		result = getResultAndTestNull(false);

		assertTrue("! False -> True", result);
	}

	@Test(timeout = 1000)
	public void notT() {
		send(true);
		interfaceToTestedUnit.not();

		result = getResultAndTestNull(false);

		assertFalse("! True -> False", result);
	}
	
	@Test(timeout = 1000)
	public void vote3_1() {
		send(true,true,true);
		interfaceToTestedUnit.vote3();

		result = getResultAndTestNull(false);

		assertTrue("3xTrue vote -> True", result);
	}	

	@Test(timeout = 1000)
	public void vote3_2() {
		send(true,false,true);
		interfaceToTestedUnit.vote3();

		result = getResultAndTestNull(false);

		assertTrue("2xTrue,False vote -> True", result);
	}	
	
	@Test(timeout = 1000)
	public void vote3_3() {
		send(false,false,true);
		interfaceToTestedUnit.vote3();

		result = getResultAndTestNull(false);

		assertFalse("1xTrue,2xFalse vote -> False", result);
	}	

	@Test(timeout = 1000)
	public void vote3_4() {
		send(false,false,false);
		interfaceToTestedUnit.vote3();

		result = getResultAndTestNull(false);

		assertFalse("3xFalse vote -> False", result);
	}	
	
	
	@Test(timeout = 1000)
	public void vote5_1() {
		send(true,true,true,true,true);
		interfaceToTestedUnit.vote5();

		result = getResultAndTestNull(false);

		assertTrue("5xTrue vote -> True", result);
	}
	
	@Test(timeout = 1000)
	public void vote5_2() {
		send(true,false,true,true,true);
		interfaceToTestedUnit.vote5();

		result = getResultAndTestNull(false);

		assertTrue("4xTrue, 1xFalse vote -> True", result);
	}
	
	@Test(timeout = 1000)
	public void vote5_3() {
		send(true,false,true,false,true);
		interfaceToTestedUnit.vote5();

		result = getResultAndTestNull(false);

		assertTrue("3xTrue, 2xFalse vote -> True", result);
	}

	@Test(timeout = 1000)
	public void vote5_4() {
		send(true,false,false,false,true);
		interfaceToTestedUnit.vote5();

		result = getResultAndTestNull(false);

		assertFalse("2xTrue, 3xFalse vote -> False", result);
	}

	@Test(timeout = 1000)
	public void vote5_5() {
		send(false,false,false,false,true);
		interfaceToTestedUnit.vote5();

		result = getResultAndTestNull(false);

		assertFalse("1xTrue, 4xFalse vote -> False", result);
	}

	@Test(timeout = 1000)
	public void vote5_6() {
		send(false,false,false,false,false);
		interfaceToTestedUnit.vote5();

		result = getResultAndTestNull(false);

		assertFalse("0xTrue, 5xFalse vote -> False", result);
	}

	@Test(timeout = 1000)
	public void and_E() {
		send(false);
		interfaceToTestedUnit.and();

		getResultAndTestNull(true);
	}

	@Test(timeout = 1000)
	public void or_E() {
		send(false);
		interfaceToTestedUnit.or();

		getResultAndTestNull(true);
	}
	
	@Test(timeout = 1000)
	public void not_E() {
		interfaceToTestedUnit.not();

		getResultAndTestNull(true);
	}	
	
	@Test(timeout = 1000)
	public void vote3_E() {
		send(false,false);
		interfaceToTestedUnit.vote3();

		getResultAndTestNull(true);
	}

	@Test(timeout = 1000)
	public void vote5_E() {
		send(false,false,false,false);
		interfaceToTestedUnit.vote5();

		getResultAndTestNull(true);
	}
	
	@Test(timeout = 1000)
	public void complex1() {
		send(false,false,true,false);
		interfaceToTestedUnit.or();
		interfaceToTestedUnit.or();
		interfaceToTestedUnit.or();

		result = getResultAndTestNull(false);
		
		assertTrue( "F F T F or or or -> T", result );
	}
	
	@Test(timeout = 1000)
	public void complex2() {
		send(false,false,true,false,false);
		interfaceToTestedUnit.not();
		interfaceToTestedUnit.or();
		interfaceToTestedUnit.not();
		interfaceToTestedUnit.or();
		interfaceToTestedUnit.and();
		interfaceToTestedUnit.not();
		interfaceToTestedUnit.or();

		// F F T F F not or not or -> F F T F T or not or -> F F T T not or -> F F T F or -> F F T
		// F F T and not or -> F F not or -> F T or -> T
		
		result = getResultAndTestNull(false);
		
		assertTrue( "F F T F F not or not or and not or -> T", result );
	}
	
	@Test(timeout = 1000)
	public void complex3() {
		send(false,false,true,false,false);
		interfaceToTestedUnit.not();
		interfaceToTestedUnit.or();
		interfaceToTestedUnit.not();
		interfaceToTestedUnit.or();
		interfaceToTestedUnit.and();
		interfaceToTestedUnit.not();
		interfaceToTestedUnit.or();
		send(false);
		interfaceToTestedUnit.and();

		result = getResultAndTestNull(false);
		
		assertFalse( "F F T F F not or not or and not or F and -> F", result );
	}
	
	@Test(timeout = 1000)
	public void clear1() {
		send(false);
		interfaceToTestedUnit.clear();
		send(true,true);
		interfaceToTestedUnit.and();

		result = getResultAndTestNull(false);
		
		assertTrue( "T T and -> T", result );

		interfaceToTestedUnit.and();
		getResultAndTestNull(true); // teraz powinien byc blad
	}

	@Test(timeout = 1000)
	public void clearE() {
		send(false);
		interfaceToTestedUnit.and();
		getResultAndTestNull(true); // teraz powinien byc blad
		interfaceToTestedUnit.clear();
		send(true,true);
		interfaceToTestedUnit.and();

		result = getResultAndTestNull(false); // bledu byc juz nie powinno
		
		assertTrue( "T T and -> T", result );
	}
}
