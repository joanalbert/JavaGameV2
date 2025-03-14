/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.controllers;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.input_system.enums.EKey;
import com.mycompany.gamev2.input_system.interfaces.IAction;
import com.mycompany.gamev2.input_system.mappings.PlayerMapping_001;

/**
 *
 * @author J.A
 */
public class PlayerController extends BaseController {
    
    public PlayerController(){
        super();
        setActionMapping(new PlayerMapping_001());
    }
    

    @Override
    public void onEventReceived(BaseEvent event) {
        
        if(event instanceof KeyPressEvent){
            KeyPressEvent e = (KeyPressEvent) event;
            EKey key = EKey.fromKeyCode(e.getKeyCode());
            if (key != null && mapping != null && controlledObject != null) {
                IAction action = mapping.getActionForKey(key);
                if (action != null) {
                    controlledObject.handleAction(action);
                }
            }
        } 
        
        
    }
    
    
}
