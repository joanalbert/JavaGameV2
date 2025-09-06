/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level_components.camera_component;

import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.Utils.GridMoveTimer;
import com.mycompany.gamev2.component.object_components.GridMovementComponent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.exceptions.CameraNoTargetException;
import com.mycompany.gamev2.gamemath.Utils;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.characters.GridPlayerCharacter2D;
import com.mycompany.gamev2.levels.BaseLevel;

/**
 *
 * @author J.A
 */
public class GridLevelCameraComponent extends LevelCameraComponent {
    
    //possible candidates for subclass
    private boolean cam_is_moving = false;
    private GridMoveTimer timer;
    private Vector3 target_destination_position;
    private Vector3 target_current_position;
    private GridMovementComponent target_movement;
    
    public GridLevelCameraComponent(BaseLevel level){
        super(level);
        this.timer = new GridMoveTimer(0.225d, null);
        //this.position = this.target.getObjectLocation();
    }
    
    
    @Override
    public void tick(TickEvent e) {
        if(!this.isActive()) return; //only tick if active
        
        this.delta = e.getDeltaSeconds();
               
        //System.out.println(".");
        //System.out.println(this.timer.getProgress());
        //track the target's position
        
        double frames = GameLoopV2.getInstance().getFrames();
        System.out.println(frames+" cam tick");
        
        track();
        
        if(this.cam_is_moving) this.process_cam_movement();
        
        //update camera bounds and offsets
        try{
            compute_bounds();
            compute_cam_offsets();
        }
        catch(CameraNoTargetException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    protected void track(){
            
        if(target == null) return;
        
        
        
        //System.out.println("CAM POS: "+this.position.toString()+" TARGET POS: "+target.getObjectLocation().toString());
        if(target instanceof GridPlayerCharacter2D grid_player){
            GridMovementComponent target_movement = grid_player.getComponent(GridMovementComponent.class);
            if(target_movement == null) return;
            
            this.target_movement = target_movement;
            
            /*double target_t = target_movement.getMove_timer().getProgress();
            double cam_t    = this.timer.getProgress();
            System.out.println("target: "+target_t+" cam: "+cam_t);*/
                                      
            //target has started moving this frame
            //if( target_movement.getStarted_moving() || target_movement.is_move_completed() ) {
            if(target_movement.getStarted_moving() || (target_movement.is_move_completed() && target_movement.getIsMoving())){    
                double frames = GameLoopV2.getInstance().getFrames();
                System.out.println("frame: "+frames+" CAM MOVE");
                this.target_current_position = target_movement.getStartPos(); //this.target.getObjectLocation();  
                this.target_destination_position = target_movement.getTargetPos();
                this.cam_is_moving = true;
                this.timer.restart();
            }
            
            
            //target has stopped moving this frame
            if(target_movement.getStopped_moving()) {
                double frames = GameLoopV2.getInstance().getFrames();
                System.out.println("frame: "+frames+" CAM STOP - " +target_movement.is_move_completed());
                
                this.position = this.target.getObjectLocation(); //target_movement.getTargetPos();
                this.cam_is_moving = false;
                this.timer.stop();
                
                //System.out.println("____END____");
                //System.out.println(this.target.getObjectLocation().toString()+" "+this.position.toString()+" "+this.target_current_position.toString()+" "+this.target_destination_position);
            }
            
            
        }
        
        //this.position = target.getObjectLocation();
    }
    
    private void process_cam_movement(){
        //System.out.println("CAM MOVE");
        double t = this.timer.getProgress();
        double target_t = target_movement.getMove_timer().getProgress();
        
        Vector3 newLocation = Utils.dlerp(this.target_current_position, this.target_destination_position, t);
        
        //for some reason t gets to 0 before the movement has compelted, cuasing a very noticeable jitter in the 2nd last frame of the transition
        //for now i'll handle this by manually checking t
        //if(t < this.timer.get_prev_t()) return;
        
        this.position = newLocation;
        
        
        //System.out.println("t: "+t+" "+this.target.getObjectLocation().toString()+" "+newLocation.toString()+" "+this.target_current_position.toString()+" "+this.target_destination_position);
    }
}
