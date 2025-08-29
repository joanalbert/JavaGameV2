/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level_components.camera_component;

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
    
    public GridLevelCameraComponent(BaseLevel level){
        super(level);
        this.timer = new GridMoveTimer(.225d, null);
    }
    
    
    @Override
    public void tick(TickEvent e) {
        if(!this.isActive()) return; //only tick if active
        
        this.delta = e.getDeltaSeconds();
        
        
        //track the target's position
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
                          
            //target has started moving this frame
            if( target_movement.getStarted_moving() ) {
                //System.out.println("MOVE");
                this.target_current_position = target_movement.getStartPos(); 
                this.target_destination_position = target_movement.getTargetPos();
                this.cam_is_moving = true;
                this.timer.start();
            }
            
            //target has stopped moving this frame
            if(target_movement.getStopped_moving()) {
                //System.out.println("STOP");
                this.cam_is_moving = false;
                this.timer.stop();
            }
            
        }
        
        this.position = target.getObjectLocation();
        
    }
    
    private void process_cam_movement(){
        double t = this.timer.getProgress();
        Vector3 newLocation = Utils.dlerp(this.target_current_position, this.target_destination_position, t);
        this.position = newLocation;
    }
}
