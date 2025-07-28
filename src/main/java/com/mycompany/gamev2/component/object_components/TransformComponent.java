/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components;

import com.mycompany.gamev2.gamemath.Transform;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.GameObject;

/**
 *
 * @author J.A
 */
public class TransformComponent<T extends GameObject> extends ObjectComponent {
 
    private Transform transform;    
    
    public TransformComponent(T owner){
        super(owner);
        this.transform = new Transform();
    }
        
    public TransformComponent(T owner, Vector3 l, Vector3 s, Vector3 r){
        super(owner);
        this.transform = new Transform(l, s, r);
    }
    
    //component operations
    
    
     //accessors
    public Vector3 getLocation() {
        return this.transform.getLocation();
    }

    public void setLocation(Vector3 location) {
        this.transform.setLocation(location);
    }

    public Vector3 getScale() {
        return this.transform.getScale();
    }

    public void setScale(Vector3 scale) {
        this.transform.setScale(scale);
    }

    public Vector3 getRotation() {
        return this.transform.getRotation();
    }

    public void setRotation(Vector3 rotation) {
        this.transform.setRotation(rotation);
    } 
}
