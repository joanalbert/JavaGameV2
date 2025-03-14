/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.controllers;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;

/**
 *
 * @author J.A
 */
public class PlayerController extends BaseController {

    @Override
    public void onEventReceived(BaseEvent event) {
        
        if(event instanceof KeyPressEvent){
            System.out.println("player controller Nº:"+this.id);
            KeyPressEvent e = (KeyPressEvent) event;
            
            
            
        } 
        
        
    }
    
    
}
