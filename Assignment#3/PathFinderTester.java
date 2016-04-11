
public class PathFinderTester {
	
	public static final boolean INC = true;
	public static final boolean DEC = !INC;
	
	public static LabirynthGenerator lab1() {
		LabirynthGenerator lb = new LabirynthGenerator( 4, 5, 3 );
		lb.setStartPosition( new IndicesExt( 0, 0, 0));
		lb.moveX( 2,  INC );
		lb.moveY( 2,  INC  );
		lb.moveX( 2,  DEC );
		lb.moveZ( 2,  INC );
		
		Indices pos = lb.getCurrentPosition();
		
		lb.moveX( 2, INC );

		lb.setCurrentPosition( pos );
		
		lb.moveY( 2, INC );

		lb.setFinish( lb.getCurrentPosition() );
				
		lb.setPassable( true );
		return lb;
	}
	
	public static LabirynthGenerator reverse( LabirynthGenerator lb ) {
		Indices pos = lb.getFinish();
		lb.setFinish( lb.getStart() );
		lb.setStartPosition( pos );
		
		return lb;
	}
	
	public static LabirynthGenerator lab2() {
		LabirynthGenerator lb = new LabirynthGenerator( 5, 6, 5 );
		lb.setStartPosition( new IndicesExt( 0, 0, 0));
		lb.moveX( 2,  INC );
		lb.moveY( 2,  INC  );
		
		Indices pos = lb.getCurrentPosition();
		
		lb.moveX( 2,  DEC );
		lb.moveZ( 4,  INC );
				
		lb.moveX( 4, INC );

		lb.moveZ( 4, DEC );
		lb.moveY( 2,  INC );

		
		lb.setFinish( lb.getCurrentPosition() );

		lb.setCurrentPosition( pos );
		lb.moveZ( 2,  INC );
		lb.moveY( 2,  DEC );
		
		
		
		lb.setPassable( true );
		return lb;
	}	
	
	public static LabirynthGenerator lab3() {
		LabirynthGenerator lb = new LabirynthGenerator( 5,5,7 );
		lb.setStartPosition( new IndicesExt( 2, 0, 0));
		lb.moveY( 2,  INC );
		Indices pos = lb.getCurrentPosition();

		lb.moveX( 1,  DEC  );
		lb.moveY( 2,  INC );

		lb.setCurrentPosition( pos );
		
		lb.moveX( 2,  INC );
		lb.moveY( 2,  INC );
				
		lb.moveZ( 2, INC );
		lb.moveX( 2,  DEC );
		lb.moveY( 3,  DEC );
		lb.moveX( 2,  DEC );
		lb.moveY( 1,  DEC );
		lb.moveZ( 2,  INC );
		
		lb.moveX( 1,  INC );
		lb.moveY( 3,  INC );
		lb.moveX( 3,  INC );

		pos = lb.getCurrentPosition();
		lb.moveY( 2,  DEC );
		lb.setCurrentPosition( pos );
		lb.moveY( 1,  INC );
		lb.moveZ( 2,  INC );		

		pos = lb.getCurrentPosition();
		
		lb.moveX( 4, DEC );
		
		lb.setCurrentPosition( pos );
		
		lb.moveY( 4, DEC );
		lb.setFinish( lb.getCurrentPosition() );		
		
		lb.setPassable( true );
		return lb;
	}	

	public static LabirynthGenerator lab4() {
		LabirynthGenerator lb = new LabirynthGenerator( 5,5,7 );
		lb.setStartPosition( new IndicesExt( 2, 0, 0));
		lb.moveY( 2,  INC );
		Indices pos = lb.getCurrentPosition();

		lb.moveX( 1,  DEC  );
		lb.moveY( 2,  INC );

		lb.setCurrentPosition( pos );
		
		lb.moveX( 2,  INC );
		lb.moveY( 2,  INC );
				
		lb.moveZ( 2, INC );
		lb.moveX( 2,  DEC );
		lb.moveY( 3,  DEC );
		lb.moveX( 2,  DEC );
		lb.moveY( 1,  DEC );
		lb.moveZ( 2,  INC );
		
		lb.moveX( 1,  INC );
		lb.moveY( 3,  INC );
		lb.moveX( 3,  INC );

		pos = lb.getCurrentPosition();
		lb.moveY( 2,  DEC );
		lb.setCurrentPosition( pos );
		lb.moveY( 1,  INC );
		lb.moveZ( 2,  INC );		

		pos = lb.getCurrentPosition();
		
		lb.moveX( 4, DEC );
		
		lb.setCurrentPosition( new IndicesExt( pos.firstIndex, pos.secondIndex-1, pos.thirdIndex ) );
		
		lb.offSecurity();
		lb.moveY( 3, DEC );
		lb.setFinish( lb.getCurrentPosition() );		
		
		lb.setPassable( false );
		return lb;
	}	
	
