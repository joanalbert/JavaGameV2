/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects.characters;

import com.mycompany.gamev2.component.level_components.camera_component.GridLevelCameraComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.component.object_components.GridMovementComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.exceptions.NonGridLevelException;
import com.mycompany.gamev2.exceptions.NullLevelException;
import com.mycompany.gamev2.exceptions.NullOwnerTransformException;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.input_system.InputActions.IA_Walk;
import com.mycompany.gamev2.input_system.InputBinding;
import com.mycompany.gamev2.input_system.InputContexts.GridPlayerCharacter_InputContext;
import com.mycompany.gamev2.input_system.InputManager;
import com.mycompany.gamev2.levels.LevelManager;
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
        System.out.println("GridPlayerCharacter2D.ComponentSetup");
        super.ComponentSetup(); 
        
        //initialize character specific components
        
        //movement
        try{
            this.movement = new GridMovementComponent(this); //this line could throw an exception
            this.addComponent(GridMovementComponent.class, this.movement);
            this.movement.setWalkSpeed(200);
            this.movement.try_ensure_grid_alignment();
        }
        catch(NoSuchLevelComponentException | NonGridLevelException | NullLevelException | NullOwnerTransformException e){
            System.out.println(e.getMessage());
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
        this.movement.initiate_movement(v, walkAction.getDeltaTime());
    }

    
    //for grid based characters we make sure that setObjectLocation always snaps to the grid
    @Override
    public void setObjectLocation(Vector3 newLocation) {
        super.setObjectLocation(newLocation);
        try{this.movement.try_ensure_grid_alignment();}
        catch(NullOwnerTransformException e){System.out.println(e.getMessage());}
    }
    
    

    @Override
    protected void tick(TickEvent event) {
        //we do nothing here (we override super behavior)
        boolean is  = this.movement.getIsMoving();
        boolean was = this.movement.was_moving;
        
        
    }

    
    
    @Override
    protected void render(RenderEvent event) {
        if(!this.isActive) return;
        
        try {
            Graphics2D g = event.getGraphics();
            Vector3 location = getObjectLocation();

            LevelGridComponent grid = LevelManager.getInstance().getCurrentLevel().getComponent(LevelGridComponent.class);
            if(grid != null && grid.getIsCameraFollow()){
                GridLevelCameraComponent cam  = LevelManager.getInstance().getCurrentLevel().getComponent(GridLevelCameraComponent.class);
                Vector3 offset = cam.getCamOffsets();
                location = location.minus(offset);
            }

            GridMovementComponent movement = this.getComponent(GridMovementComponent.class);

            //g.setColor(this.color);
            if(movement.getIsMoving()) g.setColor(this.color);
            else g.setColor(Color.BLUE);

            g.fillRect((int)location.getX(), (int)location.getY(), 32, 32);
            
        } catch (NoSuchLevelComponentException ex){System.out.println(ex.getMessage());}
    }
    
}
