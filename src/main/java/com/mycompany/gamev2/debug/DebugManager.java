/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.debug;

import com.mycompany.gamev2.debug.menu.DebugMenu;
import com.mycompany.gamev2.debug.menu.DebugPage;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;
import com.mycompany.gamev2.providers.EventProvider;
import java.awt.event.KeyEvent;

/**
 *
 * @author J.A
 */
public class DebugManager implements IGameUpdateListener, IInputListener {
    
    //class fields
    private EventProvider event_provider;
    private DebugMenu menu;
    private boolean active;
    
    //<editor-fold desc="singleton stuff">
    private static final class Holder {
        static final DebugManager INSTANCE = new DebugManager(new EventProvider());
    }
    
    public static DebugManager getInstance(){return Holder.INSTANCE;}
    
    private DebugManager(EventProvider e_provider){
        this.menu = new DebugMenu();
        this.event_provider = e_provider;
        this.active = false;
        this.event_setup();
        this.menu_setup();
    }
    //</editor-fold>
    
    
    private void event_setup(){
        this.event_provider.subscribe(this, IInputListener.class);
        this.event_provider.subscribe(this, IGameUpdateListener.class);
    }
    
    private void menu_setup(){
        DebugPage events_page = new DebugPage("[Event_System]");
        DebugPage input_page = new DebugPage("[Input_System]");
        DebugPage level_page = new DebugPage("[Current_Level]");
        
        this.menu.add_page(events_page);
        this.menu.add_page(input_page);
        this.menu.add_page(level_page);
    }

    @Override
    public void onEventReceived(BaseEvent event) {
        
        if(event instanceof TickEvent te) this.tick(te);
        else if (event instanceof RenderEvent re) this.render(re);
        else if (event instanceof KeyPressEvent ke){
            
            //toggle behavior
            if(ke.getKeyCode() == KeyEvent.VK_P){
                this.active = !this.active;
                return;
            }
            
            this.handle_keys(ke);
        }
    }
    
    private void tick(TickEvent event){
        if(!this.active) return;
        
    }
    
    private void render(RenderEvent event){
        if(!this.active) return;
        this.menu.render(event);
    }
    
    
    private void handle_keys(KeyPressEvent event){
        if(!this.active) return;
          
        int keycode = event.getKeyCode();
              
        //navigating the menu
        switch(keycode){
            case KeyEvent.VK_LEFT:
                this.menu.left();
                break;               
            case KeyEvent.VK_RIGHT:
                this.menu.right();
                break;
        }
        
    }
    
    
}
