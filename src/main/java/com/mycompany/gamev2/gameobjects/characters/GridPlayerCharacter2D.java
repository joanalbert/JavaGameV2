/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects.characters;

import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.component.object_components.GridMovementComponent;
import com.mycompany.gamev2.component.object_components.SpriteComponent.GridCharacterSpriteComponent;
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
import com.mycompany.gamev2.interfaces.characters.ECharacter;
import com.mycompany.gamev2.providers.LevelGridProvider;
import java.awt.Color;

/**
 *
 * @author J.A
 */
public class GridPlayerCharacter2D extends PlayerCharacter {
 
    private IA_Walk ia_walk;
    
    private GridMovementComponent movement;
    private GridCharacterSpriteComponent sprite;
    
    public GridPlayerCharacter2D(){
        this.color = Color.YELLOW;
    }
    
    @Override
    public void ComponentSetup() {
        System.out.println("GridPlayerCharacter2D.ComponentSetup");
        super.ComponentSetup(); 
        
        //initialize character specific components
        
        //components
        try{
            this.movement = new GridMovementComponent(this, new LevelGridProvider()); //this line could throw an exception
            this.addComponent(GridMovementComponent.class, this.movement);
            this.movement.setWalkSpeed(200);
            this.movement.try_ensure_grid_alignment();
            
            this.sprite = new GridCharacterSpriteComponent(this);
            this.sprite.configure(ECharacter.BRENDAN);
            this.addComponent(GridCharacterSpriteComponent.class, this.sprite);
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
                this.process_IA_Walk(ia_walk);
            }
        });
    }
    
    
   
    private void process_IA_Walk(IA_Walk walkAction){
        Vector3 v = new Vector3(walkAction.getAxisValues()[0], walkAction.getAxisValues()[1], 0);
       
        //double tap_threshold = 0.225d * 0.48d;
        //double tap_threshold = 0.225d * 0.62d;
        double tap_threshold = this.movement.move_duration * 0.62d;
        double f = GameLoopV2.getInstance().getFrames();
        double last_heldTime = walkAction.getLast_heldTime();
        double heldTime = walkAction.getHeldTime();
                
        //we use previous held time, because we can only infer a 'tap' happened, AFTER it's happened, i.e after the input is 'released'
        if(last_heldTime > 0d && last_heldTime < tap_threshold) {
            
                        
            Vector3 new_facing = walkAction.getLast_nonzero_axis2d_input().normalize();
            Vector3 old_facing = this.movement.getFacing().normalize();
            boolean same = new_facing.equals(old_facing);
            
            //System.out.println("new: "+new_facing.toString());
            //System.out.println("old: "+old_facing.toString());
            
            if(same) {
                this.move(old_facing, walkAction.getDeltaTime());
                //System.out.println("TAP: move "+f);
            }
            else {
                this.face(new_facing);
                //System.out.println("TAP: face "+f);
            }
            
          
        }
        
        
        if(heldTime > tap_threshold){
            //System.out.println("no tap "+f);
            this.move(v, walkAction.getDeltaTime());
        }
            
    }
    
    private void face(Vector3 v){ 
        if(this.movement == null || this.movement.getIs_moving()) return;
        this.movement.setFacing(v);
    }
    
    private void move(Vector3 v, double deltaTime){        
        if(this.movement == null) return; //movement component not initialized
             
        //delegate movement to MovementComponent
        this.movement.initiate_movement(v, deltaTime);
    }

    
    //for grid based characters we make sure that setObjectLocation always snaps to the grid
    @Override
    public void setObjectLocation(Vector3 newLocation) {
        super.setObjectLocation(newLocation);
        try{this.movement.try_ensure_grid_alignment();}
        catch(NullOwnerTransformException e){System.out.println(e.getMessage());}
    }
    
    

    @Override
    public void tick(TickEvent event) {
        super.tick(event);
        //we do nothing here (we override super behavior)
        
        
        
    }

    
    
    @Override
    public void render(RenderEvent event) {
        //super.render(event);
        if(!this.isActive) return;
        
        this.sprite.render(event);
        
        /*try {
            
            if(!DebugFlags.getInstance().getShow_level_grids()) return;
                        
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
        } catch (NoSuchLevelComponentException ex){System.out.println(ex.getMessage());}*/
    }
    
}
