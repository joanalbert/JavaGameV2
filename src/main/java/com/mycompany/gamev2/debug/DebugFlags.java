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
    
    private boolean show_debug_FPS = false;
    private boolean show_level_grids = false;
    private boolean debug_mode = false;
    
    private static final class Holder{
        static final DebugFlags INSTANCE = new DebugFlags();
    }
    
    public static DebugFlags getInstance(){
        return Holder.INSTANCE;
    }
    
    
    private DebugFlags(){
        EventManager.getInstance().subscribe(this, IInputListener.class);
    }
    
    
    @Override
    public void onEventReceived(BaseEvent event) {
        
                
        if(event instanceof KeyPressEvent){
            KeyPressEvent e = (KeyPressEvent) event;
            int code = e.getKeyCode();
            
            switch(code){
                case KeyEvent.VK_P:
                        show_debug_FPS = !show_debug_FPS;
                        show_level_grids = !show_level_grids;
                        debug_mode = !debug_mode;
                    break;
            }
            
            
        }
    }
    
   
    public boolean getShow_debug_FPS(){return show_debug_FPS;}
    public boolean getShow_level_grids(){return show_level_grids;}

    public boolean isDebug_mode() {
        return debug_mode;
    }

    
    
   
}
