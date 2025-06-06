/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.debug;

import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;
import java.awt.event.KeyEvent;

/**
 *
 * @author J.A
 */
public class DebugFlags implements IInputListener{

    private static DebugFlags instance;
    
    private boolean show_debug_FPS = false;
    
    private DebugFlags(){
        EventManager.getInstance().subscribe(this, IInputListener.class);
    }
    
    public static DebugFlags getInstance(){
        if(instance == null) instance = new DebugFlags();
        return instance;
    }
    
    @Override
    public void onEventReceived(BaseEvent event) {
        
                
        if(event instanceof KeyPressEvent){
            KeyPressEvent e = (KeyPressEvent) event;
            int code = e.getKeyCode();
            
            switch(code){
                case KeyEvent.VK_P:
                        show_debug_FPS = !show_debug_FPS;
                    break;
            }
            
            
        }
    }
    
   
    public boolean getShow_debug_FPS(){return show_debug_FPS;}
}
