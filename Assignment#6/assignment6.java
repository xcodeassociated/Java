//package com.javaclasses;
import java.util.*;

/**
 * Super wazny interfejs systemu przechowywania obiektow. Tak naprawde to zadanie, na zabawe
 * ze zbiorami, listami, mapami i sortowaniem.
 *
 * @author Piotr Marek Oramus &copy;
 */
 interface ObjectExchangeInterface {
    
    /**
     * Metoda rejestruje uzytkownika w systemie. Poprawne nazwy uzytkownikow
     * musza byc unikalne czyli nie moga sie powtarzac.
     *
     * @param username
     *            nazwa uzytkownika
     * @return true - rejestracja przebiegla prawidlowo, false - uzytkownik o
     *         podanym username juz jest zarejestrowany.
     */
    boolean registerUser(String username);
    
    /**
     * Zwraca zbior zarejestrowanych nazw uzytkownika
     *
     * @return Zbior nazw zarejestrowanych uzytkownikow, null - brak
     *         uzytkownikow systemu
     */
    Set<String> getRegisteredUsers();
    
    /**
     * Usuniecie uzytkownika z systemu. Wszystkie obiekty uzytkownika takze sa z
     * systemu usuwane.
     *
     * @param username
     *            nazwa uzytkownika, ktory chce zakonczyc prace z systemem
     * @return true - usunieto uzytkownika, false - uzytkownika nie bylo w
     *         systemie
     */
    boolean deregisterUser(String username);
    
    /**
     * Metoda dodaje do systemu obiekt. Obiekt jest wiazany z nazwa uzytkownika.
     * Dzieki <code>extends</code> dodawane obiekty <em>musza</em>
     * byc zgodne z interfesjem <code>ValueI</code>
     * Obiekty moga sie wielokrotnie w systemie powtarzac. Nawet jeden
     * uzytkownik moze posiadac kilka takich samych obiektow.
     *
     * @param username
     *            nazwa uzytkownika-wlasciciela obiektu
     * @param obj
     *            obiekt do przekazania do systemu (tak, wiem, tak naprawde
     *            system dostaje kopie referencji, ale jakos latwiej pisac, ze
     *            dostaje obiekt).
     * @return true - obiekt przyjeto do przechowania, false - bledna nazwa
     *         uzytkownika, lub przyslano zamiast obiektu null
     */
    <T extends ValueI> boolean addObjectToSystem(String username, T obj);
    
    /**
     * Metoda zwraca posortowana <em>malejaco</em> liste umieszczonych w
     * systemie obiektow.
     *
     * @param username
     *            nazwa wlasciciela obiektow
     * @return lista posortowanych obiektow, null jesli nazwa uzytkownika nie
     *         jest poprawna, albo gdy ten nie posiada w systemie zadanych
     *         swoich obiektow
     */
    List<ValueI> getSortedListOfObjects(String username);
    
    /**
     * Metoda zwraca posortowana <em>malejaco</em> mape, w ktorej kluczami sa
     * referencje do obiektow, zas wartosciami liczba ich powtorzen tego obiektu
     * na liscie obiektow nalezacych do zadanego uzytkownika.
     *
     * @param username
     *            nazwa uzytkownika systemu
     * @return mapa krotnosci wystepowania obiektow. Im wiecej powtorzen obiektu
     *         tym jest on wczesniej do odnalezienia w przeslanej mapie.
     *         <tt>null</tt> w przypadku podania blednej nazwy uzytkownika
     *         lub braku obiektow dla wskazanego uzytkownika.
     */
    Map<ValueI, Integer> getSortedMap(String username);
}

/* -------------------------------------------------------------------------------------------------*/
/* -------------------------------------------------------------------------------------------------*/
/* -------------------------------------------------------------------------------------------------*/


/**
 * Interfejs pozwalajacy na poznanie pewnej wartosci. To wg. niej moga byc
 * sortowane obiekty, ktore system bedzie przechowywac.
 */
 interface ValueI {
    /**
     * Zwraca wartosc liczbowa przypisana do obiektu.
     *
     * @return wartosc liczbowas
     */
    float getValue();
}

/* -------------------------------------------------------------------------------------------------*/
/* -------------------------------------------------------------------------------------------------*/
/* -------------------------------------------------------------------------------------------------*/

