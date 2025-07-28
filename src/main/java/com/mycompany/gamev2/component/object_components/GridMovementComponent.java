/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components;

import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.GameObject;

/**
 *
 * @author J.A
 */
public class GridMovementComponent<T extends GameObject> extends MovementComponent{
    
    public GridMovementComponent(T owner){
        super(owner);
    }
       
    public void applyMovement(Vector3 vel, double deltaTime){
        
        try {
            if(transform == null) throw new Exception("Cant apply movement: owner transform is not set");
            
            Vector3 location    = this.transform.getLocation();
            Vector3 newLocation = location.plus(vel.getScaled(this.walk_speed * deltaTime));
            transform.setLocation(newLocation);
            
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
}
