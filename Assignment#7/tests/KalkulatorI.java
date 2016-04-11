/**
 * Interfejs bardzo prostego kalkulatora uzywajacego dwoch akumulatorow
 * do przechowywania wynikow obliczen.
 * 
 * @author oramus
 *
 */
public interface KalkulatorI {

	/**
	 * Interfejs zagniezdzony akumulatora przechowujacego jedna wartosc typu
	 * int. Poczatkowa zawartosc kalkulatora to 0.
	 */
	public static interface AccumulatorI {
		/**
		 * Ustawia wartosc w kalkulatorze.
		 * 
		 * @param acc
		 *            - nowa wartosc zapamietywana przez kalkulator
		 */
		public void setValue(int acc);

		/**
		 * Zwraca wartosc zapamietana przez kalkulator.
		 * 
		 * @return watosc zapamiatywana przez kalkulator.
		 */
		public int getValue();
	}

	/**
	 * Instaluje w kalkulatorze akumulator A, ktory sluzy potem do wykonywania
	 * obliczen
	 * 
	 * @param a
	 *            obiekt do przechowywania wynikow
	 */
	void setAccumulatorA(AccumulatorI a);

	/**
	 * Instaluje w kalkulatorze akumulator B
	 * 
	 * @param a
	 *            obiekt do przechowywania wynikow
	 */
	void setAccumulatorB(AccumulatorI a);

	/**
	 * Akumulator A i B zamieniane ze soba miejscami. Akumulator
	 * A staje sie B, a B dziala jako A.
	 */
	void swapAccumulators();

	/**
	 * Zwraca zawartosc akumulator-a A lub Integer.MIN_VALUE jesli go brak
	 * 
	 * @return zwraca zawartosc akumulatora A
	 */
	int getA();

	/**
	 * Dodaje argument do akumulatora A, o ile ten zostal ustawiony i zwraca
	 * aktualna zawartosc akumulatora A.
	 * 
	 * @param i liczba, ktora nalezy dodac do zawartosci kalkulatora
	 * @return zaktualizowana zawartosc akumulatora A lub Integer.MIN_VALUE
	 *         jesli brak akumulatora
	 */
	int addA(int i);

	/**
	 * Odejmuje argument od akumulatora A, o ile zostal ustawiony i zwraca
	 * aktualna zawartosc akumulatora A.
	 * 
	 * @param i liczba, ktora nalezy odjac do zawartosci kalkulatora
	 * @return zaktualizowana zawartosc akumulatora A lub Integer.MIN_VALUE
	 *         jesli brak akumulatora
	 */
	int subA(int i);

	/** Zeruje akumulator A, jesli jest ustawiony */
	void zeroA();
}
