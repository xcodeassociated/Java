import java.util.Random;

public class PMO_WyjatkowySaper implements WyjatkowySaperGameInterface {
	private final int LICZBA_SAPEROW;
	private final int ROZMIAR_PLANSZY;
	private final boolean[][] board;
	private boolean aktywnySaper = true;
	private int licznikSaperow;
	private int wyjscieIndex1;
	private int wyjscieIndex2;
	private boolean firstCall = true;
	private int liczbaProb;
	private boolean missionAccomplished;
	private final boolean[][] boardVisited;
	private boolean error;

	public PMO_WyjatkowySaper(int ls, int size) {
		LICZBA_SAPEROW = ls;
		ROZMIAR_PLANSZY = size;
		board = new boolean[size][size];
		boardVisited = new boolean[size][size];
		licznikSaperow = 0;
		liczbaProb = 1 + (int) (Math.log(ROZMIAR_PLANSZY) / Math.log(2));
	}

	@Override
	public void nastepnySaper() throws NieMaJuzNikogo {
		if (licznikSaperow == LICZBA_SAPEROW) {
	
			PMO_SystemOutRedirect.println("	   ____                       ___");
			PMO_SystemOutRedirect.println("	  / ___| __ _ _ __ ___   ___ / _ \\__   _____ _ __ ");
			PMO_SystemOutRedirect.println("	 | |  _ / _` | '_ ` _ \\ / _ \\ | | \\ \\ / / _ \\ \'__|");
			PMO_SystemOutRedirect.println("	 | |_| | (_| | | | | | |  __/ |_| |\\ V /  __/ |   ");
			PMO_SystemOutRedirect.println("	  \\____|\\__,_|_| |_| |_|\\___|\\___/  \\_/ \\___|_|   ");
			throw new NieMaJuzNikogo();
		}
	
		licznikSaperow++;
		aktywnySaper = true;
	}

	@Override
	public void sprawdzam(int index1, int index2) throws UwagaMiny, Bum,
			ToJestWyjscie {
		
		if (missionAccomplished) {
			PMO_SystemOutRedirect.println( "BLAD: Wykonywane sa dzialania poszukiwawcze, a wyjscie juz zostalo wczesniej odszukane" );
			error = true;
		}
		
		saperDostepny();
		testWyjsciaPozaPlansze(index1, index2);
	
		boardVisited[index1][index2] = true;
		
		if (board[index1][index2]) {
			board[index1][index2]=false; // dezaktywacja miny!
			boom();
		}
	
		if (wyjscie(index1, index2)) {
	
			PMO_SystemOutRedirect.println("			__        ___                       _ ");
			PMO_SystemOutRedirect.println("			\\ \\      / (_)_ __  _ __   ___ _ __| | ");
			PMO_SystemOutRedirect.println("			 \\ \\ /\\ / /| | \'_ \\| \'_ \\ / _ \\ \'__| | ");
			PMO_SystemOutRedirect.println("			  \\ V  V / | | | | | | | |  __/ |  |_|");
			PMO_SystemOutRedirect.println("			   \\_/\\_/  |_|_| |_|_| |_|\\___|_|  (_)");
	
			missionAccomplished = true;
			
			throw new ToJestWyjscie();
		}
	
		int count = countMines(index1, index2);
	
		if (count == 0)
			return;
	
		throw new UwagaMiny(count);
	}

	@Override
	public void rozmiarPlanszyDoGry(int propozycja) throws ZaDuzy, ZaMaly,
			PoczatkowaLiczbaProb {
	
		if (firstCall) {
			firstCall = false;
			throw new PoczatkowaLiczbaProb(liczbaProb);
		} else {
			if (liczbaProb > 0) {
	
				liczbaProb--;
	
				if (propozycja > ROZMIAR_PLANSZY)
					throw new ZaDuzy();
				if (propozycja < ROZMIAR_PLANSZY)
					throw new ZaMaly();
				return;
			} else {
				if ((new Random()).nextBoolean()) {
					throw new ZaDuzy();
				} else {
					throw new ZaMaly();
				}
			}
		}
	}

	protected void show( int si1, int si2 ) {
		for (int i1 = ROZMIAR_PLANSZY - 1; i1 >= 0; i1--) {
			PMO_SystemOutRedirect.print("| ");
			for (int i2 = 0; i2 < ROZMIAR_PLANSZY; i2++) {
				if ( ( si1 == i1 ) && ( si2 == i2 ) ) {
					PMO_SystemOutRedirect.print( " S");
				} else if (wyjscie(i1, i2))
					PMO_SystemOutRedirect.print(" E");
				else if ( boardVisited[i1][i2] ) {
					PMO_SystemOutRedirect.print( " o");
				}
				else if (board[i1][i2]) {
					PMO_SystemOutRedirect.print(" *");
				} else {
					PMO_SystemOutRedirect.print(" .");
				}
			}
			PMO_SystemOutRedirect.println(" |");
		}
	}

	protected void setMine(int i, int j) {
		board[j][i] = true;
	}

	protected void setExit(int i2, int i1) {
		wyjscieIndex1 = i1;
		wyjscieIndex2 = i2;
	}

	/*
	 * Zlicza miny bez uwzglednienia min poza plansza!
	 */
	protected int countMines(int i, int j) {
		int count = 0;
	
		count += mine2int(i - 1, j - 1);
		count += mine2int(i, j - 1);
		count += mine2int(i + 1, j - 1);
	
		count += mine2int(i - 1, j + 1);
		count += mine2int(i, j + 1);
		count += mine2int(i + 1, j + 1);
	
		count += mine2int(i - 1, j);
		count += mine2int(i + 1, j);
	
		return count;
	}

	protected boolean startCorrect( int i1, int i2 ) {
		if ( ( countMines( i1, i2 ) == 0 ) && ( ! board[ i1 ][ i2 ] ) ) {
			return true;
		} else 
			return false;
	}

	protected boolean getMissionAccomplishedFlag() {
		return missionAccomplished;
	}
	
	protected boolean getErrorFlag() {
		return error;
	}

	private int mine2int(int i, int j) {
		try {
			if (board[i][j])
				return 1;
		} catch (Exception e) { // obsluga arrayOutOfBoundsException
		}
		return 0;
	}

	private void boom() throws Bum {
		aktywnySaper = false;
		throw new Bum();
	}

	private void testWyjsciaPozaPlansze(int i) throws Bum {
		if ((i < 0) || (i >= ROZMIAR_PLANSZY)) {
			boom();
		}
	}

	private void testWyjsciaPozaPlansze(int i1, int i2) throws Bum {
		testWyjsciaPozaPlansze(i1);
		testWyjsciaPozaPlansze(i2);
	}

	private void saperDostepny() {
		if (!aktywnySaper)
			throw new RuntimeException("Brak sapera.");
	}

	private boolean wyjscie(int i1, int i2) {
		if ((i1 == wyjscieIndex1) && (i2 == wyjscieIndex2)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws NieMaJuzNikogo {
		PMO_WyjatkowySaper saper = new PMO_WyjatkowySaper(3, 10);

	//	saper.setMine(i, j);

		saper.setExit(2,2);
		
		saper.show(1, 1);
	}

}
 