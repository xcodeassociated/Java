//package com.javaclasses;
import java.util.ArrayList;

interface RoomPetenratorInterface{
	public AbstractRoom findPlatypus(AbstractRoom room, ArrayList<AbstractRoom> visited);
}

abstract class RoomPenetratorAbstract implements RoomPetenratorInterface{
	abstract protected AbstractRoom keepShearching(AbstractRoom room, ArrayList<AbstractRoom> visited);
	abstract public AbstractRoom findPlatypus(AbstractRoom room, ArrayList<AbstractRoom> visited);	
}

class RoomPenetrator extends RoomPenetratorAbstract{
	RoomPenetrator(){
		//default c-tor
	}
	
	protected AbstractRoom keepShearching(AbstractRoom room, ArrayList<AbstractRoom> checkedRooms){
		if(room.hasPlatypus()){ 
			return room;
		}else{
			checkedRooms.add(room);
			AbstractRoom temporary = null; 
			AbstractRoom[] doors = room.getDoors();
			
			if(doors==null) 
				return null;
			
			for(AbstractRoom e: doors){
				if(!checkedRooms.contains(e)){
					temporary = this.keepShearching(e, checkedRooms);
				
					if(temporary!= null) 
						break;
				}
			}
			return temporary;
		}
	};
	
	public AbstractRoom findPlatypus(AbstractRoom room, ArrayList<AbstractRoom> checkedRooms){
		return this.keepShearching(room, checkedRooms);
	};
}

class PlatypusFinder extends PlatypusAbstractFinder{
	private ArrayList<AbstractRoom> checkedRooms = null;
	private AbstractRoom platypusIsIn = null;
	private boolean PlatypusFound = false;
	private RoomPenetratorAbstract roomPenetrator = null;
	
	public PlatypusFinder() {
		this.checkedRooms = new ArrayList<AbstractRoom>();
		this.roomPenetrator = new RoomPenetrator();
	};
	
	public void find( AbstractRoom entranceToTheBuilding ){
		if (this.roomPenetrator != null && entranceToTheBuilding != null){
            this.platypusIsIn = null;
            this.PlatypusFound = false;
            
			this.platypusIsIn = this.roomPenetrator.findPlatypus(entranceToTheBuilding, this.checkedRooms);
			
			if (this.platypusIsIn != null)
				this.PlatypusFound = true;
			
			this.checkedRooms.clear();
		}
	};

	public AbstractRoom getRoomWithPlatypus(){
		if (this.platypusIsIn != null && this.PlatypusFound == true)
			return this.platypusIsIn;
		else 
			return null;
	};
	
};

class Start {

	public static void main(String[] args) {
		//PlatypusFinderTester.runTest();
	}

}
