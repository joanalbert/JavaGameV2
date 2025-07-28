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
public class MovementComponent<T extends GameObject> extends ObjectComponent {
    
    protected TransformComponent transform;
    protected double walk_speed = 0;
    protected Vector3 direction;
    
    public MovementComponent(T owner){
        super(owner);
        transform = owner.getComponent(TransformComponent.class);
    }
    
    public TransformComponent getTransform(){return this.transform;}
    public double getWalkSpeed(){return this.walk_speed;}
    public Vector3 getDirection(){return this.direction;}
    
    public void setWalkSpeed(double newSpeed){
        try{
            if(newSpeed > -1) this.walk_speed = newSpeed;
            else throw new Exception("walk speed cant be negative");
        } 
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    
    public void applyMovement(Vector3 vel, double deltaTime){
        Vector3 location    = this.transform.getLocation();
        Vector3 newLocation = location.plus(vel.getScaled(this.walk_speed * deltaTime));
        transform.setLocation(newLocation);
    }
}
