/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system;



import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IEventListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 *
 * @author J.A
 */
public class EventBus<T extends IEventListener> {
    
    private ArrayList<T> bus = new ArrayList<>();
    public String name;
    
    private int e = 0;
    
    private static final int QUEUE_LIMIT = 100;
    private int QUEUE_COUNT = 0;
    private ArrayList<Runnable> QUEUE = new ArrayList<>();
    private boolean isNotifying;
    
    
    public EventBus(String name){
        this.name = name;
        this.isNotifying = false;
    }
    
    public void addListener(T listener){
        if(this.isNotifying){
            System.out.println("Adding "+listener.getClass()+" to the "+this.name+" queue.");
            queue(()->addListener(listener));
            return;
        }
        this.bus.add(listener);
    }
    
    public void removeListener(T listener){
        if(this.isNotifying){
            System.out.println("Adding "+listener.getClass()+" to the "+this.name+" queue.");
            queue(()->removeListener(listener));
            return;
        }
        this.bus.remove(listener);
    }
    
    public void notify(BaseEvent event) {      
        
        if (isNotifying) {
            System.out.println("Adding "+event.getClass()+" to the "+this.name+" queue.");
            queue(event); // Defer to queue
            return;
        }
        
        this.isNotifying = true;
        
        try{
            //we don't iterate the actual bus but a copy of it, to avoid getting concurrency issues
            ArrayList<T> bus_copy = new ArrayList<>(bus); 
                      
            //actually notify the bus
            for(T listener : bus_copy){
                listener.onEventReceived(event); // No if-else, direct call
            }
                      
        }
        
        finally{
            this.isNotifying = false;
            if(QUEUE.size() > 0) cycleQueue();
        }
        
    }
    
    private void cycleQueue(){
        ArrayList<Runnable> copy_queue = new ArrayList<>(QUEUE); //copy to avoid infinite loops
        QUEUE.clear();
        QUEUE_COUNT = 0;
        
        System.out.println("processing queue of size: "+copy_queue.size());
        
        int idx = 1;
        for (Runnable task : copy_queue) {
            System.out.println(this.name+" cycling queue ("+idx+")...");
            task.run(); // Process each queued event
            idx++;
        }
        System.out.println(this.name+" is now free");
    }
    
    public void queue(Runnable task){
        if(QUEUE_COUNT + 1 <= QUEUE_LIMIT){
            QUEUE.add(task);
            QUEUE_COUNT++;
        }
        else{
            System.out.println("EVENT QUEUE LIMIT REACHED: " +task.toString());
        }
    }
    
    public void queue(BaseEvent event){
        if(QUEUE_COUNT + 1 <= QUEUE_LIMIT){
            QUEUE.add(() -> notify(event));
            QUEUE_COUNT++;
        }
        else{
            System.out.println("EVENT QUEUE LIMIT REACHED: "+event.getClass());
        }
    }
    
    public ArrayList<T> getBus(){return bus;}
    public boolean isNotifying(){return this.isNotifying;}
}
