//package com.javaclasses;
import java.util.Random;

interface Assignment2_interface{
	/**
	 *	Every variable in class interface is static and final by default. 
	 */
	
	int max_range = 100;
	int min_range = 1;
	
	public void doTask();
}

class Assignment2 implements Assignment2_interface{
	
	// Class instances:
	private Random random = null;
	private double measurement[];
	private int[] counted; 
	
	public Assignment2(){
		this.random = new Random();
		
		// init class variables:
		this.counted = new int[25];
		this.measurement = new double[3];
	}
	
	private int getBiggest(){ return find(1); }
	private int getSmallest(){ return find(-1); } 

	private int find(int param) {
		int temp_find = this.counted[0];
		for(int i=1; i < this.counted.length; i++){ 
			if (param == 1){
				if(this.counted[i] > temp_find){
					temp_find = this.counted[i];
				}
			}else if (param == -1){
				if(this.counted[i] < temp_find){
					temp_find = this.counted[i];
				}
			}
	    }return temp_find;
	}
	
	public void doTask(){
		int c = 0; 
		for(int j = 1000; j <= 100000; j *= 10){
			long sum = 0L, square_sum = 0L;
			double avr = 0d;
			
			this.clearCounted();
			
			for(int i = 0; i < j; i++){
				int random_num = this.getRandomNumber();
				this.counted[ (random_num - 1) / 4 ]++;
			}
			
			for(int i = 0; i < this.counted.length; i++){
				sum += this.counted[i];
				square_sum += (long)Math.pow(this.counted[i], 2);
			}
		
			avr = this.getAvr(sum);
			this.measurement[c] = this.doMeasurement(square_sum, avr);
			
			this.printStats(j); 
			
			int samllest_num = this.getSmallest();
			int biggest_num = this.getBiggest();
			
			this.printResoults(sum, avr, samllest_num, biggest_num, c);
			c++;
			
		}
	}

	private void clearCounted() {
		this.counted = null;
		this.counted = new int[25];
	}
	
	private void printStats(int j) {
		System.out.println("Statystyki dla: " + j);
		for (int i = 0; i < this.counted.length; i++){
			int begin = (this.counted.length / this.counted.length) + (4 * i);
			int end = ( (this.counted.length / this.counted.length) + 3) + (4 * i);
			
			System.out.print("<" + begin + " , " + end + ">");	
			System.out.println( " : " + this.counted[i] );	
		}
	}

	private int getRandomNumber() {
		return random.nextInt((Assignment2.max_range + 1) - Assignment2.min_range) + Assignment2.min_range;
	}

	private void printResoults(long _sum, double _avr, int samllest_num, int biggest_num, int index) {
		System.out.println("\n>>	Raport: ");
		System.out.print("	suma liczb: " + _sum + " srednia liczb: " + _avr + " najmniejsza wygenerowana: " + samllest_num + " najwieksza wygenerowana: " + biggest_num + " odchylenie wynioslo: " + this.measurement[index] + "\n\n");
	}

	private double doMeasurement(long square_sum, double average) {
		double x = ( ((double)square_sum - ((double)this.counted.length * Math.pow(average, 2))  ) / ( (double)this.counted.length * ((double)this.counted.length - 1)) );
		return Math.sqrt( x );
	}

	private double getAvr(long _sum) { 
		return ( (double)_sum / (double)this.counted.length ); 
	}
	
};

 class Start {
	 
	public static void main(String[] args) {
		System.out.println("Janusz Majchrzak - zadanie 2 \n");
		
		Assignment2 asg2 = new Assignment2();
		asg2.doTask();	
	}

};
