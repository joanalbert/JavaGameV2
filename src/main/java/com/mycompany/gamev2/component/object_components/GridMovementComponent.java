/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components;


import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.Utils.GridMoveTimer;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV2;
import com.mycompany.gamev2.debug.DebugUtils;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.event_system.gameplay_events.CharacterStepEvent;
import com.mycompany.gamev2.event_system.gameplay_events.CharacterStepEvent.ECharacterStepSide;
import com.mycompany.gamev2.exceptions.ExceptionUtils;
import com.mycompany.gamev2.exceptions.NoSuchGridTileException;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.exceptions.NonGridLevelException;
import com.mycompany.gamev2.exceptions.NullLevelException;
import com.mycompany.gamev2.exceptions.NullOwnerTransformException;
import com.mycompany.gamev2.gamemath.Utils;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.interfaces.event_listeners.IGameplayListener;
import com.mycompany.gamev2.levels.LevelManager;
import com.mycompany.gamev2.levels.grid.GridLevelBase;
import com.mycompany.gamev2.gameobjects.characters.Character;
import com.mycompany.gamev2.interfaces.providers.IGridProvider;
import com.mycompany.gamev2.providers.LevelGridProvider;
/**
 *
 * @author J.A
 */
public class GridMovementComponent extends MovementComponent {
    
    // Movement flags
    private boolean started_moving = false;
    private boolean stopped_moving = false;
    private boolean was_moving = false;
    private boolean is_moving = false;
    private boolean is_move_completed = false;

    //step flags
    private boolean leftFired = false;
    private boolean rightFired = false;
    
    
    private IGridProvider grid_provider;
    
    private Vector3 prev_dir;
    private Vector3 vel = Vector3.ZERO;
    private Vector3 startPos;
    private Vector3 targetPos;
    private Vector3 facing = Vector3.DOWN;

        
    public GridMoveTimer move_timer;
    public double move_duration = 0.268d;
    
    public GridMovementComponent(GameObject owner, IGridProvider grid_provider) {
        super(owner); // Call super constructor, sets a reference to the owner's transform component
        
        this.grid_provider = grid_provider;
       
        
        // Initialize timer 0.225
        this.move_timer = new GridMoveTimer(this.move_duration, this::timer_onComplete);
    }

    @Override
    public void tick(TickEvent event) {
        
        //this.tap = false;
        // Reset start-stop transitions
        this.stopped_moving = false;
        this.started_moving = false;
                
        // Handle movement
        if (this.is_moving) this.process_movement(event.getDeltaSeconds());
        
        
        if (!this.was_moving && this.is_moving) this.started_moving = true;
        else if (this.was_moving && !this.is_moving) this.stopped_moving = true;
        
            
        // Log current state
        //double frames = GameLoopV2.getInstance().getFrames();
        //System.out.println(frames+" movement tick");
        //System.out.println("frame: "+frames+" was: " + was_moving + " is: " + is_moving + " started: " + this.started_moving + " stopped: " + this.stopped_moving+" finished: "+this.is_move_completed);
        
        // Save is_moving as was_moving for the next frame
        this.was_moving = this.is_moving;
        this.is_move_completed = false;
        
        if (this.started_moving) {
            DebugUtils.getInstance().draw_string("DEBUG: MOVING");
        } else if (this.stopped_moving) {
            DebugUtils.getInstance().draw_string("DEBUG: NOT MOVING");
        }
    }
    
    //<editor-fold desc="old code">
    //UNUSED: but i dont remove it because it was a good learnin experience (TAP IS NO LONGER DETECTED HERE)
    /*private BasicTimer tap_timer;
    private double tap_threshold = .225d;
    public boolean tap = false;
    private void is_tap_v2(Vector3 _vel){
        double f = GameLoopV2.getInstance().getFrames();    
        boolean zero = _vel.equals(Vector3.ZERO);
       
        if(!zero && !this.was_moving){
            //new movement has just been initiated (i think) so we start the "tap" timer
            System.out.println("START: "+f);
            this.tap_timer.start();
        }
       
        if(this.was_moving && !this.is_moving) {
            System.out.println("STOP "+f);
            
            //once movement is over we bring the 'tap' flag back to normal
            this.tap = false;
        }
        
        if(this.is_moving){
            //if(this.tap_timer.isRunning()) System.out.println("timer on: "+f);
            //else System.out.println("timer off: "+f);
        }
        
        if(this.was_moving && this.is_moving && !this.is_move_completed && zero) {
            //movement is still not complete, but key is released...
            //System.out.println("END: "+f);
            
            //if this happens while the tap-timer is running, we've got a tap!
            if(this.tap_timer.isRunning()) {
                System.out.println("TAP: "+f);
                this.tap = true;
                this.tap_timer.stop();
                this.tap_timer.reset();
            }
        }
    }*/
    //</editor-fold>
    