	private static boolean test( LabirynthGenerator lg ) {
		System.out.println( lg );   // tu jest prezentacja labiryntu
		PathFinder pf = new PathFinder();
		boolean result = false;
		try {
			result = pf.find(lg.getLabirynth(), lg.getStart(), lg.getFinish()) == lg.isPassable();
		} catch (Exception e ) {
			System.out.println( e );
			System.out.println( "Doszlo do wyjatku!");
		}
		
		if ( ! result ) {
			System.out.println( "Test zakonczony porazka" );
		}
		
		return result;
	}
	
	private static boolean testAndReverse( LabirynthGenerator lg, LabirynthGenerator lg2 ) {
		return test( lg ) & test( reverse( lg2 ));
	}
	
	private static boolean testWrongParam() {
		PathFinder pf = new PathFinder();

		int counter = 0;
		
		try {
			if ( ! pf.find( lab1().getLabirynth(), null, null) ) {
				System.out.println( "PathFinder testowany na dzialanie null");
				counter++;
			} else {
				System.out.println( "PathFinder nieuodporniony na dzialanie null");
			}
		} catch ( Exception e ) {
			System.out.println( e );
			System.out.println( "PathFinder nieuodporniony na dzialanie null");
		}

		try {
			if ( ! pf.find(lab1().getLabirynth(), new IndicesExt( -1,-1,-1 ), new IndicesExt( 0,0,0 )) ) {
				System.out.println( "PathFinder testowany na dzialanie -1");
				counter++;
			} else {
				System.out.println( "PathFinder nieuodporniony na dzialanie -1");
			}
		} catch ( Exception e ) {
			System.out.println( e );
		}
	
				
		try {
			if ( ! pf.find(lab1().getLabirynth(), new IndicesExt( 11,11,11 ), new IndicesExt( 202,220,220 )) ) {
				System.out.println( "PathFinder testowany na dzialanie indeksow spoza tablicy");
				counter++;
			} else {
				System.out.println( "PathFinder nieuodporniony na dzialanie indeksow spoza tablicy");
			}
		} catch ( Exception e ) {
			System.out.println( e );
		}
		
		if ( counter != 3 ) System.out.println( "Zaliczono " + counter + " testow na 3 zapodane");
		
		return ( counter == 3);
	}
	
	
	public static void main(String[] args) {
		
		Thread th = new Thread( new Runnable() {
			
			@Override
			public void run() {
				System.out.println( "Start testow");
				
				int counter = 0;
				
				if ( testAndReverse( lab1(), lab1() ) ) {
					counter++;
				} 
				if ( testAndReverse( lab2(), lab2() ) )  {
					counter++;
				} 
				if ( testAndReverse( lab3(), lab3() ) ) {
					counter++;
				} 
				if ( testAndReverse( lab4(), lab4() ) ) {
					counter++;
				} 
				if ( counter == 4 ) {
					System.out.println( "!!! Udalo sie wykonac testy labiryntu bez bledu");
					if ( testWrongParam() ) {
						System.out.println( "!!! Udalo sie wykonac testy blednych parametow");
					} else {
						System.out.println( "Problem z obsluga blednych parametrow...");
					}
				} else {
					System.out.println( "Oj, cos tu jest nie tak... zaliczono " + counter + "/4 testow podstawowych" );
				}
				
			}
		} );
		th.setDaemon( true );
		th.start();
		
		try {
			th.join( 5000 );			
		} catch ( InterruptedException ie ) {}
		
	}
}
