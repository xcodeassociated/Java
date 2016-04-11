//package com.javaclasses;

import java.util.*;

class Indices {
   public int firstIndex;
   public int secondIndex;
   public int thirdIndex;
}

interface PathFinderInterface{
	public boolean find( boolean[][][] labyrinth, Indices start, Indices finish );
}

class PathFinder implements PathFinderInterface{
	private int step;
	private ArrayList<Indices> lastStep = null;
	
	static{ /* static block */ }
	
	{ /*non-static block*/	}
	
	PathFinder(){
		this.lastStep = new ArrayList<Indices>();
		this.step = 0;
	}
	
	private int[] getTabDimensionSize(boolean[][][] tab){
		int[] temp = new int[3];
		temp[0] = tab.length;
		temp[1] = tab[0].length;
		temp[2] = tab[0][0].length;
		return temp;
	}
	
	private boolean tabBoundery(boolean[][][] tab, Indices check){
		int[] tab_length = this.getTabDimensionSize(tab);
		
		if ((check.firstIndex <= (tab_length[0] - 1) &&  check.firstIndex >= 0) && 
			(check.secondIndex <= (tab_length[1] - 1) && check.secondIndex >= 0) && 
			(check.thirdIndex <= (tab_length[2] - 1) && check.thirdIndex >= 0)){
			
			return true;
		}else
			return false;
	}
	
	private boolean getWallInfo(boolean[][][] tab, Indices check){
		return tab[check.firstIndex][check.secondIndex][check.thirdIndex];
	}
	
	private Indices getNext(int x, int y, int z){
		Indices temp = new Indices();
		temp.firstIndex = x;
		temp.secondIndex = y;
		temp.thirdIndex = z;
		
		return temp;
	}
	
	private Indices[] getPossibleCellsIndices(Indices arg){
		Indices[] temp = new Indices[6];
		temp[0] = getNext(arg.firstIndex + 1,	arg.secondIndex, 		 arg.thirdIndex);
		temp[1] = getNext(arg.firstIndex, 		arg.secondIndex + 1,	 arg.thirdIndex);
		temp[2] = getNext(arg.firstIndex, 		arg.secondIndex, 		 arg.thirdIndex + 1);
		temp[3] = getNext(arg.firstIndex - 1, 	arg.secondIndex, 		 arg.thirdIndex);
		temp[4] = getNext(arg.firstIndex, 		arg.secondIndex - 1,     arg.thirdIndex);
		temp[5] = getNext(arg.firstIndex, 		arg.secondIndex, 		 arg.thirdIndex - 1);
		
		return temp;
	}
	
	private String walkInfo(int arg){
		switch (arg){
			case 0:{
				return "move right";
			}
			case 1:{
				return "move up";
			}
			case 2:{
				return "move forward";
			}
			case 3:{
				return "move left";
			}
			case 4:{
				return "move down";
			}
			case 5:{
				return "move backward";
			}
			default:{
				return "unknow move...";
			}
		}
	}
	
	private void insertToLastStep(Indices arg) {
		this.lastStep.add(arg);
	}

	private boolean checkLastSteps(Indices arg) {
		for (int i = 0; i < this.lastStep.size(); i++){
			if (this.lastStep.get(i).firstIndex == arg.firstIndex &&
				this.lastStep.get(i).secondIndex == arg.secondIndex &&
				this.lastStep.get(i).thirdIndex == arg.thirdIndex){
					
					return true;
			}
		}return false;
	}
	
	private boolean tab3DValid(boolean[][][] tab){
		return (this.getTabDimensionSize(tab)[0] > 0 && this.getTabDimensionSize(tab)[1] > 0 && this.getTabDimensionSize(tab)[2] > 0) ? true : false;
	}
	
	private String getTabIndexesAsString(Indices arg){
		return new String("[" + arg.firstIndex + "]" + "[" + arg.secondIndex + "]" + "[" + arg.thirdIndex + "]");
	}
	
	private boolean isValid(boolean[][][] _labyrinth, Indices arg, Indices arg2) {
		if (arg != null && arg2 != null &&  _labyrinth != null){
			if (this.tab3DValid(_labyrinth)){
				return true;
			}
			return false;
		}
		return false;			
	}

	private boolean isFinish(Indices arg, Indices arg2) {
		return arg.firstIndex == arg2.firstIndex && arg.secondIndex == arg2.secondIndex && arg.thirdIndex == arg2.thirdIndex;
	}
	
	private boolean areStartAndFinishInside(boolean[][][] _labyrinth, Indices _start, Indices _finish){
		if (this.tabBoundery(_labyrinth, _start) == true && this.tabBoundery(_labyrinth, _finish) == true){
			if (this.getWallInfo(_labyrinth, _start) == true && this.getWallInfo(_labyrinth, _finish) == true)
				return true;
			else
				return false;
		}else
			return false;
	}
	