    //UNUSED FOR NOW: but i leave it, because to be able to cancel a movement feels like a needed core function
    private void cancelMovement(){
        if (is_moving) {
            owner_transform.setLocation(startPos);
            is_moving = false;
            move_timer.stop();
            if (move_timer instanceof GridMoveTimer gt) gt.reset();
        }
    }
    
            
    // <editor-fold desc="movement logic">
    public void initiate_movement(Vector3 _vel, double deltaTime) {
        // Catch exception
        if (owner_transform == null) {
            System.out.println("Cant apply movement: owner transform is not set");
            return;
        }
      
        
        // CHECK: if move direction isn't a cardinal one
        boolean isCardinal  = _vel.isCardinalDirection();
        if (!isCardinal || is_moving) return;
        
        // We only move 1 tile at a time so we normalize the input
        Vector3 dir = _vel.normalize();
        this.vel = _vel;
        
        //update our facing direction to the movement direction
        this.facing = dir;
           
                
        // Obtain current grid coordinates while maintaining grid-alignment
        Vector3 grid_coords = Vector3.ZERO;
        try {
            grid_coords = this.try_ensure_grid_alignment();
        } catch (NullOwnerTransformException e) {
            System.out.println(e.getMessage());
            return;
        }
        
        // Calculate target grid pos
        Vector3 target_grid_coords = grid_coords.plus(dir);
        
        // Check if target is within bounds
        if (!this.target_check_within_bounds(target_grid_coords)) {
            System.out.println("Movement blocked: Target tile out of bounds");
            return;
        }
        
        // COLLISION CHECK
        if(this.target_check_is_blocked(target_grid_coords)){
            System.out.println("Movement blocked: world collision");
            return;
        }
          
        
        // Prepare start and target positions for the movement
        this.startPos = this.grid_provider.grid_to_screen(grid_coords);
        this.targetPos = this.grid_provider.grid_to_screen(target_grid_coords);
        
        // Update movement flag
        is_moving = true;
        
        //System.out.println("pos: "+this.owner_transform.getLocation().toString()+"PLAYER START MOVEMENT FRAME: " + GameLoopV2.getInstance().getFrames());
        this.move_timer.start();
    }
    
    private boolean target_check_within_bounds(Vector3 target_grid_coords){
        // Check if target is within bounds
        if (target_grid_coords.getX() < 0 || target_grid_coords.getX() >= grid_provider.width_in_tiles() ||
            target_grid_coords.getY() < 0 || target_grid_coords.getY() >= grid_provider.height_in_tiles()) {
            return false;
        }
        return true;
    }
    
    private boolean target_check_is_blocked(Vector3 target_grid_coords){
        LevelGridTileV2.COLISION_TYPE colision_type = this.grid_provider.colision_at_tile(target_grid_coords);
        if (colision_type.equals(LevelGridTileV2.COLISION_TYPE.BLOCK) ||
            colision_type.equals(LevelGridTileV2.COLISION_TYPE.SURF)) {
            return true;
        } 
        return false;
    }
    
    private void process_movement(double deltaSeconds) {
        double t = this.move_timer.getProgress();
        Vector3 newLocation = Utils.dlerp(this.startPos, this.targetPos, t);
    
        //we post step events for anyonw whos interested (eg: animation)
        this.post_step_events(t);
        
               
        this.owner_transform.setLocation(newLocation);
    }
    
        
    private void timer_onComplete() {
        // Update location
        this.owner_transform.setLocation(this.targetPos);
        
        //System.out.println("PLAYER STOP MOVEMENT FRAME: " + GameLoopV2.getInstance().getFrames());
            
        this.is_moving = false;
        this.is_move_completed = true;
        this.move_timer.stop();
    }
    
