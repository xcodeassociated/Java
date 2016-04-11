//package com.javaclasses;

import java.util.ArrayList;

/**
 * 
 * Abstract Functor class of Functor object.
 * Every Functor has to extend this class.
 * Thare are only two types of Functor: true & false. Each of them represents different logic value/state.
 *   
 */

abstract class FunctorAbstract{
	abstract public Boolean getLogicValue();
}

class FunctorTrue extends FunctorAbstract{
	private Boolean logicValue;
	public FunctorTrue() {
		this.logicValue = new Boolean(true);
	}
	
	public Boolean getLogicValue(){
		return this.logicValue;
	}
}

class FunctorFalse extends FunctorAbstract{
	private Boolean logicValue;
	public FunctorFalse() {
		this.logicValue = new Boolean(false);
	}
	
	public Boolean getLogicValue(){
		return this.logicValue;
	}
}

/**
 * 
 * Abstract class of Operation object.
 * Every operation has to extend this class in order to work with the Functor operations.
 * 
 */
abstract class OperactionAbstract{
	public Boolean runOperation(FunctorAbstract a, FunctorAbstract b, FunctorAbstract c, FunctorAbstract d, FunctorAbstract e){ return null; };
	public Boolean runOperation(FunctorAbstract a, FunctorAbstract b, FunctorAbstract c){ return null; };
	public Boolean runOperation(FunctorAbstract a, FunctorAbstract b){ return null; };
	public Boolean runOperation(FunctorAbstract a){ return null; };
}

//OR logic operator
class OR extends OperactionAbstract{
	public Boolean runOperation(FunctorAbstract a, FunctorAbstract b){
		if (a == null || b == null) return null;
		
		return (a.getLogicValue() || b.getLogicValue()) ? new Boolean(true) : new Boolean(false);
	}
}

//AND logic operator
class AND extends OperactionAbstract{
	public Boolean runOperation(FunctorAbstract a, FunctorAbstract b){ 
		if (a == null || b == null) return null;
		
		return (a.getLogicValue() && b.getLogicValue()) ? new Boolean(true) : new Boolean(false);
	}
}

//NOT logic operator
class NOT extends OperactionAbstract{
	public Boolean runOperation(FunctorAbstract a){ 
		if (a == null) return null;
		
		Boolean t = a.getLogicValue();
		Boolean new_t = !t;
		return new Boolean(new_t);
	};
}

//vote3 logic operation
class Vote3 extends OperactionAbstract{
	public Boolean runOperation(FunctorAbstract a, FunctorAbstract b, FunctorAbstract c){ 
		FunctorAbstract[] funcTab = {a, b, c};
		for (FunctorAbstract element: funcTab){
			if (element == null)
				return null;
		}
		
		int numTrue = 0, numFalse = 0;
		
		for (FunctorAbstract element: funcTab){
			if (element.getLogicValue() == true)
				numTrue++;
			else
				numFalse++;
		}
		
		if (numTrue > numFalse)
			return new Boolean(true);
		else
			return new Boolean(false);
	}

}

//vote5 logic operation
class Vote5 extends OperactionAbstract{
	public Boolean runOperation(FunctorAbstract a, FunctorAbstract b, FunctorAbstract c, FunctorAbstract d, FunctorAbstract e){ 
		FunctorAbstract[] funcTab = {a, b, c, d, e};
		for (FunctorAbstract element: funcTab){
			if (element == null)
				return null;
		}
		
		int numTrue = 0, numFalse = 0;
		
		for (FunctorAbstract element: funcTab){
			if (element.getLogicValue() == true)
				numTrue++;
			else
				numFalse++;
		}
		
		if (numTrue > numFalse)
			return new Boolean(true);
		else
			return new Boolean(false);
	}
}

/**
 * 
 * Implementation of RPNLogicInterface in class RPNLogic.
 * RPN - stends of: "Polish Reversed Notation". 
 *
 */

