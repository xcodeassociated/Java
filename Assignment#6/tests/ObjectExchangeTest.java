import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ObjectExchangeTest {

	private ObjectExchangeInterface oei;

	@Before
	public void createExchangeSystem() {
		oei = new ObjectExchange();
	}

	private void addUserAndTest(String user) {

		int users;
		if (oei.getRegisteredUsers() != null) {
			users = oei.getRegisteredUsers().size();
		} else {
			users = 0; // zakladamy, ze null -> brak uzytkownikow
		}
		boolean res = oei.registerUser(user);

		assertTrue(
				"Dodanie uzytkownika o unikalnym username powinno zwrocic true",
				res);

		assertTrue("Po dodaniu uzytkownika powinien on byc na liscie", oei
				.getRegisteredUsers().contains(user));
		assertEquals(
				"Dodanie unikalnego uzytkownika powinien oznaczac powiekszenie zbioru",
				users + 1, oei.getRegisteredUsers().size());
	}

	private void addNonUniqUser(String user) {
		int users;
		if (oei.getRegisteredUsers() != null) {
			users = oei.getRegisteredUsers().size();
		} else {
			users = 0; // zakladamy, ze null -> brak uzytkownikow
		}
		boolean res = oei.registerUser(user);
		assertFalse(
				"Dodanie uzytkownika o nieunikalnym username powinno zwrocic false",
				res);
		assertEquals(
				"Ponowne dodanie tego samego uzytkownika nie powininno powiekszac zbioru",
				users, oei.getRegisteredUsers().size());
	}

	private void removeUser(String user) {
		int users;
		users = oei.getRegisteredUsers().size();

		boolean res = oei.deregisterUser(user);

		assertTrue("Usuniecie istniejacego uzytkownika powinno zwrocic true",
				res);

		if (users > 1) {
			assertFalse(
					"Po usunieciu uzytkownika nie powinien on juz byc na liscie",
					oei.getRegisteredUsers().contains(user));
			assertEquals(
					"Usuniecie unikalnego uzytkownika powinno oznaczac zmniejszenie zbioru",
					users - 1, oei.getRegisteredUsers().size());

		} else
			assertNull(
					"Po usunieciu wszystkich uzytkownikow getRegisteredUsers powinien dac null",
					oei.getRegisteredUsers());
	}

	@Test
	public void nullUsers() {
		assertNull("Brak uzytkownikow, ich zbior powinien byc null",
				oei.getRegisteredUsers());
	}

	@Test
	public void nullObjectTest() {
		assertNull("Brak obiektow dla nieznanego uzytkownika -> null",
				oei.getSortedListOfObjects( "ala") ) ;
		oei.registerUser( "ala" );
		assertNull("Brak obiektow dla zarejestrowanego uzytkownika -> null",
				oei.getSortedListOfObjects( "ala") ) ;		
		oei.addObjectToSystem( "ala", new TestObject( 1 ) );
		assertFalse( "Dodanie obiektu null, oczekiwano false", oei.addObjectToSystem( "ala", null ) );
		oei.deregisterUser( "ala" );
		assertNull("Po usunieciu uzytkownika -> brak obiektow -> null",
				oei.getSortedListOfObjects( "ala") ) ;		
	}

	
	@Test
	public void usersAddOnly() {
		try {
			String u1 = "Ala";
			String u2 = "Bela";
			String u3 = "Cela";

			addUserAndTest(u1);
			addUserAndTest(u2);
			addUserAndTest(u3);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			fail("Test zakonczony pojawieniem sie wyjatku");
		}
	}

	@Test
	public void usersAddAndNonuniq() {
		try {
			String u1 = "Ala";
			String u2 = "Bela";
			String u3 = "Cela";

			addUserAndTest(u1);
			addUserAndTest(u2);
			addUserAndTest(u3);

			addNonUniqUser(u1);
			addNonUniqUser(u2);
			addNonUniqUser(u3);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			fail("Test zakonczony pojawieniem sie wyjatku");
		}
	}

	@Test
	public void usersAddAndNonuniqAndRemove() {
		try {
			String u1 = "Ala";
			String u2 = "Bela";
			String u3 = "Cela";
			String u4 = "alamakota";
			boolean res;

			addUserAndTest(u1);
			addUserAndTest(u2);
			addUserAndTest(u3);

			res = oei.deregisterUser(u4);
			assertFalse(
					"Usuniecie niezarejestrowanego uzytkownika powinno zakonczyc sie false",
					res);

			addNonUniqUser(u1);
			addNonUniqUser(u2);
			addNonUniqUser(u3);

			removeUser(u1);
			removeUser(u2);
			removeUser(u3);

			res = oei.deregisterUser(u4);
			assertFalse(
					"Usuniecie uzytkownika z pustego systemu powinno zakonczyc sie false",
					res);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			fail("Test zakonczony pojawieniem sie wyjatku");
		}
	}

	private void addObject(String username, ValueI obj) {
		int objects;
		if (oei.getSortedListOfObjects(username) != null) {
			objects = oei.getSortedListOfObjects(username).size();
		} else {
			objects = 0; // zakladamy, ze null -> brak obiektow
		}

		boolean res = oei.addObjectToSystem(username, obj);

		assertTrue(
				"Dodanie obiektu dla poprawnego username powinno zwrocic true",
				res);

		assertNotNull( "Lista obiektow dla uzytkownika nie jest pusta, nie moze byc Null.", oei.getSortedListOfObjects(username) );
		
		assertEquals(
				"Dodanie obiektu dla uzytkownika powinno oznaczac powiekszenie listy",
				objects + 1, oei.getSortedListOfObjects(username).size());

		assertTrue(
				"Po dodaniu obiektu dla uzytkownika powinien on byc na liscie jego obiektow",
				oei.getSortedListOfObjects(username).contains(obj));
	}

	@Test
	public void usersAndAddObjects() {
		try {
			String u1 = "Ala";
			String u2 = "Bela";

			addUserAndTest(u1);
			addUserAndTest(u2);

			TestObject o1 = new TestObject(1);
			TestObject o2 = new TestObject(2);

			addObject( u1, o1 );
			addObject( u2, o1 );
			addObject( u2, o2 );
			addObject( u2, o2 );
			addObject( u2, o2 );

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			fail("Test zakonczony pojawieniem sie wyjatku");
		}
	}


	@Test
	public void sortedListTest() {
		try {
			String u1 = "Ala";
			String u2 = "Bela";

			addUserAndTest(u1);
			addUserAndTest(u2);

			TestObject o_1 = new TestObject(-1);
			TestObject o1 = new TestObject(1);
			TestObject o2 = new TestObject(2);
			TestObject o3 = new TestObject(3);
			TestObject o4 = new TestObject(4);
			TestObject o5 = new TestObject(5);

			addObject( u1, o5 );
			addObject( u2, o2 );
			addObject( u2, o1 );
			addObject( u2, o3 );
			addObject( u2, o3 );
			addObject( u2, o_1 );
			addObject( u2, o3 );
			addObject( u2, o4 );

			List<ValueI> list = oei.getSortedListOfObjects( u2 );
			
			assertEquals( "Pierwszy obiekt na liscie powinien zawierac 4", 4, list.get(0).getValue(), 0.01f );
			assertEquals( "Ostatni obiekt na liscie powinien zawierac -1", -1, list.get(list.size()-1).getValue(), 0.01f );
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			fail("Test zakonczony pojawieniem sie wyjatku");
		}		
	}
	
	@Test
	public void sortedMapTest() {
		try {
			String u1 = "Ala";
			String u2 = "Bela";

			addUserAndTest(u1);
			addUserAndTest(u2);

			TestObject o_1 = new TestObject(-1);
			TestObject o1 = new TestObject(1);
			TestObject o2 = new TestObject(2);
			TestObject o3 = new TestObject(3);
			TestObject o4 = new TestObject(4);
			TestObject o5 = new TestObject(5);

			addObject( u1, o5 );
			addObject( u1, o_1 );
			addObject( u1, o_1 );
			addObject( u1, o_1 );
			addObject( u1, o_1 );
			addObject( u1, o_1 );
			addObject( u1, o_1 );
			addObject( u1, o_1 );
			
			addObject( u2, o2 );  //   1
			addObject( u2, o3 );  // 1
			addObject( u2, o3 );  // 2
			addObject( u2, o1 );  //     1
			addObject( u2, o3 );  // 3
			addObject( u2, o3 );  // 4
			addObject( u2, o_1 ); //       1
			addObject( u2, o3 );  // 5
			addObject( u2, o4 );  //          1
			addObject( u2, o_1 ); //       2
			addObject( u2, o2 );  //   2
			addObject( u2, o4 );  //          2

			Map<ValueI, Integer> v2i = oei.getSortedMap( u2 );

			assertEquals( "W mapie jest 5 roznych obiektow", 5, v2i.size() );
			
			int counter = 0;
			for ( Map.Entry< ValueI, Integer> entry : v2i.entrySet() ) {
				if ( counter == 0 ) {
					assertEquals( "Obiekt o3 pojawia sie najczesciej", 5, entry.getValue().intValue() );
					assertEquals( "Obiekt o3 pojawia sie najczesciej", o3, entry.getKey() );
				}
				if ( counter == 4 ) {
					assertEquals( "Obiekt o1 pojawia sie najrzadziej", 1, entry.getValue().intValue() );
					assertEquals( "Obiekt o1 pojawia sie najrzadziej", o1, entry.getKey() );
				}
				counter++;
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			fail("Test zakonczony pojawieniem sie wyjatku");
		}		
	}
	
}
