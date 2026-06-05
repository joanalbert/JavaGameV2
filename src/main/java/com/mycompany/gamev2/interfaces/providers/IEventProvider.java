/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.gamev2.interfaces.providers;


import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IEventListener;

/**
 *
 * @author J.A
 */
public interface IEventProvider {
    public <T extends IEventListener> void post(BaseEvent event, Class<T> listenerType);
    public <T extends IEventListener> void subscribe(T listener, Class<T> listenerType);
    public <T extends IEventListener> void unsubscribe(T listener, Class<T> listenerType);
    public <T extends IEventListener> void unsubscribeAll(T listener);
}
