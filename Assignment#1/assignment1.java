//package com.javaclasses;
import java.util.Random;

interface Assignment1_Interface{
	
	/**
	 * 	Every variable defined in the interface is static and final by default.
	 *  */
	
	int max_range = 75;
	int min_range = 0;
	
	public void doTask(int _count);
};

class Assignment1 implements Assignment1_Interface{
	public static Random random_generator = null;
	
	/* Class default c-tor */
	public Assignment1(){
		//Init random number generator as class instance 
		random_generator = new Random();
	}
	
	/* Main task */
	public void doTask(int _count){
		long  square_sum = 0L, sum = 0L;
		for (int i = 0; i < _count; i++) {
			int generated = this.getRandomInt(random_generator);
			sum += (long)generated;
			square_sum += (long)square(generated);
		};
		double average = this.getAverage(_count, sum);
		double measurement = this.doMeasurement(_count, average, square_sum);
		
		this.printTask(_count, average, measurement, sum);
	}

	/* Print out data: */
	private void printTask(int _count, double average, double measurement, long sum) {
		System.out.println("Losowania: " + _count + 
				"\n>>	suma: " + sum + 
				"\n>>	srednia: " + average + 
				"\n>>	odchylenie: " + measurement);
	}

	private double doMeasurement(int _count, double average, long square_sum) {
		double x = ( ((double)square_sum - ((double)_count * Math.pow(average, 2))  ) / ( (double)_count * ((double)_count - 1)) );
		return Math.sqrt( x );
	}
	
	private double getAverage(int _count, long sum) {
		return ( (double)sum / (double)_count );
	}

	private double square(int generated) {
		return Math.pow(generated, 2);
	}
	
	private int getRandomInt(Random random_generator){
		 return random_generator.nextInt((max_range + 1) - min_range) + min_range; 
	}
	
}

interface Start_Interface{
	int _array[] = {100, 1000, 10000};	
}

class Start implements Start_Interface {
	
	public static void main(String[] args) {
		System.out.println("Janusz Majchrzak - zadanie 1");
		
		Assignment1 asg1 = new Assignment1();
		for (int _i : Start._array){ asg1.doTask(_i); };
		
	};
};