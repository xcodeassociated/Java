/**
 * Interfejs systemu pozwalajacego na wyliczanie wyrazenia logicznego zapisanego
 * bez uzycia nawiasow. 
 * Wyrazenie logiczne zadawane jest poprzez wywolywanie metod intefejsu w kolejnosci
 * zgodnej z odwrotna notacja polska. Przyklad: 
 * <pre>
 * false true and not false or
 * </pre>
 * Obliczamy:
 * <pre>
 * false true and not false or
 *          false not false or
 *               true false or
 *                        true
 * </pre>
 * czyli zadanie powinno zakonczyc sie wynikiem <tt>true</tt>.<br><br>
 * Powyzszy przyklad realizowany przez wywolanie metod interfejsu moze wygladac nastepujaco:
 * <pre>
 * falsee();
 * truee();
 * and();
 * result(); -&gt; false
 * not();
 * result(); -&gt; true
 * falsee();
 * or();
 * result(); -&gt; true 
 * </pre>
 * Wynik koncowy jest identyczny.
 * <br><br>
 * @author oramus
 *
 * @see <a href="https://pl.wikipedia.org/wiki/Odwrotna_notacja_polska">Odwrotna notacja polska</a>
 */
public interface RPNLogicInterface {
	/**
	 * Operand true. true jest slowem zarezerwowanym przez Java i metoda nie moze
	 * nazywac sie po prostu true.
	 * 
	 * @see <a href="https://pl.wikipedia.org/wiki/Operand">operand</a>
	 */
	void truee();
	
	/**
	 * Operand false. false jest slowem zarezerwowanym przez Java i metoda nie moze
	 * nazywac sie po prostu false.
	 * 
	 * @see <a href="https://pl.wikipedia.org/wiki/Operand">operand</a>
	 */
	void falsee();
	
	/**
	 * Operator koniunkcji logicznej.
	 * 
	 * @see <a href="https://pl.wikipedia.org/wiki/Koniunkcja_(logika)">koniunkcja</a>
	 */
	void and();

	/**
	 * Operator alternatywy logicznej.
	 * 
	 * @see <a href="https://pl.wikipedia.org/wiki/Alternatywa">alternatywa</a>
	 */	
	void or();
	
	/**
	 * Operator negacji logicznej.
	 * 
	 * @see <a href="https://pl.wikipedia.org/wiki/Negacja">negacja</a>
	 */	
	void not();
	
	/**
	 * Operator "glosowania". Operator uzywa 3 operandow, o wyniku operacji decyduje wartosc
	 * logiczna wiekszosci operandow (np.: true,false,true-&gt;true). 
	 */	
	void vote3();
	
	/**
	 * Operator "glosowania". Operator uzywa 5-ciu operandow, o wyniku operacji decyduje wartosc
	 * logiczna wiekszosci operandow (np.: true,false,true,false,false-&gt;false). 
	 */	
	void vote5();
	
	/**
	 * Czysci pamiec systemu przywracajac go do stanu poczatkowego. Ewentualny stan bledu jest rowniez czyszczony.
	 */
	void clear();
	
	/**
	 * Metoda zwraca wynik przeprowadzonych operacji. Jesli w trakcie operacji wystapil blad
	 * zwracany jest null.
	 * 
	 * @return wynik przeprowadzonych operacji logicznych, null - blad
	 */
	Boolean result();
}
