/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels.grid;

import com.mycompany.gamev2.component.level.grid.LevelGridComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gameobjects.characters.PlayerCharacter;

/**
 *
 * @author J.A
 */
public class GridLevel_01 extends GridLevelBase{
    
     public GridLevel_01(){
        super("GridLevel_01");        
        ComponentSetup();
    }

    @Override
    protected void configure_grid_component() {
        LevelGridComponent grid = getComponent(LevelGridComponent.class);
        if(grid == null) return;
        
        grid.config_height(10)
            .config_width(10)
            .config_tile_size(32).construct();
    }

   
     
    @Override
    public void level_windup() {
        System.out.println("GRID WINDUP");
        this.configure_grid_component();
        
        PlayerCharacter player = new PlayerCharacter();
        addGameObject(player);
    }

    @Override
    protected void tick(TickEvent e) {
        
    }

    @Override
    protected void render(RenderEvent e) {
        LevelGridComponent grid = getComponent(LevelGridComponent.class);
        if(grid == null) return;
        grid.render(e);
    }
     
     
         
}
