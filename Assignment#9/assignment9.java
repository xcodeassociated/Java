//package com.javaclasses;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class HotelReservation implements HotelReservationInterface {
    QueueKeeperI qKeeper = null;
    CountersI cKeeper = null;
    ReservationEngineAbstract rEngine = null;
    
    Map<Integer, BlockingQueue<Integer>> queues = null;
    Map<Integer, Integer> counters = null;
    Map<Integer, Integer> limits = null;
    
    Object locker = null;
    
    public HotelReservation(){
    	this.qKeeper = new QueueKeeper();
    	this.cKeeper = new Counters();
    	this.rEngine = new ReservationEngine();
    	this.locker = new Object();
    }
    
    @Override
    public void setNumberOfRooms(Map<Integer, Integer> sizeNumber) {
        this.counters = createCounters(sizeNumber);
        this.queues = createQueues();
        this.limits = new HashMap<Integer, Integer>(this.counters);
    }
    
    @Override
    public void reservation(Map<Integer, Integer> requirements, StayAtTheHotel obj) {
        this.rEngine.makeReservation(requirements, obj, this.limits, this.queues, this.counters, this.locker);
    }

    @Override
    public Map<Integer, Integer> getNumberOfFreeRooms() {
        return new HashMap<Integer, Integer>(this.counters);
    }

    private Map<Integer, Integer> createCounters(Map<Integer, Integer> sizeNumber) {
    	return this.cKeeper.makeCountersMap(sizeNumber);
    }

    private Map<Integer, BlockingQueue<Integer>> createQueues() {
    	return this.qKeeper.createQ(this.counters);
    }
}

abstract class ReservationEngineAbstract{
	abstract void makeReservation(Map<Integer, Integer> requirements, Object obj, Map<Integer, Integer> limits, Map<Integer, BlockingQueue<Integer>> queues, Map<Integer, Integer> counters, Object locker);
	abstract boolean reservationValidator(Map<Integer, Integer> requirements, Map<Integer, Integer> limits);
	abstract void reserveRoom(Map<Integer, Integer> requirements, Map<Integer, BlockingQueue<Integer>> queues);
	abstract void releaseRoom(Map<Integer, Integer> requirements, Map<Integer, BlockingQueue<Integer>> queues);
}

interface QueueKeeperI{
	Map<Integer, BlockingQueue<Integer>> createQ(Map<Integer, Integer> counters);
}

interface CountersI{
	public Map<Integer, Integer> makeCountersMap(Map<Integer, Integer> sizeNumber);
}

class QueueKeeper implements QueueKeeperI{
	public QueueKeeper() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Map<Integer, BlockingQueue<Integer>> createQ(Map<Integer, Integer> counters) {
	      Map<Integer, BlockingQueue<Integer>> queues = new HashMap<Integer, BlockingQueue<Integer>>();

          for (Map.Entry<Integer, Integer> entry : counters.entrySet()) {
           BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
           
           for (int i = 0; i < entry.getValue(); ++i) {
               try {
                   queue.put(1);
               } catch (InterruptedException e) { }
           }
           queues.put(entry.getKey(), queue);
       }
       return queues;
	}
}

class Counters implements CountersI {
	@Override
	public Map<Integer, Integer> makeCountersMap(Map<Integer, Integer> sizeNumber) {
	     HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	     prepareCountersMap(sizeNumber, map);
	     return map;
	}

	private void prepareCountersMap(Map<Integer, Integer> sizeNumber, HashMap<Integer, Integer> map) {
		for (Map.Entry<Integer, Integer> entry : sizeNumber.entrySet()) {
		    if (entry.getKey() == null) {
		        continue;
		    }
		    
		    if (entry.getKey() <= 0) {
		        continue;
		    }

		    if (entry.getValue() == null) {
		        continue;
		    }
		    
		    if (entry.getValue() <= 0) {
		        continue;
		    }
		    
		    map.put(entry.getKey(), entry.getValue());
		}
	}
}

class ReservationEngine extends ReservationEngineAbstract{
	void makeReservation(Map<Integer, Integer> requirements, Object _obj, Map<Integer, Integer> limits, Map<Integer, BlockingQueue<Integer>> queues, Map<Integer, Integer> counters, Object locker){
		HotelReservationInterface.StayAtTheHotel obj = (HotelReservationInterface.StayAtTheHotel)_obj;
		
		if (!reservationValidator(requirements, limits)) {
            return;
        }
		
        synchronized (locker) {
        	reserveRoom(requirements, queues);
        }

        obj.start();

        try {
            Thread.sleep(obj.getPeriodOfTime());
            
        } catch (InterruptedException e) {
        	
        }
        obj.stop();
        this.releaseRoom(requirements, queues);
	}
	
	boolean reservationValidator(Map<Integer, Integer> requirements, Map<Integer, Integer> limits){
		int validRequirementCounter = 0;

        for (Map.Entry<Integer, Integer> entry : requirements.entrySet()) {
        	Integer roomType = entry.getKey();

    		if (roomType == null) {
    		    return false;
    		}

    		Integer roomCount = limits.get(roomType);

    		if (roomCount == null) {
    		    return false;
    		}

    		Integer expectedRoomCount = entry.getValue();

    		if (expectedRoomCount == null) {
    		    return false;
    		}
    		if (expectedRoomCount > roomCount) {
    		    return false;
    		}
        	
            validRequirementCounter++;
        }

        return (validRequirementCounter > 0);
	}
	
	void reserveRoom(Map<Integer, Integer> requirements, Map<Integer, BlockingQueue<Integer>> queues){
		  for (Map.Entry<Integer, Integer> entry : requirements.entrySet()) {
	            for (int i = 0; i < entry.getValue(); ++i) {
	                Integer roomType = entry.getKey();
	                try {
	                    queues.get(roomType).take();
	                } catch (InterruptedException e) { }
	            }
	        }
	}
	
	void releaseRoom(Map<Integer, Integer> requirements, Map<Integer, BlockingQueue<Integer>> queues){
	    for (Map.Entry<Integer, Integer> entry : requirements.entrySet()) {
            for (int i = 0; i < entry.getValue(); ++i) {
                Integer roomType = entry.getKey();
                try {
                    queues.get(roomType).put(1);
                } catch (InterruptedException e) { }
            }
	    }
	}
}
