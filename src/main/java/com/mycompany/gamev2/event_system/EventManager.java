/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IEventListener;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.interfaces.event_listeners.IGameplayListener;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;
import com.mycompany.gamev2.interfaces.event_listeners.IWorldListener;
import java.util.HashMap;

/**
 *
 * @author J.A
 */
public class EventManager {
    
    private HashMap<Class<? extends IEventListener>, EventBus<?>> EVENT_BUSSES = new HashMap<>();
    
    
    
    private static EventManager instance;
    
    private EventManager(){
       EVENT_BUSSES.put(IGameUpdateListener.class, new EventBus<IGameUpdateListener>("GAME_BUS"));
       EVENT_BUSSES.put(IGameplayListener.class, new EventBus<IGameplayListener>("GAMEPLAY_BUS"));
       EVENT_BUSSES.put(IInputListener.class, new EventBus<IInputListener>("INPUT_BUS"));
       EVENT_BUSSES.put(IWorldListener.class, new EventBus<IWorldListener>("LEVEL_BUS"));
       
    }
    
    public static EventManager getInstance(){
        if(instance == null) instance = new EventManager();
        return instance;
    }
        
    @SuppressWarnings("unchecked")
    public <T extends IEventListener> void post(BaseEvent event, Class<T> listenerType) {
        EventBus<T> bus = (EventBus<T>) EVENT_BUSSES.get(listenerType);
        
        if(bus == null) {throw new IllegalArgumentException("No such evet-bus for event type");}
     
        bus.notify(event);        
    }
    
    @SuppressWarnings("unchecked")
    public <T extends IEventListener> void subscribe(T listener, Class<T> listenerType) {
        EventBus<T> bus = (EventBus<T>) EVENT_BUSSES.get(listenerType);
        if (bus == null) {
            bus = new EventBus<>("DYNAMIC_BUS");
            EVENT_BUSSES.put(listenerType, bus);
        }
        bus.addListener(listener);
    }
    
     @SuppressWarnings("unchecked")
    public <T extends IEventListener> void unsubscribe(T listener, Class<T> listenerType) {
        EventBus<T> bus = (EventBus<T>) EVENT_BUSSES.get(listenerType);
        if (bus != null) bus.removeListener(listener);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends IEventListener> void unsubscribeAll(T listener) {
        
        for(EventBus bus : EVENT_BUSSES.values()){
            if(bus.getBus().contains(listener)) bus.removeListener(listener);
        }
    }
}
