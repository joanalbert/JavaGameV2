/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects.characters;

import com.mycompany.gamev2.component.object.TransformComponent;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.input_system.controllers.BaseController;
import com.mycompany.gamev2.input_system.enums.EWalkingAction;
import com.mycompany.gamev2.input_system.mappings.ActionMapping;
import com.mycompany.gamev2.input_system.mappings.PlayerMapping_001;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author J.A
 */
public class PlayerCharacter extends Character {
    
    private Vector3 vel;
    private double speed;
    private double radius;
    private Color color;

    
    
    public PlayerCharacter(){
        TransformComponent transform = this.getComponent(TransformComponent.class);
        if(transform != null) transform.setLocation(new Vector3(50,50,0));
        
        this.color = Color.RED;
        this.radius = 70;
        this.vel = Vector3.ZERO;
        this.speed = 0;
    }
    
    
    public void addController(BaseController c){
        System.out.println("AAAAA");
        if(c != null){
            setController(c);
        }
    }

    @Override
    protected void registerActions() {
        ActionMapping activeMapping = getController().getMapping();
        
        if (activeMapping instanceof PlayerMapping_001) {
            actionFunctions.put(EWalkingAction.MOVE_LEFT, this::moveLeft);
            actionFunctions.put(EWalkingAction.MOVE_RIGHT, this::moveRight);
            actionFunctions.put(EWalkingAction.JUMP, this::jump);
            actionFunctions.put(EWalkingAction.SOME_ACTION, this::someAction);
        }
    }
    
    public void someAction() {
        this.color = Color.BLUE;
    }

    private void moveLeft() {
        System.out.println("Player moves left!");
        vel = Vector3.LEFT;
    }

    private void moveRight() {
        System.out.println("Player moves right!");
        vel = Vector3.RIGHT;
    }

    private void jump() {
        System.out.println("Player jumps!");
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
        
    }
}