class RPNLogic implements RPNLogicInterface{
	private ArrayList<FunctorAbstract> functors = null;
	private ArrayList<OperactionAbstract> operations = null;
	private int functors_index = 0, operations_index = 0;
	private boolean error = false;
	
	public RPNLogic(){
		this.functors = new ArrayList<FunctorAbstract>();
		this.operations = new ArrayList<OperactionAbstract>();
		this.functors_index = 0;
		this.operations_index = 0;
		this.error = false;
	}
	
	/**
	 * 
	 * Private class method - app logic, public methods are interface implementation and calling the private mechanics.
	 * 
	 */
	
	private void increment_functors_index(){
		this.functors_index++;
	}
	
	private void increment_operations_index(){
		this.operations_index++;
	}
	
	private OperactionAbstract getFirestOutOfOperations(){
		OperactionAbstract temp = this.operations.get(0);
		this.operations.remove(0);
		
		if (this.operations_index > 0) this.operations_index--;
		
		return temp;
	}
	
	private FunctorAbstract getFirstOutOfFunctors(){
		FunctorAbstract temp = this.functors.get(0);
		this.functors.remove(0);
		
		if (this.functors_index > 0) this.functors_index--;
		
		return temp;
	}
	
	private void putResoultOfOperationIntoFunctors(Boolean e){
		if (e != null){
			if (e.booleanValue() == true)
				this.functors.add(0, new FunctorTrue());
			else
				this.functors.add(0, new FunctorFalse());
		}
	}
	
	private boolean checkFunctorArray(FunctorAbstract[] array){
		for (FunctorAbstract element: array){
			if (element == null)
				return false;
		}
		return true;
	}
	
	private Boolean execSingleOperation(){
		OperactionAbstract operation = this.getFirestOutOfOperations();
		
		if (operation instanceof NOT){
			if (this.functors.size() < 1) return null;
			
			FunctorAbstract[] e = { this.getFirstOutOfFunctors() };
			if (this.checkFunctorArray(e) == false) return null;
			
			Boolean resoult = operation.runOperation(e[0]);
			this.putResoultOfOperationIntoFunctors(resoult);
			
			return (resoult != null) ? resoult : null;
			
		}else if ( (operation instanceof AND) || (operation instanceof OR)){
			if (this.functors.size() < 2) return null;
			
			FunctorAbstract[] e = { this.getFirstOutOfFunctors(), 
									this.getFirstOutOfFunctors() };
		
			if (this.checkFunctorArray(e) == false) return null;
			
			Boolean resoult = operation.runOperation(e[0], e[1]);
			this.putResoultOfOperationIntoFunctors(resoult);
	
			return (resoult != null) ? resoult : null;
			
		}else if (operation instanceof Vote3){
			if (this.functors.size() < 3) return null;
			
			FunctorAbstract[] e = { this.getFirstOutOfFunctors(), 
									this.getFirstOutOfFunctors(), 
									this.getFirstOutOfFunctors() };

			if (this.checkFunctorArray(e) == false) return null;
			
			Boolean resoult = operation.runOperation(e[0], e[1], e[2]);
			this.putResoultOfOperationIntoFunctors(resoult);

			return (resoult != null) ? resoult : null;
			
		}else if (operation instanceof Vote5){
			if (this.functors.size() < 5) return null;
			
			FunctorAbstract[] e = { this.getFirstOutOfFunctors(), 
									this.getFirstOutOfFunctors(), 
									this.getFirstOutOfFunctors(), 
									this.getFirstOutOfFunctors(), 
									this.getFirstOutOfFunctors() };
			
			if (this.checkFunctorArray(e) == false) return null;
			
			Boolean resoult = operation.runOperation(e[0], e[1], e[2], e[3], e[4]);
			this.putResoultOfOperationIntoFunctors(resoult);

			return (resoult != null) ? resoult : null;
			
		}else
			return null;
		
	}
	
	private Boolean execOperations(){
		Boolean finalRes = null; 
		int i = 0, orgin = this.operations_index;
		
		while( i < orgin ){
			finalRes = this.execSingleOperation();
			i++;
		}
		
		return finalRes;
	}
	