    // <editor-fold desc="grid alignment logic">
    private void snapToGridCoords(Vector3 coords) {
        // Catch exception
        if (owner_transform == null) {
            //System.out.println("GridMovementComponent can't snap owner to grid: owner transform is not set");
            return;
        }
        
        Vector3 new_screen_loc = this.grid_provider.grid_to_screen(coords);
        this.owner_transform.setLocation(new_screen_loc);
    }
    
    public Vector3 try_ensure_grid_alignment() throws NullOwnerTransformException {
        if (owner_transform == null) {
            String location = ExceptionUtils.get_exception_location();
            String owner_name = this.owner.getClass().getSimpleName();
            String this_name = this.getClass().getSimpleName();
            
            throw new NullOwnerTransformException(
                    owner_name + "'s " + this_name + " cannot ensure grid alignment at: " + location
            );
        }
        
        // Calculate starting tile coordinates in the grid
        Vector3 currentPos = owner_transform.getLocation();
        
        // We convert screen location to grid coordinates
        Vector3 grid_coords = this.grid_provider.screen_to_grid(currentPos);
        
        // Ensure the owner remains grid-aligned
        if (!Utils.isInteger(grid_coords.getX()) || !Utils.isInteger(grid_coords.getY())) {
            //System.out.println("GridMovementComponent snapping owner to grid");
            Vector3 corrected_grid_coords = grid_coords.getRoundComponents();
            this.snapToGridCoords(corrected_grid_coords);
            return corrected_grid_coords;
        }
        
        return grid_coords;
    }
    
    private void post_step_events(double t){
        
        double f = GameLoopV2.getInstance().getFrames();
        System.out.println(f);
                        
        //at step_timer progress t=.25-40 & .65-.85 which roughly correspond to frames 4-8 and 12-16 for left and right steps (arbitrarily chosen) 
        
        if(t >= 0.20d && t <= 0.40d && !leftFired){ 
            System.out.println("LEFT: "+f);
            postStepEvent(ECharacterStepSide.LEFT);
            leftFired = true;
        }
        else if (t >= 0.65d && t <= 0.85 && !rightFired) {
            System.out.println("RIGHT: "+f);
            postStepEvent(ECharacterStepSide.RIGHT);
            rightFired = true;
        }  

        if (t <= 0.01 || t >= 0.99) {
            leftFired = rightFired = false;
            postStepEvent(ECharacterStepSide.NEUTRAL);
        }
                    
       
    }
    
    private void postStepEvent(ECharacterStepSide step) {
        if (owner instanceof Character char_owner) {
            EventManager.getInstance().post(new CharacterStepEvent(char_owner, step), IGameplayListener.class);
        }
    }
    
    public Vector3 getPrev_dir() {
        return prev_dir;
    }

    public void setPrev_dir(Vector3 prev_dir) {
        this.prev_dir = prev_dir;
    }

   
    public Vector3 getStartPos() {
        return startPos;
    }

    public void setStartPos(Vector3 startPos) {
        this.startPos = startPos;
    }

    public Vector3 getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(Vector3 targetPos) {
        this.targetPos = targetPos;
    }

    public GridMoveTimer getMove_timer() {
        return move_timer;
    }

    public void setMove_timer(GridMoveTimer move_timer) {
        this.move_timer = move_timer;
    }

    public boolean getStarted_moving() {
        return started_moving;
    }

    public boolean getStopped_moving() {
        return stopped_moving;
    }

    public boolean getWas_moving() {
        return was_moving;
    }

    public boolean getIs_moving() {
        return is_moving;
    }
    
    public boolean getIsMoving() {
        return this.is_moving;
    }
    
    public boolean is_move_completed() {
        return is_move_completed;
    }
    
    public Vector3 getFacing() {
        return facing;
    }
    
    public void setFacing(Vector3 v){
        if(v.isCardinalDirection() && !v.equals(Vector3.ZERO)) this.facing = v;
    }
}