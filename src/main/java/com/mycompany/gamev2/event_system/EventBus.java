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
    
    
    private static final int QUEUE_LIMIT = 10;
    private int QUEUE_COUNT = 0;
    private ArrayList<Runnable> EVENT_QUEUE = new ArrayList<>();
    private boolean isNotifying;
    
    
    public EventBus(String name){
        this.name = name;
        this.isNotifying = false;
    }
    
    public void addListener(T listener){
        this.bus.add(listener);
    }
    
    public void removeListener(T listener){
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
            Iterator<T> i = bus.iterator();
            //System.out.println(this.name);
            
            //actually notify the bus
            while(i.hasNext()){
                T listener = i.next();
                listener.onEventReceived(event); // No if-else, direct call
            }
        }
        catch(ConcurrentModificationException e){ //THE MOMENNT I ADDED THIS CATCH IN HERE IT STARTED WORKING AAAAAAARGH
            System.out.println(e);
        }
        finally{
            this.isNotifying = false;
            cycleQueue();
        }
        
    }
    
    private void cycleQueue(){
        ArrayList<Runnable> copy_queue = new ArrayList<>(EVENT_QUEUE); //copy to avoid infinite loops
        EVENT_QUEUE.clear();
        for (Runnable task : copy_queue) {
            task.run(); // Process each queued event
        }
    }
    
    public void queue(BaseEvent event){
        if(QUEUE_COUNT + 1 <= QUEUE_LIMIT){
            EVENT_QUEUE.add(() -> notify(event));
            QUEUE_COUNT++;
        }
        else{
            System.out.println("EVENT QUEUE LIMIT REACHED");
        }
    }
    
    public ArrayList<T> getBus(){return bus;}
    public boolean isNotifying(){return this.isNotifying;}
}