	/**
	 * 
	 * Public methods - interface implementation for user interaction.
	 * 
	 */
	
	public void truee(){
		if (this.error == true) return;
		
		this.functors.add(this.functors_index, new FunctorTrue());
		this.increment_functors_index();
	}
	
	public void falsee(){
		if (this.error == true) return;
		
		this.functors.add(this.functors_index, new FunctorFalse());
		this.increment_functors_index();
	}
	
	public void and(){
		if (this.error == true) return;
		
		this.operations.add(this.operations_index, new AND());
		this.increment_operations_index();
	}

	public void or(){
		if (this.error == true) return;
		
		this.operations.add(this.operations_index, new OR());
		this.increment_operations_index();
	}
	
	public void not(){
		if (this.error == true) return;
		
		this.operations.add(this.operations_index, new NOT());
		this.increment_operations_index();
	}
	
	public void vote3(){
		if (this.error == true) return;
		
		this.operations.add(this.operations_index, new Vote3());
		this.increment_operations_index();
	}
	
	public void vote5(){
		if (this.error == true) return;
		
		this.operations.add(this.operations_index, new Vote5());
		this.increment_operations_index();
	}

	public void clear(){
		this.functors.clear();
		this.operations.clear();
		this.functors_index = 0;
		this.operations_index = 0;
		this.error = false;
	}
	
	public Boolean result(){
		if (this.error == false){
			Boolean resoult = this.execOperations();
			
			if (resoult == null)
				this.error = true;
			
			return resoult;
		}else
			return null; //error has occurred in some logic operation -> null
	}
}

class Start {

	public static void main(String[] args) {
		RPNLogic rpn = new RPNLogic();
		
		/*------------------------------ Test #1 ------------------------------*/
		
		 rpn.falsee();
		 rpn.truee();
		 rpn.and();
		 Boolean r1 = rpn.result();
		 rpn.not();
		 Boolean r2 = rpn.result(); 
		 rpn.falsee();
		 rpn.or();
		 Boolean r3 = rpn.result(); 
		 
		 rpn.clear();
		 
		 //expected: false true true
		 System.out.println("r1: " + r1.toString() + " r2: " + r2.toString() + " r3: "  + r3.toString());
		 
		 /*------------------------------ Test #2 ------------------------------*/
		 
		 rpn.truee();
		 rpn.falsee();
		 rpn.falsee();
		 rpn.vote3();
		 
		 Boolean r4 = rpn.result(); //false: vote3
		 System.out.println("r4: " + r4.toString());
		 
		 rpn.not();
		 
		 Boolean r5 = rpn.result(); //true: !r4 
		 System.out.println("r5: " + r5.toString());
		 
		 rpn.result(); // check if crash with empty functors and operation ArrayList
		 
		 rpn.clear();
		 
		 /*------------------------------ Test #3 ------------------------------*/

		 rpn.truee();
		 rpn.or();
		 
		 rpn.result(); //null : true || <empty> => null
		 
		 rpn.clear(); 
		 
		 rpn.falsee();
		 rpn.falsee();
		 rpn.and(); //false: false && false => false
		 rpn.not(); //true: !false => true
		 rpn.not(); //false: !true
		 rpn.not(); //true: !false
		 rpn.falsee();
		 rpn.and(); //false: true && false => false
		 rpn.truee();
		 rpn.or(); //true: false || true => true
		 rpn.not(); //false: !true
		 rpn.truee();
		 rpn.truee();
		 rpn.vote3(); //true: vote3(false, true, true) => true
		 rpn.falsee();
		 rpn.falsee();
		 rpn.falsee();
		 rpn.truee();
		 rpn.vote5(); //false: vote5(true, false, false, false, true) => false
		 rpn.not(); //true: !false
		 
		 Boolean r6 = rpn.result();
		 System.out.println("r6: " + r6.toString());
	
		 rpn.result(); //check if not crash? ok!
		 
		 rpn.clear();
	}

}