	private boolean _find(boolean[][][] labyrinth, Indices start, Indices finish){
		if (this.isValid(labyrinth, start, finish)){
			if (this.isFinish(start, finish) == true){ 
				System.out.println(this.step + " " + this.getTabIndexesAsString(start) + " - way out!  \n");
				return true;
			}else{
				
				if (this.areStartAndFinishInside(labyrinth, start, finish) == false){
					System.out.println("Given Start or Finish Indices are incorrect - may be set as flase or be outside the labyrinth!");
					return false;
				}
				
				Indices[] indiTab = this.getPossibleCellsIndices(start);
				int[] j = this.getMinusFillededArray(6);
				
				for (int i = 0; i < indiTab.length; i++){

					if (this.tabBoundery(labyrinth, indiTab[i]) == true && this.checkLastSteps(indiTab[i]) == false){
						if (this.getWallInfo(labyrinth, indiTab[i]) == true){
							System.out.println(this.getWalkReport(start, i, indiTab[i]));
							j[i] = i;
						}
					}
					
				}System.out.println("");
				
				this.insertToLastStep(start);
				this.step++;
					
				if (this.getNumberOfPassiblePaths(j) > 0)
					return (this.runRecursionTreeSearch(j, labyrinth, indiTab, finish) > 0) ? true : false;
				else{
					System.out.println(this.getTabIndexesAsString(start) + " blind/alredy visited cell!");
					return false;
				}
			}
		}else{
			System.out.println("Invalid data has been passed!");
			return false;
		}
	}
	
	public boolean find (boolean[][][] labyrinth, Indices start, Indices finish) {
		
		//call main path finding method
		boolean main_resoult = this._find(labyrinth, start, finish);
		
		//reset all instance field - prepare for the next _find call
		this.step = 0;
		this.lastStep.clear();
		
		return main_resoult;
	}
	
	private int[] getMinusFillededArray(int _size_){
		int[] temp = new int[_size_];
		for (int i = 0; i < _size_; i++)
			temp[i] = -1;
		
		return temp;
	}
	
	private String getWalkReport(Indices arg, int i, Indices arg2){
		return new String(this.getTabIndexesAsString(arg) + " passible for: (" + this.walkInfo(i) + ") to -> " + this.getTabIndexesAsString(arg2));
	}

	private int runRecursionTreeSearch(int[] _j, boolean[][][] _tab, Indices[] _indis, Indices _finish){
		boolean[] foo = new boolean[_j.length]; 
		for (int c = 0; c < _j.length; c++){
			if (_j[c] != -1){
				foo[c] = this._find(_tab, _indis[ _j[c] ], _finish);						
			}
		}
		
		int temp = 0;
		for (boolean e: foo){
			if (e == true)
				temp++;
		}
		return temp;
	}
	
	private int getNumberOfPassiblePaths(int[] j) {
		int temp = 0;
		for (int e : j){
			if (e != -1)
				temp++;
		}
		return temp;
	}

};

class Start {
	
	static public boolean[][][] tab;

	public static boolean[][][] makeLabirynth(){
		boolean[][][] tab = new boolean[5][5][5];
		tab[1][0][0] = true;
		tab[1][0][1] = true;
		tab[1][1][1] = true;
			
			//div
			tab[1][1][2] = true;
					
		tab[2][1][1] = true;
		tab[2][1][2] = true;
		
			//div
			tab[3][1][2] = true;
			tab[4][1][2] = true;
			tab[4][1][3] = true;
			tab[4][1][4] = true;
			tab[4][2][4] = true;
			tab[4][3][4] = true;
			
		tab[2][1][3] = true;
		tab[2][2][3] = true;
		
			//div
			tab[1][3][3] = true;
			tab[0][3][3] = true;
			
				//div
				tab[0][3][2] = true;
				tab[0][3][1] = true;
				tab[0][3][0] = true;
						
			tab[0][4][3] = true;
			
		tab[2][3][3] = true;
		tab[3][3][3] = true;
		tab[3][3][4] = true;
		
		return tab;
	}
	
	static{
		
		Start.tab = Start.makeLabirynth();
	
	}
	
	public static void main(String[] args) {
		
		Indices start = new Indices();
		Indices finish = new Indices();

		start.firstIndex = 1;
		start.secondIndex = 0;
		start.thirdIndex = 0;
		
		finish.firstIndex  = 3; 
		finish.secondIndex = 3; 
		finish.thirdIndex =  4; 
		
		boolean[][][] true_tab = new boolean[5][5][5];
		for(int a = 0; a < 5; a++) {
		    for(int b = 0; b < 5; b++) {
		        for(int c = 0; c < 5; c++) {
		            true_tab[a][b][c] = true;
		        }
		    }
		}
		
		System.out.println("--------- Path Finder logs: --------- \n");
		PathFinder pf = new PathFinder();
		
		/**
		 * Tests: 
		 * 	#1 normal order - start -> finish; 
		 *	#2 reversed order - finish -> start; 
		 * 	#3 all true - start -> finish;
		 */
		
		boolean pf_resoult = pf.find(Start.tab, start, finish);
		//boolean pf_resoult = pf.find(Start.tab, finish, start);
		//boolean pf_resoult = pf.find(true_tab, start, finish);
		System.out.println("-------------------------------------------------");
		
		System.out.println("Can we find a way out? " + pf_resoult);
	}

};