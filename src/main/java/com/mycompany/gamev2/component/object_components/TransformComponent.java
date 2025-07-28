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
public class TransformComponent<T extends GameObject> extends ObjectComponent {
 
    private Vector3 location;
    private Vector3 rotation;
    private Vector3 scale;
    
    
    public TransformComponent(T owner){
        super(owner);
        this.scale = new Vector3(1,1,1);
        this.location = Vector3.ZERO;
        this.rotation = Vector3.ZERO;
    }
        
    public TransformComponent(T owner, Vector3 l, Vector3 s, Vector3 r){
        super(owner);
        this.scale = s;
        this.location = l;
        this.rotation = r;
    }
    
    public Vector3 getLocation() {
        return location;
    }

    public void setLocation(Vector3 location) {
        this.location = location;
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }
    
    
    
}
