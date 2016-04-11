//package com.javaclasses;

// JUnit imports
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class KalkulatorTest{
	private KalkulatorI kalkulator = null;
	private Accumulator accumulator = null; 
	
	private void accumulatorToKalkulator(){
		kalkulator.setAccumulatorA(this.accumulator);
		assertTrue("The accumulatorA was not set, and it's value is stil Integer.MIN_VALUE - check 'setAccumulatorA' method!", kalkulator.getA() != Integer.MIN_VALUE);
	}
	
	@Before public void prepareInstances(){
		this.kalkulator = new Kalkulator();
		this.accumulator = new Accumulator();
		
		//check if the objects were made
		assertFalse("Unexpected: Accumulator instance is still null, after the constructor call - check the class constructo.", this.accumulator == null);
		assertFalse("Unexpected: Kalkulator instance is still null, after the constructor call ", this.kalkulator == null);
	}
	
	@Test public void AccumulatorASettingValueTests(){
		assertEquals("Default value of accumulatorA should be 0", 0, this.accumulator.getValue());
		this.accumulator.setValue(1);
		assertEquals("The value of accumulatorA has been set for 1 - if this test has failed, check also getValue method!", this.accumulator.getValue(), 1);
		this.accumulator.setValue(this.accumulator.getValue() + 2);
		assertEquals("The value of accumulatorA has been changed for 1 to 3", 3, this.accumulator.getValue());
	}
	@Test public void UnsetAccumulatorsCall(){
		assertTrue("Unset Accumulator should return Integer.MIN_VALUE out of 'getA()'", kalkulator.getA() 		== Integer.MIN_VALUE);
		assertTrue("Unset Accumulator should return Integer.MIN_VALUE out of 'addA(...)' ", kalkulator.addA(0) 	== Integer.MIN_VALUE);
		assertTrue("Unset Accumulator should return Integer.MIN_VALUE out of 'subA(...)'", kalkulator.subA(0) 	== Integer.MIN_VALUE);
	}
	
	/* ---------------------------------------------- Tests with set accumulator ---------------------------------------------------------- */

	@Test public void CleanAccum(){
		this.accumulatorToKalkulator(); //set the accumulator 
		
		this.accumulator.setValue(4567);
		assertTrue("Unexpected value returned after set", this.accumulator.getValue() == 4567);
		
		this.accumulator.setValue(777);
		assertTrue("Unexpected value returned after set", this.accumulator.getValue() == 777);
		
		this.kalkulator.zeroA(); 
		
		assertEquals("Unexpected 'zeroA()' seems not working - the value was not reset to 0", this.accumulator.getValue(), 0);
		assertEquals("Unexpected 'zeroA()' seems not working when called by the 'kalkulator' - might return prev. operation result", this.kalkulator.getA(), 0);
	}
	@Test public void NeutralOperation(){
		this.accumulatorToKalkulator(); //set the accumulator 
		
		assertTrue(this.kalkulator.getA() == 0);
		this.kalkulator.addA(12);
		this.kalkulator.subA(12);
		assertEquals("Unexpected result of neural operation: +12 - 12", kalkulator.getA(), 0);
	}
	@Test public void NeutralOperation2(){
		this.accumulatorToKalkulator(); //set the accumulator 
		
		this.kalkulator.addA(3);
		this.kalkulator.addA(-3);
		assertEquals("Unexpected result of neutral adding: 3 + (-3)", kalkulator.getA(), 0);
	}
	@Test public void NeutralOperation3(){
		this.accumulatorToKalkulator(); //set the accumulator 
		
		this.kalkulator.subA(3);
		this.kalkulator.subA(-3);
		assertEquals("Unexpected result of neutral subtraction: 3 + (-3)", kalkulator.getA(), 0);
	}
	@Test public void SetValueTest(){
		this.accumulatorToKalkulator(); //set the accumulator 

		this.accumulator.setValue(4567);
		assertTrue("Unexpected value returned after set", this.accumulator.getValue() == 4567);
		
		this.accumulator.setValue(777);
		assertTrue("Unexpected value returned after set", this.accumulator.getValue() == 777);
		
		assertEquals("Unexpeced value of accumulator and kalkulator are not the same - might the kalkulator use different accumulator", this.accumulator.getValue(), this.kalkulator.getA());
	}
	@Test public void AdditionWithSetAccumulator(){
		this.accumulatorToKalkulator(); //set the accumulator 
		
		assertTrue("Default value of accumulator hasn't been set.", this.kalkulator.getA() == 0);
		assertEquals("Unexpected fail with adding 2 to the 0 (default value)", this.kalkulator.addA(2), 2);
		assertEquals("Unexpected Method getA() doesn't return a right value after addA method been called with arguent 2", this.kalkulator.getA(), 2);
		assertEquals("Unexpected Kalkulator wrong implementation of addA() method - might operateon the different accumulator", this.accumulator.getValue(), 2);
		assertEquals("Unexpected result of adding 2 (prev. value) + 13 (passed value)", this.kalkulator.addA(13), 15);
		assertEquals("Unexpected result with adding a negative value: 15 (prev. value) + (-12) (passed value)", this.kalkulator.addA(-12), 3);
		assertEquals("Unexpected result of 'getA()' should be 3, as above.", this.kalkulator.getA(), 3);
	}
	@Test public void SubstractionWithSetAccumulator(){
		this.accumulatorToKalkulator(); //set the accumulator 
		
		assertTrue("Default value of accumulator is different than", this.kalkulator.getA() == 0);
		assertEquals("Unexpected value with substraction (default value - 0) and -3", this.kalkulator.subA(3), -3);
		assertEquals("Unexpected value returned after above operation.", this.kalkulator.getA(), -3);
		assertEquals("Unexpected value returned after sub(-3) should be 0 because: -3 (prev. value) - (-3) = 0", this.kalkulator.subA(-3), 0);
		this.kalkulator.subA(2); //now the accum value should be -2
		assertEquals("Kalkulator's method subA() might change the value of wrong accumulator - other than was set in setAccumulatorA()", this.accumulator.getValue(), -2);
	}
	
};
