/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.gamev2.interfaces.event_listeners;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;

/**
 *
 * @author J.A
 */
public interface IEventListener {
    public void onEventReceived(BaseEvent event);
}
