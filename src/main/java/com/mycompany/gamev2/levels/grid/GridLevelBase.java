/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels.grid;


import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.levels.BaseLevel;

/**
 *
 * @author J.A
 */
public abstract class GridLevelBase extends BaseLevel {
    
    private String json_name;
    
    public GridLevelBase(String name, String json_name){
        super(name);        
        
        this.json_name = json_name;
        
        //components
        ComponentSetup();
    }

    @Override
    public void ComponentSetup() {
        LevelGridComponent grid = new LevelGridComponent(this);
        addComponent(grid.getClass(), grid);
    }

   
    public String getJsonName(){return this.json_name;}
    protected abstract void configure_grid_component();
}
