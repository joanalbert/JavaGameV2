/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels.grid;

import com.mycompany.gamev2.component.level_components.camera_component.GridLevelCameraComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.gameobjects.characters.GridPlayerCharacter2D;

/**
 *
 * @author J.A
 */
public class GridLevel_01 extends GridLevelBase{
        
    public GridLevel_01(){
        super("GridLevel_01_docks", "map_layouts/map_fail.json");
        ComponentSetup();
    }

    @Override
    protected void configure_grid_component() {
        try{
            LevelGridComponent grid = getComponent(LevelGridComponent.class);
            if(grid == null) return;

            grid.config_height(20)
                .config_width(50)
                .config_tile_size(32)
                .config_viewport_culling(true)
                .config_override_json_dimensions(false)
                .config_camera_follow(true)
                .construct_fromJSON(this.getJsonName());
            
        } catch (NoSuchLevelComponentException ex){System.out.println(ex.getMessage());}
    }

    
    @Override 
    public void ComponentSetup() {
        super.ComponentSetup();
        GridLevelCameraComponent level_camera = new GridLevelCameraComponent(this);
        addComponent(level_camera.getClass(), level_camera);
    }
   
     
    @Override
    public void level_windup() {
        super.level_windup();
        System.out.println(this.getName()+" WINDUP");
        
        //initialize grid component config
        this.configure_grid_component();
        
        //GAME OBJECT
        GridPlayerCharacter2D player = new GridPlayerCharacter2D();
        addGameObject(player);
        
        
        //LEVEL COMPONENTS
        try{
            GridLevelCameraComponent cam = getComponent(GridLevelCameraComponent.class);
            if(cam != null){
                cam.setTarget(player);
            }
        } catch (NoSuchLevelComponentException e){System.out.println(e.getMessage());}
    }
    

    
         
}
