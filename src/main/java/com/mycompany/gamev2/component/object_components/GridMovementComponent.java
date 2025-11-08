/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components;

import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.Utils.BasicTimer;
import com.mycompany.gamev2.Utils.GridMoveTimer;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV2;
import com.mycompany.gamev2.debug.DebugUtils;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.exceptions.ExceptionUtils;
import com.mycompany.gamev2.exceptions.NoSuchGridTileException;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.exceptions.NonGridLevelException;
import com.mycompany.gamev2.exceptions.NullLevelException;
import com.mycompany.gamev2.exceptions.NullOwnerTransformException;
import com.mycompany.gamev2.gamemath.Utils;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.levels.LevelManager;
import com.mycompany.gamev2.levels.grid.GridLevelBase;

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

    //tap detection stuff
    private BasicTimer tap_timer;
    private double tap_threshold = .225d;
    public boolean tap = false;
    
    private LevelGridComponent grid_component;
    
    private Vector3 prev_dir;
    private Vector3 vel = Vector3.ZERO;
    private Vector3 startPos;
    private Vector3 targetPos;
    private Vector3 facing = Vector3.ZERO;

        
    public GridMoveTimer move_timer;
    
    public GridMovementComponent(GameObject owner) throws NonGridLevelException, NoSuchLevelComponentException, NullLevelException {
        super(owner); // Call super constructor, sets a reference to the owner's transform component
        
       
        // Get the current level's LevelGridComponent and cache it
        GridLevelBase current_level = LevelManager.getInstance().getCurrentGridLevel();
        LevelGridComponent grid = current_level.getComponent(LevelGridComponent.class);
        if (grid != null) this.grid_component = grid;
        else throw new NoSuchLevelComponentException("Couldn't retrieve the following component: LevelGridComponent from GridLevelBase");
        
        // Initialize timer
        this.move_timer = new GridMoveTimer(0.225d, this::timer_onComplete);
        this.tap_timer = new BasicTimer(tap_threshold/2);
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
    }
    
    
    private void cancelMovement(){
        if (is_moving) {
            owner_transform.setLocation(startPos);
            is_moving = false;
            move_timer.stop();
            if (move_timer instanceof GridMoveTimer gt) gt.reset();
        }
        this.tap = false;
    }
    
    double heldTime = 0d;
        
    // <editor-fold desc="movement logic">
    public void initiate_movement(Vector3 _vel, double deltaTime) {
        // Catch exception
        if (owner_transform == null) {
            //System.out.println("Cant apply movement: owner transform is not set");
            return;
        }
      
        boolean inputActive = !_vel.equals(Vector3.ZERO);
        boolean isCardinal  = _vel.isCardinalDirection();
        /*double f = GameLoopV2.getInstance().getFrames();                          
        
        if(inputActive){
            this.heldTime += deltaTime;
            this.tap = false;
        }
        else {
            if(this.heldTime > 0 && this.heldTime < this.tap_threshold) {
                this.tap = true;
                System.out.println("TAP: "+f);
            }
            this.heldTime = 0d;
        }
        
        if(this.tap){
            Vector3 new_facing = _vel.normalize();
            if(!new_facing.equals(this.facing)){
                this.cancelMovement();
                this.facing = _vel.normalize();
                return;
            }
        }*/
        
        // CHECK: if move direction isn't a cardinal one
        if (!isCardinal || is_moving) return;
        
                        
        // Obtain current grid coordinates while maintaining grid-alignment
        Vector3 grid_coords = Vector3.ZERO;
        try {
            grid_coords = this.try_ensure_grid_alignment();
        } catch (NullOwnerTransformException e) {
            System.out.println(e.getMessage());
            return;
        }
        
        // We only move 1 tile at a time
        Vector3 dir = _vel.normalize();
        this.vel = _vel;
        this.facing = dir;
        
        // Calculate target grid pos
        Vector3 target_grid_coords = grid_coords.plus(dir);
        
        // Check if target is within bounds
        if (target_grid_coords.getX() < 0 || target_grid_coords.getX() >= grid_component.getTileWidth() ||
            target_grid_coords.getY() < 0 || target_grid_coords.getY() >= grid_component.getTileHeight()) {
            System.out.println("Movement blocked: Target tile out of bounds");
            return;
        }
        
        // COLLISION CHECK
        try {
            LevelGridTileV2.COLISION_TYPE colision_type = this.grid_component.getColisionForTile(target_grid_coords);
            if (colision_type.equals(LevelGridTileV2.COLISION_TYPE.BLOCK) ||
                colision_type.equals(LevelGridTileV2.COLISION_TYPE.SURF)) {
                System.out.println("Movement blocked: world collision");
                return;
            }
        } catch (NoSuchGridTileException e) {
            System.out.println(e.getMessage());
        }
        
        // Prepare start and target positions for the movement
        this.startPos = this.grid_component.GridCoords_to_ScreenLoc(grid_coords);
        this.targetPos = this.grid_component.GridCoords_to_ScreenLoc(target_grid_coords);
        
        // Update movement flag
        is_moving = true;
        
        //System.out.println("pos: "+this.owner_transform.getLocation().toString()+"PLAYER START MOVEMENT FRAME: " + GameLoopV2.getInstance().getFrames());
        this.move_timer.start();
    }
    
    private void process_movement(double deltaSeconds) {
        double t = this.move_timer.getProgress();
        Vector3 newLocation = Utils.dlerp(this.startPos, this.targetPos, t);
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
        
        Vector3 new_screen_loc = this.grid_component.GridCoords_to_ScreenLoc(coords);
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
        Vector3 grid_coords = this.grid_component.ScreenLoc_to_GridCoords(currentPos);
        
        // Ensure the owner remains grid-aligned
        if (!Utils.isInteger(grid_coords.getX()) || !Utils.isInteger(grid_coords.getY())) {
            //System.out.println("GridMovementComponent snapping owner to grid");
            Vector3 corrected_grid_coords = grid_coords.getRoundComponents();
            this.snapToGridCoords(corrected_grid_coords);
            return corrected_grid_coords;
        }
        
        return grid_coords;
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