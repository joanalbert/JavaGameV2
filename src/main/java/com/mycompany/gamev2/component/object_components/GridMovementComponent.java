/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components;

import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.exceptions.NonGridLevelException;
import com.mycompany.gamev2.exceptions.NullLevelException;
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
   
       
    public void applyMovement(Vector3 vel, double deltaTime){
        
        //catch exception
        if(owner_transform == null){
            System.out.println("Cant apply movement: owner transform is not set");
            return;
        } 
        
        //not valid direction or the previous movement hasn't completed yet
        if(!vel.isCardinalDirection() || this.isMoving) return; 
        
        //we only move 1 tile at a time
        Vector3 dir = vel.normalize();
        
        
        //calculate starting tile coordinates in the grid
        Vector3 currentPos = owner_transform.getLocation();
        int tile_size = grid_component.getTileSize();
        Vector3 currentGridPos = new Vector3(currentPos.getX() / tile_size, currentPos.getY() / tile_size, 0);
        
        //calculatte target grid pos
        Vector3 targetGridPos = currentGridPos.plus(dir);
        
        // Check if target is within bounds
        if (targetGridPos.getX() < 0 || targetGridPos.getX() >= grid_component.getTileWidth() ||
            targetGridPos.getY() < 0 || targetGridPos.getY() >= grid_component.getTileHeight()) {
            System.out.println("Movement blocked: Target tile out of bounds");
            return;
        }
        
        //HERE WE DOULD CHECK FOR COLLISSIONS, BUT NOT YET
        
        
        // Set moving flag to prevent new moves until this one completes
        isMoving = true;

        // Move to the target grid position (snap to grid) IN THE FUTURE, WE'LL LERP POSITION
        Vector3 newLocation = new Vector3(targetGridPos.getX() * tile_size, targetGridPos.getY() * tile_size, 0);
        owner_transform.setLocation(newLocation);

        // Reset moving flag (for now, assume instant movement; see Step 4 for smooth movement)
        isMoving = false;
    }
}
