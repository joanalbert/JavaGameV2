/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components;

import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.Utils.GridMoveTimer;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV2;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.exceptions.NoSuchGridTileException;
import com.mycompany.gamev2.exceptions.NoSuchLevelComponentException;
import com.mycompany.gamev2.exceptions.NonGridLevelException;
import com.mycompany.gamev2.exceptions.NullLevelException;
import com.mycompany.gamev2.gamemath.Utils;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.levels.LevelManager;
import com.mycompany.gamev2.levels.grid.GridLevelBase;

/**
 *
 * @author J.A
 */
public class GridMovementComponent extends MovementComponent implements IGameUpdateListener{
    
    private boolean isMoving = false;
    private LevelGridComponent grid_component;
    
    private Vector3 prev_dir;
    private Vector3 queued_dir;
    private Vector3 startPos;
    private Vector3 targetPos;
    
    private GridMoveTimer move_timer;
    
    /*private double moveDuration = 0.225d;
    private double moveTimer = 0f;*/
    
    public GridMovementComponent(GameObject owner) throws NonGridLevelException, NoSuchLevelComponentException, NullLevelException {
        super(owner); //call super constructor, sets a reference to the owner's transform component
        
        
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
        
        //get the current level's LevelGridComponent
        GridLevelBase current_level = LevelManager.getInstance().getCurrentGridLevel();
        
        //from the current map, get the grid component, and cache it
        LevelGridComponent grid = current_level.getComponent(LevelGridComponent.class);
        if(grid != null) this.grid_component = grid; 
        else throw new NoSuchLevelComponentException("Couldn't retrieve the following component: LevelGridComponent from GridLevelBase");
        
        this.move_timer = new GridMoveTimer(0.225d, this::timer_onComplete);
    }

    @Override
    public void onEventReceived(BaseEvent event) {
        if(event instanceof TickEvent tevent) this.tick(tevent);
    }
   
    
        
    private Vector3 screenLoc_to_GridCoords(Vector3 loc ){
        int tile_size = grid_component.getTileSize();
        Vector3 grid_coords = new Vector3(loc.getX() / tile_size,
                                          loc.getY() / tile_size, 0);
        return grid_coords;
    }
    
    private Vector3 GridCoords_to_ScreenLoc(Vector3 grid_coords){
        int tile_size = grid_component.getTileSize();
        Vector3 screen_loc = new Vector3(grid_coords.getX() * tile_size,
                                         grid_coords.getY() * tile_size, 0);
        return screen_loc;
    }

    private void snapToGridCoords(Vector3 coords){
        //catch exception
        if(owner_transform == null){
            System.out.println("GridMovementComponent can't snap owner to grid: owner transform is not set");
            return;
        } 
        
        Vector3 new_screen_loc = this.GridCoords_to_ScreenLoc(coords);
        this.owner_transform.setLocation(new_screen_loc);
        
    }
    
   
    public Vector3 ensure_grid_alignment(){
        if (owner_transform == null) {
            System.out.println("GridMovementComponent cannot ensure grid alignment: owner transform is not set");
            return null;
        }
        
        //calculate starting tile coordinates in the grid
        Vector3 currentPos = owner_transform.getLocation();
        
        //we convert screen location to grid coordinates
        Vector3 grid_coords = this.screenLoc_to_GridCoords(currentPos);
       
        //ensure the owner remains grid-aligned
        if(!Utils.isInteger(grid_coords.getX()) || !Utils.isInteger(grid_coords.getY())){
           System.out.println("GridMovementComponent snapping owner to grid");
           Vector3 corrected_grid_coords = grid_coords.getRoundComponents();
           this.snapToGridCoords(corrected_grid_coords);
           
           return corrected_grid_coords;
        }
        
        return grid_coords;
    }
    
    public void applyMovement(Vector3 vel, double deltaTime){
        
       //System.out.println("target coords 1");
                        
        //catch exception
        if(owner_transform == null){
            System.out.println("Cant apply movement: owner transform is not set");
            return;
        } 
        
        //System.out.println("target coords 2");
        
        //if directions isn't a cardinal one
        if(!vel.isCardinalDirection()) return;
        
        //System.out.println("target coords 3");
        
        //we only move or queue if we're not already moving
        if (isMoving) {
            double p = this.move_timer.getProgress();
            if(p >= 0.89d && vel.isCardinalDirection()) this.queued_dir = vel; //only queue a movement if it's inputed towards the end of the current one
            return;
        }
     
        //System.out.println("target coords 4");
               
        //obtain current grid coordinates while maintaining grid-alignment
        Vector3 grid_coords = this.ensure_grid_alignment();
        if(grid_coords == null){
            System.out.println("Cant apply movement: owner transform is not set");
            return;
        } 
        
        //System.out.println("target coords 5");
       
        //we only move 1 tile at a time
        Vector3 dir = vel.normalize();
                       
        //calculatte target grid pos
        //System.out.println("target coords 6");
        Vector3 target_grid_coords = grid_coords.plus(dir);
        
        // Check if target is within bounds
        if (target_grid_coords.getX() < 0 || target_grid_coords.getX() >= grid_component.getTileWidth() ||
            target_grid_coords.getY() < 0 || target_grid_coords.getY() >= grid_component.getTileHeight()) {
            System.out.println("Movement blocked: Target tile out of bounds");
            return;
        }
        
        //COLISION CHECK
        try{
            LevelGridTileV2.COLISION_TYPE colision_type = this.grid_component.getColisionForTile(target_grid_coords); //this can throw an exception
            
            if(colision_type.equals(LevelGridTileV2.COLISION_TYPE.BLOCK) ||
               colision_type.equals(LevelGridTileV2.COLISION_TYPE.SURF)){
                System.out.println("Movement blocked: world collision");
                return;
            }
        } catch (NoSuchGridTileException e){
            System.out.println(e.getMessage());
        }
        
        
        
        //prepare start and target positions for the movement
        this.startPos  = this.GridCoords_to_ScreenLoc(grid_coords); 
        this.targetPos = this.GridCoords_to_ScreenLoc(target_grid_coords);
        
               
        //set the flag and initiate the timer
        isMoving = true;
        this.move_timer.start();
    }
    
    private void tick(TickEvent event){
        if(this.isMoving) this.processMovement(event.getDeltaSeconds());        
    }
    
    private void processMovement(double deltsSeconds){
        double t = this.move_timer.getProgress();
        Vector3 newLocation = Utils.dlerp(this.startPos, this.targetPos, t);
        this.owner_transform.setLocation(newLocation);
        System.out.println("progress: "+t +" time: "+System.currentTimeMillis());
    }
        
    private void timer_onComplete(){
        
        this.owner_transform.setLocation(this.targetPos);
        this.isMoving = false;
        
        
        if(this.queued_dir != null){ //continuous movement
            Vector3 q = this.queued_dir;
            this.queued_dir = null;
            this.applyMovement(q, 0);
        }
        else { //single tap
            
            this.move_timer.stop();
        }
        
        
    }
    
    public boolean getIsMoving(){return this.isMoving;}
}