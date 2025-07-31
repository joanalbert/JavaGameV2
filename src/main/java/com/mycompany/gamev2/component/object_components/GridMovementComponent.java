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
        super(owner);
        
        GridLevelBase current_level = LevelManager.getInstance().getCurrentGridLevel();
        LevelGridComponent grid = current_level.getComponent(LevelGridComponent.class);
        if(grid != null) this.grid_component = grid; 
        else throw new NoSuchLevelComponentException("Couldn't retrieve the following component: LevelGridComponent from GridLevelBase");
    }
   
       
    public void applyMovement(Vector3 vel, double deltaTime){
        
        //catch exception
        if(transform == null){
            System.out.println("Cant apply movement: owner transform is not set");
            return;
        } 
        
        //not valid direction or the previous movement hasn't completed yet
        if(!vel.isCardinalDirection() || this.isMoving) return; 
        


        
        
        Vector3 location    = this.transform.getLocation();
        Vector3 newLocation = location.plus(vel.getScaled(this.walk_speed * deltaTime));
        transform.setLocation(newLocation);
    }
}
