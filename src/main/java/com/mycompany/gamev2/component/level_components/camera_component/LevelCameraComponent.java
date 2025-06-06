/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level_components.camera_component;


import com.mycompany.gamev2.component.level_components.LevelComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
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
    
    private void compute_bounds(){
        if(this.target == null) return;
        
        double top    = this.position.getY() - MyWindow.DIMENSIONS.height/2;
        double bottom = this.position.getY() + MyWindow.DIMENSIONS.height/2;
        double left   = this.position.getX() - MyWindow.DIMENSIONS.width/2;
        double right  = this.position.getX() + MyWindow.DIMENSIONS.width/2;
        bounds = new BoxBounds(top - y_offset, bottom + y_offset , left + x_offset, right - x_offset);
    }
    
    @Override
    public void tick(TickEvent e) {
        track(this.target);
        compute_bounds();
    }
    @Override
    public void render(RenderEvent e) {
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
}
