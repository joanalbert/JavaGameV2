/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.providers;

import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV2;
import com.mycompany.gamev2.exceptions.NoSuchGridTileException;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.exceptions.NonGridLevelException;
import com.mycompany.gamev2.exceptions.NullLevelException;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.interfaces.providers.IGridProvider;
import com.mycompany.gamev2.levels.LevelManager;
import com.mycompany.gamev2.levels.grid.GridLevelBase;
import com.mycompany.gamev2.providers.base.BaseProvider;

/**
 *
 * @author J.A
 */
public class LevelGridProvider extends BaseProvider implements IGridProvider{
    
    private LevelGridComponent grid;
    
    public LevelGridProvider() throws NonGridLevelException, NoSuchLevelComponentException, NullLevelException{
         // Get the current level's LevelGridComponent and cache it
        GridLevelBase current_level = LevelManager.getInstance().getCurrentGridLevel();
        LevelGridComponent grid = current_level.getComponent(LevelGridComponent.class);
        if (grid != null) this.grid = grid;
        else throw new NoSuchLevelComponentException("Couldn't retrieve the following component: LevelGridComponent from GridLevelBase");
    }

    @Override
    public LevelGridTileV2.COLISION_TYPE colision_at_tile(Vector3 grid_coords) {
        try{return this.grid.getColisionForTile(grid_coords);}
        catch(NoSuchGridTileException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
      
    
    @Override
    public Vector3 grid_to_screen(Vector3 grid_coords){
        return this.grid.GridCoords_to_ScreenLoc(grid_coords);
    }

    @Override
    public Vector3 screen_to_grid(Vector3 grid_coords) {
        return this.grid.ScreenLoc_to_GridCoords(grid_coords);
    }

    
    
    @Override
    public int height_in_tiles() {
        return this.grid.getTileHeight();
    }

    @Override
    public int width_in_tiles() {
        return this.grid.getTileWidth();
    }
    
    
    
   
}
