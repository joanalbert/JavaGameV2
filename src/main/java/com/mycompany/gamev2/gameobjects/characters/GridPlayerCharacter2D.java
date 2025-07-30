/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects.characters;

import com.mycompany.gamev2.component.object_components.GridMovementComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.exceptions.NonGridLevelException;
import com.mycompany.gamev2.exceptions.NullLevelException;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.input_system.InputActions.IA_Walk;
import com.mycompany.gamev2.input_system.InputBinding;
import com.mycompany.gamev2.input_system.InputContexts.GridPlayerCharacter_InputContext;
import com.mycompany.gamev2.input_system.InputManager;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author J.A
 */
public class GridPlayerCharacter2D extends PlayerCharacter {
 
    private IA_Walk ia_walk;
    
    private GridMovementComponent movement;
    
    public GridPlayerCharacter2D(){
        this.color = Color.YELLOW;
    }
    
     @Override
    public void ComponentSetup() {
        super.ComponentSetup(); 
        
        //initialize character specific components
        
        //movement
        try{
            this.movement = new GridMovementComponent(this); //this line could throw an exception
            this.addComponent(GridMovementComponent.class, this.movement);
            this.movement.setWalkSpeed(200);
        }
        catch(NoSuchLevelComponentException | NonGridLevelException | NullLevelException e){
            System.out.println(e.getMessage());
            return;
        }
        
    }

    @Override
    protected void input_setup() {
        
        GridPlayerCharacter_InputContext context = new GridPlayerCharacter_InputContext();
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
                this.move(walkAction);
            }
        });
    }
    
    private void move(IA_Walk walkAction){
        Vector3 v = new Vector3(walkAction.getAxisValues()[0],
                                walkAction.getAxisValues()[1], 0); 
        
        if(this.movement == null) return; //movement component not initialized
        
        if(v.equals(Vector3.ZERO)) return; //no movement
        
        //delegate movement to MovementComponent
        this.movement.applyMovement(v, walkAction.getDeltaTime());
    }

    @Override
    protected void tick(TickEvent event) {
        //we do nothing here (we override super behavior)
    }

    
    
    @Override
    protected void render(RenderEvent event) {
        if(!this.isActive) return;
        
        Graphics2D g = event.getGraphics();
        Vector3 location = getObjectLocation();
        
        g.setColor(this.color);
        g.fillRect((int)location.getX(), (int)location.getY(), 32, 32);
    }
    
}
