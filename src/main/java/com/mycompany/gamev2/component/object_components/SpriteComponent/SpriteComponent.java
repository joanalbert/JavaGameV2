/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components.SpriteComponent;

import com.mycompany.gamev2.component.object_components.MovementComponent;
import com.mycompany.gamev2.component.object_components.ObjectComponent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.GameObject;

/**
 *
 * @author J.A
 */
public class SpriteComponent<T extends GameObject> extends ObjectComponent{
    
    protected String image;
    
    protected Vector3 owner_facing_direction;
    protected boolean owner_is_moving;
    
    protected MovementComponent owner_movement;
        
    public SpriteComponent(T owner, String image){
        super(owner);
        this.image = image;
    }

    
}
