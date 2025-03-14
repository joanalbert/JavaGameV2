package com.mycompany.gamev2.event_system.input_events;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import java.util.HashMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

/**
 *
 * @author J.A
 */
public class KeyReleaseEvent extends BaseEvent{
    private int keyCode;
    private HashMap<Integer, Boolean> keystates;

       
    public KeyReleaseEvent(int pressedKeyCode, HashMap<Integer, Boolean> keystates){
        this.keystates = keystates;
        this.keyCode = pressedKeyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
    
    public HashMap<Integer, Boolean> getKeyStates(){
        return this.keystates;
    }
}
