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

    
    
    private LevelGridComponent grid_component;
    
    private Vector3 prev_dir;
    private Vector3 queued_dir;
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
    }

    @Override
    public void tick(TickEvent event) {
        
                               
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
    
    // <editor-fold desc="movement logic">
    public void initiate_movement(Vector3 vel, double deltaTime) {
        // Catch exception
        if (owner_transform == null) {
            System.out.println("Cant apply movement: owner transform is not set");
            return;
        }
        
        // CHECK: if move direction isn't a cardinal one
        if (!vel.isCardinalDirection() || is_moving) return;
        
                
        // Obtain current grid coordinates while maintaining grid-alignment
        Vector3 grid_coords = Vector3.ZERO;
        try {
            grid_coords = this.try_ensure_grid_alignment();
        } catch (NullOwnerTransformException e) {
            System.out.println(e.getMessage());
            return;
        }
        
        // We only move 1 tile at a time
        Vector3 dir = vel.normalize();
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
        
        System.out.println("pos: "+this.owner_transform.getLocation().toString()+"PLAYER START MOVEMENT FRAME: " + GameLoopV2.getInstance().getFrames());
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
        
        System.out.println("PLAYER STOP MOVEMENT FRAME: " + GameLoopV2.getInstance().getFrames());
            
        this.is_moving = false;
        this.is_move_completed = true;
        this.move_timer.stop();
    }
    
    // <editor-fold desc="grid alignment logic">
    private void snapToGridCoords(Vector3 coords) {
        // Catch exception
        if (owner_transform == null) {
            System.out.println("GridMovementComponent can't snap owner to grid: owner transform is not set");
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
            System.out.println("GridMovementComponent snapping owner to grid");
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

    public Vector3 getQueued_dir() {
        return queued_dir;
    }

    public void setQueued_dir(Vector3 queued_dir) {
        this.queued_dir = queued_dir;
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
}