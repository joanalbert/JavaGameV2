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
import java.util.ArrayList;

/**
 *
 * @author J.A
 */
public class EventBus<T extends IEventListener> {
    
    private ArrayList<T> bus = new ArrayList<>();
    
    public EventBus(){
    
    }
    
    public void addListener(T listener){
        this.bus.add(listener);
    }
    
    public void removeListener(T listener){
        this.bus.remove(listener);
    }
    
    public void notify(BaseEvent event){
        for(T listener : this.bus){
            if (event instanceof TickEvent && listener instanceof ITickListener) {
                ((ITickListener) listener).onTick((TickEvent) event);
            } else if (event instanceof RenderEvent && listener instanceof IRenderListener) {
                ((IRenderListener) listener).onRender((RenderEvent) event);
            }
        }
    }
    
    public ArrayList<T> getBus(){return bus;}
}
