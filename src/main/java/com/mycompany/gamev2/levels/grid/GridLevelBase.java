/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels.grid;

import com.mycompany.gamev2.component.level_components.camera_component.LevelCameraComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.levels.BaseLevel;

/**
 *
 * @author J.A
 */
public abstract class GridLevelBase extends BaseLevel implements IGameUpdateListener {
    public GridLevelBase(String name){
        super(name);        
        ComponentSetup();
        
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
    }

    @Override
    public void ComponentSetup() {
        LevelGridComponent grid = new LevelGridComponent(this);
        addComponent(grid.getClass(), grid);
    }
    
    

    @Override
    public void onEventReceived(BaseEvent event) {
        super.onEventReceived(event); 
        
        if(!this.isActive()) return;
        
              
        if (event instanceof TickEvent){
            tick((TickEvent) event);
        }
        else if (event instanceof RenderEvent){
            render((RenderEvent) event);
        }
    }
   
    
    protected abstract void tick(TickEvent e);
    protected abstract void render(RenderEvent e);
    protected abstract void configure_grid_component();
}