class ObjectExchange implements ObjectExchangeInterface {
    
    private Set<String> users = null;
    private Map<String, List<ValueI>> objects = null;
    
    private boolean isThisUserAlredyExists(String _username){
        return (this.users.contains(_username) == true) ? true : false;
    }
    
    private void addUser(String _username){
        if (_username != null)
            this.users.add(_username);
    }
    
    private static void printMap(Map<Float, Integer> map) {
        for (Map.Entry<Float, Integer> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());
        }
    }
    
    private Map<Float, Integer> sortByComparator(Map<Float, Integer> unsortMap) {
        
        List<Map.Entry<Float, Integer>> list = new LinkedList<Map.Entry<Float, Integer>>(unsortMap.entrySet());
        
        Collections.sort(list, new Comparator<Map.Entry<Float, Integer>>() {
            public int compare(Map.Entry<Float, Integer> o1, Map.Entry<Float, Integer> o2) {
                return (-1)*(o1.getValue()).compareTo(o2.getValue());
            }
        });
        
        Map<Float, Integer> sortedMap = new LinkedHashMap<Float, Integer>();
        for (Iterator<Map.Entry<Float, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Float, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    
    private Map<ValueI, Integer> remakeMap(Map<Float, Integer> map){
        Map<ValueI, Integer> r = new LinkedHashMap<>();
        
        for (Map.Entry<Float, Integer> entry : map.entrySet()) {
            r.put( new Value(entry.getKey().floatValue()), new Integer(entry.getValue().intValue()) );
        }
        
        return r;
    }
    
    {
        //non-static code block
    }
    
    public ObjectExchange() {
        this.users = new TreeSet<>();
        this.objects = new HashMap<>();
    }
    
    public boolean registerUser(String username){
        if (username != null  && username.length() > 0){
            if (!this.isThisUserAlredyExists(username)){
                this.addUser(username);
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public Set<String> getRegisteredUsers(){
        return (this.users.size() > 0) ? this.users : null;
    }
    
    public boolean deregisterUser(String username){
        if (username != null && username.length() > 0){
            if (this.isThisUserAlredyExists(username)){
                this.users.remove(username);
                this.objects.remove(username);
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public <T extends ValueI> boolean addObjectToSystem(String username, T obj){
        if (username == null || obj == null || !this.isThisUserAlredyExists(username) || username.length() == 0)
            return false;
        else{
            List<ValueI> temp = this.objects.get(username);
            if (temp != null){
                temp.add(obj);
                this.objects.replace(username, temp);
            }else{
                List<ValueI> t = new ArrayList<ValueI>();
                t.add(obj);
                this.objects.put(username, t);
            }
            return true;
        }
    }
    
    public List<ValueI> getSortedListOfObjects(String username){
        if (username == null || username.length() == 0 || !this.isThisUserAlredyExists(username))
            return null;
        else{
            List<ValueI> list = new ArrayList<>();
            
            for (Map.Entry<String, List<ValueI>> entry: this.objects.entrySet()) {
                String key = entry.getKey();
                
                if (key == username){
                    List<ValueI> val = entry.getValue();
                    list = val;
                }
            }
            
            if (list.size() == 0)
                return null;
            
            list.sort( new Comparator<ValueI>(){
                public int compare(ValueI o1, ValueI o2) {
                    return (-1)*( new Float(o1.getValue()).compareTo(new Float(o2.getValue() )) );
                }
            });
            
            return list;
        }
    }
    
    public Map<ValueI, Integer> getSortedMap(String username){
        if (username == null || username.length() == 0 || !this.isThisUserAlredyExists(username))
            return null;
        else{
            HashMap<Float, Integer> temp = new HashMap<>();
            List<ValueI> t_values = new ArrayList<>();
            
            for (Map.Entry<String, List<ValueI>> entry: this.objects.entrySet()) {
                String key = entry.getKey();
                
                if (key == username)
                    t_values = entry.getValue();
            }
            
            if (t_values.size() == 0) 
                return null;
            
            ArrayList<Float> t = new ArrayList<>();
            for (int i = 0; i < t_values.size(); i++){
                if (t.contains(new Float(t_values.get(i).getValue())))
                    ;
                else
                    t.add(t_values.get(i).getValue());
            }
            
            for (int i = 0; i < t.size(); i++){
                int count = 0;
                for (int j = 0; j < t_values.size(); j++){
                    if (t_values.get(j).getValue() == t.get(i).floatValue())
                        count++;
                }
                //System.out.println(t.get(i).floatValue() + " " + count);
                temp.put(t.get(i), new Integer(count));
            }
            
            Map<Float, Integer> t2 = this.sortByComparator(temp);
            //printMap(t2);
            
            return this.remakeMap(t2);
        }
    }
    
}


/* -------------------------------------------------------------------------------------------------*/
/* -------------------------------------------------------------------------------------------------*/
/* -------------------------------------------------------------------------------------------------*/

class Value implements ValueI{
    private float val = 0.0f;
    
    public Value(){
        
    }
    
    public Value(float _val){
        this.val = _val;
    }
    
    public void setValue(float _val){
        this.val = _val;
    }
    
    public float getValue(){
        return this.val;
    }
    
    public String toString(){
        return String.valueOf(this.val);
    }
    
}

/* -------------------------------------------------------------------------------------------------*/
/* -------------------------------------------------------------------------------------------------*/
/* -------------------------------------------------------------------------------------------------*/


class Start {

	public static void main(String[] args) {
		ObjectExchangeInterface obi = new ObjectExchange();
		
		String user1 = "janusz", user2 = "kaja";
		
		System.out.println("\nRegister the users:");
		System.out.println(obi.registerUser(user1)); //return: true
		System.out.println(obi.registerUser(user2)); //return: true
		
		//true to add 'user1' again..
		System.out.println(obi.registerUser(user1) + " - user reg. duplicated! (should be false)"); //return: flase
		
		obi.addObjectToSystem(user1, new Value(1));
		obi.addObjectToSystem(user1, new Value(1));
		obi.addObjectToSystem(user1, new Value(1));
		obi.addObjectToSystem(user1, new Value(2));
		obi.addObjectToSystem(user1, new Value(3));
		obi.addObjectToSystem(user1, new Value(3));
	
		obi.addObjectToSystem(user2, new Value(1));
		obi.addObjectToSystem(user2, new Value(4));
		obi.addObjectToSystem(user2, new Value(4));
		
		System.out.println("\nIncorrect user object adding:");
		System.out.println(obi.addObjectToSystem("Mietek_co_go_w_systemie_nie_ma", new Value(4))); //return: false
		System.out.println(obi.addObjectToSystem(null, new Value(4))); //return: false
		System.out.println(obi.addObjectToSystem(user1,  null)); //return: false
		
		System.out.println("\nRegistered users:");
		Set<String> reg_users = obi.getRegisteredUsers();
		for (String e: reg_users){
			System.out.println("	user: " + e);
		}
		
		System.out.println("\nStored list of objects:");
		List<ValueI> lob1 = obi.getSortedListOfObjects(user1);
		for (ValueI v: lob1)
			System.out.println("   " + user1 + " : " + v.getValue());
		
		List<ValueI> lob2 = obi.getSortedListOfObjects(user2);
		for (ValueI v: lob2)
			System.out.println("   " + user2 + " : " + v.getValue());
		
		System.out.println("\nUser1 map:");
		Map<ValueI, Integer> smo1 = obi.getSortedMap(user1);
		System.out.println(smo1);

		System.out.println("\nUser2 map:");
		Map<ValueI, Integer> som2 = obi.getSortedMap(user2);
		System.out.println(som2);
		
		System.out.println("\nUnknow user map (should be null):");
		System.out.println(obi.getSortedMap("Mietek_co_go_w_systemie_nie_ma")); // return: null

		System.out.println("\nDeregister:");
		System.out.println(obi.deregisterUser(user1)); //return: true
		System.out.println(obi.deregisterUser(user2)); //return: true
		System.out.println(obi.deregisterUser("Mietek_co_go_w_systemie_nie_ma")); // return: false
		
		System.out.println("\nOther Tests:");
		if (obi.getRegisteredUsers() == null)
			System.out.println("ok!");
		
		if (obi.addObjectToSystem(user1, new Value(8)) == false)
			System.out.println("ok!");
		
		if (obi.deregisterUser(user1) == false)
			System.out.println("ok!");

	}

}
