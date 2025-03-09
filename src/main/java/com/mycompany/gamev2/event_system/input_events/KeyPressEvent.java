/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system.input_events;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;

/**
 *
 * @author J.A
 */
public class KeyPressEvent extends BaseEvent {
    private int keyCode;

    public KeyPressEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
