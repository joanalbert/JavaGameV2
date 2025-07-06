/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels.grid;

import com.mycompany.gamev2.component.level_components.camera_component.LevelCameraComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gameobjects.characters.PlayerCharacter;

/**
 *
 * @author J.A
 */
public class GridLevel_02 extends GridLevelBase{
        
    public GridLevel_02(){
        super("GridLevel_02_town", "map_layouts/map_town.json");
        ComponentSetup();
    }
    
    
    @Override
    protected void configure_grid_component() {
        LevelGridComponent grid = getComponent(LevelGridComponent.class);
        if(grid == null) return;
        
        grid.config_height(20)
            .config_width(20)
            .config_tile_size(32)
            .config_viewport_culling(false)
            .construct_fromJSON(this.getJsonName());
    }

    
    @Override 
    public void ComponentSetup() {
        super.ComponentSetup();
        
        LevelCameraComponent level_camera = new LevelCameraComponent(this);
        addComponent(level_camera.getClass(), level_camera);
    }
   
     
    @Override
    public void level_windup() {
        System.out.println("GRID WINDUP");
        this.configure_grid_component();
        
        PlayerCharacter player = new PlayerCharacter();
        addGameObject(player);
        
        LevelCameraComponent cam = getComponent(LevelCameraComponent.class);
        if(cam != null) cam.setTarget(player);
    }

    @Override
    protected void tick(TickEvent e) {
        LevelCameraComponent cam = getComponent(LevelCameraComponent.class);
        if(cam != null) cam.tick(e);
    }

    @Override
    protected void render(RenderEvent e) {
        LevelGridComponent grid = getComponent(LevelGridComponent.class);
        if(grid == null) return;
        grid.render(e);
        
        
        LevelCameraComponent cam = getComponent(LevelCameraComponent.class);
        if(cam != null) cam.render(e);
    }

}
