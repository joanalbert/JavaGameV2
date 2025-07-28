/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gamemath;

/**
 *
 * @author J.A
 */
public class Transform {
    private Vector3 location;
    private Vector3 rotation;
    private Vector3 scale;
    
    public Transform(){
        this.location = new Vector3(0,0,0);
        this.rotation = new Vector3(0,0,0);
        this.scale    = new Vector3(1,1,1);
    }
    
    
    public Transform(Vector3 location){
        this.location = location;
        this.rotation = new Vector3(0,0,0);
        this.scale    = new Vector3(1,1,1);
    }
    
    public Transform(Vector3 location, Vector3 rotation){
        this.location = location;
        this.rotation = rotation;
        this.scale         = new Vector3(1,1,1);
    }
    
    public Transform(Vector3 location, Vector3 rotation, Vector3 scale){
        this.location = location;
        this.rotation = rotation;
        this.scale    = scale; 
    }
    
    
   
    //transform operations
    
    
    
    
    //accessors
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
