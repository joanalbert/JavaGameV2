/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level_components.camera_component;


import com.mycompany.gamev2.component.level_components.LevelComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.exceptions.CameraNoTargetException;
import com.mycompany.gamev2.gamemath.BoxBounds;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.levels.BaseLevel;
import com.mycompany.gamev2.window.MyWindow;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author J.A
 */
public class LevelCameraComponent extends LevelComponent {
    
    private Vector3 position = Vector3.ZERO;
    private GameObject target = null; 
    private BoxBounds bounds  = null;
    
    double x_offset = 0;
    double y_offset = 0;
    
    private BaseLevel owning_level;
    
    private boolean smooth_tracking = false;
       
    public LevelCameraComponent(BaseLevel level){
        this.owning_level = level;
    }
    
    public void setTarget(GameObject target){
        if(target != null) this.target = target;
    }
    
    private void track(GameObject target){
        if(target == null) return;
        this.position = target.getObjectLocation();
    }
    
    private void smooth_track(GameObject target) {
        if (target == null) return;
        Vector3 targetPos = target.getObjectLocation();
        double lerpFactor = 0.1; // Adjust for smoothness
        this.position = this.position.plus(targetPos.minus(this.position).getScaled(lerpFactor));
    }
    
    private void compute_bounds() throws CameraNoTargetException{
        if(this.target == null) {
            throw new CameraNoTargetException("Attempted to compute bounds of a camera which has no target");
        }
        
        /*
        double top    = this.position.getY() - MyWindow.DIMENSIONS.height/2;
        double bottom = this.position.getY() + MyWindow.DIMENSIONS.height/2;
        double left   = this.position.getX() - MyWindow.DIMENSIONS.width/2;
        double right  = this.position.getX() + MyWindow.DIMENSIONS.width/2;
        */
        
        //int test = 240;
        
        int w = MyWindow.DIMENSIONS.width;
        int h = MyWindow.DIMENSIONS.height;
        
        double top    = this.position.getY() - h/2;
        double bottom = this.position.getY() + h/2;
        double left   = this.position.getX() - w/2;
        double right  = this.position.getX() + w/2;
        
        bounds = new BoxBounds(top - y_offset, bottom + y_offset , left + x_offset, right - x_offset);
    }
    
    @Override
    public void tick(TickEvent e) {
        if(!this.isActive()) return; //only tick if active
        
        if(this.smooth_tracking) smooth_track(this.target);
        else track(this.target);
        
        try{compute_bounds();}
        catch(CameraNoTargetException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    @Override
    public void render(RenderEvent e) {
         if(!this.isActive()) return; //only render if active
         
        Graphics2D g = e.getGraphics();
        g.setColor(Color.blue);
        
        /*int x = (int) this.bounds.getLeft();
        int y = (int) this.bounds.getTop();
        int w = (int) (this.bounds.getLeft() - this.bounds.getRight());
        int h = (int) (this.bounds.getBottom() - this.bounds.getTop());
        
        g.drawRect(x, y, w, h);*/
        
        
        int x = (int) this.bounds.getLeft();
        int y = (int) this.bounds.getTop();
        int w = (int) (this.bounds.getRight() - this.bounds.getLeft()); // Width
        int h = (int) (this.bounds.getBottom() - this.bounds.getTop()); // Height

        g.drawRect(x, y, w, h);
    }
    
    
    public BoxBounds getBounds(){return this.bounds;}
    public void setSmoothTracking(boolean b){this.smooth_tracking = b;}
}
