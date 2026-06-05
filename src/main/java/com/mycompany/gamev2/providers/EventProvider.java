/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.providers;

import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IEventListener;
import com.mycompany.gamev2.interfaces.providers.IEventProvider;
import com.mycompany.gamev2.providers.base.BaseProvider;

/**
 *
 * @author J.A
 */
public class EventProvider extends BaseProvider implements IEventProvider {
    
    private EventManager manager;
    public EventProvider(){
        this.manager = EventManager.getInstance();
    }

    @Override
    public <T extends IEventListener> void post(BaseEvent event, Class<T> listenerType) {
        this.manager.post(event, listenerType);
    }

    @Override
    public <T extends IEventListener> void subscribe(T listener, Class<T> listenerType) {
        this.manager.subscribe(listener, listenerType);
    }

    @Override
    public <T extends IEventListener> void unsubscribe(T listener, Class<T> listenerType) {
        this.manager.unsubscribe(listener, listenerType);
    }

    @Override
    public <T extends IEventListener> void unsubscribeAll(T listener) {
        this.manager.unsubscribeAll(listener);
    }
   
}
