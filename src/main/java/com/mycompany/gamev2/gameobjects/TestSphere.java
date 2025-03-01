/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects;

import com.mycompany.gamev2.component.object.TransformComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gamemath.Vector3;


import java.awt.Color;
import java.awt.Graphics2D;


/**
 *
 * @author J.A
 */
public class TestSphere extends GameObject {
    
    
    private Vector3 vel;
    private double speed;
    private double radius;
    private Color color;
    
    public TestSphere(Color c, double radius, Vector3 position, Vector3 vel, double speed){
        super();
        
        TransformComponent transform = this.getComponent(TransformComponent.class);
        if(transform != null) transform.setLocation(position);
        
        this.color = c;
        this.radius = radius;
        this.vel = vel.normalize();
        this.speed = speed;
        
        
    }
    
    @Override
    public void ComponentSetup(){
        super.ComponentSetup();        
    }

    @Override
    public void onRender(RenderEvent event) {
        //System.out.println("render sphere");
        
    }

    @Override
    public void onTick(TickEvent e) {
        System.out.println("tick sphere");
    }


    
}

