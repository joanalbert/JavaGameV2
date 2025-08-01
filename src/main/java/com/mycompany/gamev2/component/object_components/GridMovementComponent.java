/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components;

import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.exceptions.NonGridLevelException;
import com.mycompany.gamev2.exceptions.NullLevelException;
import com.mycompany.gamev2.gamemath.Utils;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.levels.LevelManager;
import com.mycompany.gamev2.levels.grid.GridLevelBase;

/**
 *
 * @author J.A
 */
public class GridMovementComponent extends MovementComponent{
    
    private boolean isMoving = false;
    private LevelGridComponent grid_component;
    
    public GridMovementComponent(GameObject owner) throws NonGridLevelException, NoSuchLevelComponentException, NullLevelException {
        super(owner); //call super constructor, sets a reference to the owner's transform component
        
        //get the current level's LevelGridComponent
        GridLevelBase current_level = LevelManager.getInstance().getCurrentGridLevel();
        LevelGridComponent grid = current_level.getComponent(LevelGridComponent.class);
        if(grid != null) this.grid_component = grid; 
        else throw new NoSuchLevelComponentException("Couldn't retrieve the following component: LevelGridComponent from GridLevelBase");
    }
   
        
    private Vector3 screenLoc_to_GridCoords(Vector3 loc ){
        int tile_size = grid_component.getTileSize();
        Vector3 grid_coords = new Vector3(loc.getX() / tile_size,
                                          loc.getY() / tile_size, 0);
        return grid_coords;
    }
    
    private Vector3 GridCoords_to_ScreenLoc(Vector3 grid_coords){
        int tile_size = grid_component.getTileSize();
        Vector3 screen_loc = new Vector3(grid_coords.getX() * tile_size,
                                         grid_coords.getY() * tile_size, 0);
        return screen_loc;
    }

    private void snapToGridCoords(Vector3 coords){
        //catch exception
        if(owner_transform == null){
            System.out.println("GridMovementComponent can't snap owner to grid: owner transform is not set");
            return;
        } 
        
        Vector3 new_screen_loc = this.GridCoords_to_ScreenLoc(coords);
        this.owner_transform.setLocation(new_screen_loc);
    }
    
   
    public Vector3 ensure_grid_alignment(){
        if (owner_transform == null) {
            System.out.println("GridMovementComponent cannot ensure grid alignment: owner transform is not set");
            return null;
        }
        
        //calculate starting tile coordinates in the grid
        Vector3 currentPos = owner_transform.getLocation();
        
        //we convert screen location to grid coordinates
        Vector3 grid_coords = this.screenLoc_to_GridCoords(currentPos);
       
        //ensure the owner remains grid-aligned
        if(!Utils.isInteger(grid_coords.getX()) || !Utils.isInteger(grid_coords.getY())){
           System.out.println("GridMovementComponent snapping owner to grid");
           Vector3 corrected_grid_coords = grid_coords.getRoundComponents();
           this.snapToGridCoords(corrected_grid_coords);
           
           return corrected_grid_coords;
        }
        
        return grid_coords;
    }
    
    public void applyMovement(Vector3 vel, double deltaTime){
        
        //catch exception
        if(owner_transform == null){
            System.out.println("Cant apply movement: owner transform is not set");
            return;
        } 
        
        //not valid direction or the previous movement hasn't completed yet
        if(!vel.isCardinalDirection() || this.isMoving) return; 
                
                
        //maintain grid-alignment
        Vector3 grid_coords = this.ensure_grid_alignment();
        if(grid_coords == null){
            System.out.println("Cant apply movement: owner transform is not set");
            return;
        } 
       
        //we only move 1 tile at a time
        Vector3 dir = vel.normalize();
                       
        //calculatte target grid pos
        Vector3 target_grid_coords = grid_coords.plus(dir);
        
        // Check if target is within bounds
        if (target_grid_coords.getX() < 0 || target_grid_coords.getX() >= grid_component.getTileWidth() ||
            target_grid_coords.getY() < 0 || target_grid_coords.getY() >= grid_component.getTileHeight()) {
            System.out.println("Movement blocked: Target tile out of bounds");
            return;
        }
        
        //HERE WE DOULD CHECK FOR COLLISSIONS, BUT NOT YET
        
        
        // Set moving flag to prevent new moves until this one completes
        isMoving = true;

        // Move to the target grid position (snap to grid) IN THE FUTURE, WE'LL LERP POSITION
        int tile_size = grid_component.getTileSize();
        Vector3 newLocation = this.GridCoords_to_ScreenLoc(target_grid_coords);
        owner_transform.setLocation(newLocation);

        // Reset moving flag (for now, assume instant movement; see Step 4 for smooth movement)
        isMoving = false;
    }
}
