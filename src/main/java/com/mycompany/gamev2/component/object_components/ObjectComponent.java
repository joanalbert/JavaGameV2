/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components;

import com.mycompany.gamev2.component.BaseComponent;
import com.mycompany.gamev2.gameobjects.GameObject;

/**
 *
 * @author J.A
 */
public class ObjectComponent<T extends GameObject> extends BaseComponent {
    
    protected T owner;
    
    public ObjectComponent(T owner){
        this.owner = owner;
    }
    
    public  void setOwner(T o){
        if(o != null) this.owner = o;
    }
    
    public T getOwner(){return this.owner;}
}
