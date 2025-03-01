/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.interfaces.IEventListener;
import com.mycompany.gamev2.interfaces.IRenderListener;
import com.mycompany.gamev2.interfaces.ITickListener;
import java.util.HashMap;

/**
 *
 * @author J.A
 */
public class EventManager {
    
    private HashMap<Class<? extends BaseEvent>, EventBus> listeners = new HashMap<>();
    
    private static EventManager instance;
    
    private EventManager(){
        this.listeners.put(  TickEvent.class, new EventBus<ITickListener>());
        this.listeners.put(RenderEvent.class, new EventBus<IRenderListener>());
    }
    
    public static EventManager getInstance(){
        if(instance == null) instance = new EventManager();
        return instance;
    }
    
    public <T extends BaseEvent> void postEvent(T event){
        EventBus bus = this.listeners.get(event.getClass());
        if(bus != null) bus.notify(event);
    }
    
    public <T extends IEventListener> void subscribeTo(T listener, Class<? extends BaseEvent> event_type){
        EventBus bus = this.listeners.get(event_type);
        if(bus != null) bus.addListener(listener); //possible runtime failure 
    }
}
