/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects;

import com.mycompany.gamev2.component.object.TransformComponent;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.window.MyWindow;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


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
    public void onEventReceived(BaseEvent event) {
        super.onEventReceived(event);
 
        if(!this.isActive) return;
        
        if (event instanceof TickEvent){
            tick((TickEvent) event);
        }
        else if (event instanceof RenderEvent){
            render((RenderEvent) event);
        }
        else if (event instanceof KeyPressEvent){
            return;            
        }
    }


    private void render(RenderEvent event){
        if(!this.isActive) return;
        
        //System.out.println("render sphere");
        Graphics2D g = event.getGraphics();
        
        Vector3 location = getObjectLocation();
        
        double half_radius = this.radius/2;
        Vector3 drawing_coordinates = location.minus(new Vector3(half_radius, half_radius, half_radius));
        
        g.setColor(this.color);
        g.fill(new Ellipse2D.Double(drawing_coordinates.getX(), drawing_coordinates.getY(), this.radius, this.radius));
    }
    
    private void tick(TickEvent event){
        //System.out.println("tick sphere");
        Vector3 location = getObjectLocation();
        
        if(location.getX() + this.radius >= MyWindow.DIMENSIONS.width || location.getX() < this.radius ) this.vel = this.vel.getScaled(-1);
        
        Vector3 usable_vel = this.vel.getScaled(this.speed).getScaled(event.getDeltaSeconds());
        Vector3 newpos = location.plus(usable_vel);
        setObjectLocation(newpos);
    }
}

