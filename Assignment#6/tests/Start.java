import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Start {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(ObjectExchangeTest.class);
		
		System.out.println( "-------------------------------------------");
	    for (Failure failure : result.getFailures()) {
	      System.out.println("BLAD: " + failure.toString());
	    }

		System.out.println( "-------------------------------------------");
	    System.out.println( "Wykonano      : " + result.getRunCount() + " testow" );
	    System.out.println( "Nie zaliczono : " + result.getFailureCount() + " testow" );
	    
		System.out.println( "-------------------------------------------");
	    if ( result.wasSuccessful() ) {
	    	System.out.println( "Testy zakonczone calkowitym sukcesem" );
	    } else {
	    	System.out.println( "Nie wszystkie testy zostaly zaliczone" );
	    }
	}
}
