/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects.characters;

import com.mycompany.gamev2.component.object.TransformComponent;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.input_system.InputActions.IA_Look;
import com.mycompany.gamev2.input_system.InputActions.IA_Shoot;
import com.mycompany.gamev2.input_system.InputActions.IA_Walk;
import com.mycompany.gamev2.input_system.InputBinding;
import com.mycompany.gamev2.input_system.InputContexts.InputContext;
import com.mycompany.gamev2.input_system.InputContexts.PlayerCharacter_InputContext;
import com.mycompany.gamev2.input_system.InputManager;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author J.A
 */
public class PlayerCharacter extends Character implements IInputListener {
    
    private Vector3 vel;
    private double speed;
    private double radius;
    private Color color;

    private IA_Walk ia_walk;
    private IA_Shoot ia_shoot;
    private IA_Look ia_look;
    
    public PlayerCharacter(){
        super();
        TransformComponent transform = this.getComponent(TransformComponent.class);
        if(transform != null) transform.setLocation(new Vector3(50,50,0));
        
        this.color = Color.RED;
        this.radius = 70;
        this.vel = Vector3.ZERO;
        this.speed = 220;
        
        input_setup();
    }
    
    
    
    @Override
    protected boolean setInputContext(InputContext ctx){
        super.setInputContext(ctx);
        
        if(ctx == null) return false;
        return true;
        
    }
    
    @Override
    protected void input_setup(){
        
        //THIS SHOULD BE HANDLED BY THE CONTROLLER
        
        // Set up input context
        PlayerCharacter_InputContext context = new PlayerCharacter_InputContext();
        setInputContext(context);
        InputManager.getInstance().addContext(context);

        // Find Walk action and set callback
        ia_walk = (IA_Walk) context.getBindings().stream()
                .map(InputBinding::getAction)
                .filter(action -> action instanceof IA_Walk)
                .findFirst()
                .orElse(new IA_Walk());

        ia_walk.setOnTriggered(action -> {
            if (action instanceof IA_Walk walkAction) {
                Vector3 v = new Vector3(walkAction.getAxisValues()[0],
                                        walkAction.getAxisValues()[1], 0); 
                this.vel = v.normalize(); // Update velocity based on input
            }
        });
        
        
        ia_shoot = (IA_Shoot) context.getBindings().stream()
                .map(InputBinding::getAction)
                .filter(action -> action instanceof IA_Shoot)
                .findFirst()
                .orElse(new IA_Shoot());
        
        ia_shoot.setOnTriggered(action -> {
            if (action instanceof IA_Shoot shootAction) {
                //System.out.println("SHOOTING");
                
                
            }
        });
        
        ia_look = (IA_Look) context.getBindings().stream()
                .map(InputBinding::getAction)
                .filter(action -> action instanceof IA_Look)
                .findFirst()
                .orElse(new IA_Look());
        
        ia_look.setOnTriggered(action -> {
            if(action instanceof IA_Look lookAction){
                double[] axis_vals = lookAction.getAxisValues();
                System.out.println("LOOK");
                System.out.println(axis_vals[0]);
                System.out.println(axis_vals[1]);
                System.out.println("LOOK");
            }
        });

        EventManager.getInstance().subscribe(this, IInputListener.class);
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
        Vector3 location = getObjectLocation();
        Vector3 usable_vel = vel.getScaled(speed * event.getDeltaSeconds());
        setObjectLocation(location.plus(usable_vel));
    }
}
