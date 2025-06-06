/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels;

import com.mycompany.gamev2.component.level_components.camera_component.LevelCameraComponent;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridComponent;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.TestSphere;
import com.mycompany.gamev2.gameobjects.characters.PlayerCharacter;
import java.awt.Color;

/**
 *
 * @author J.A
 */
public class TestLevel_01 extends BaseLevel {
    
    
    
    public TestLevel_01(){
        super("TestLevel_01");
        ComponentSetup();
    }

    
    @Override
    public void onEventReceived(BaseEvent event) {
        super.onEventReceived(event);                
        if(!this.isActive()) return;
        
        
              
        if (event instanceof TickEvent){
            tick((TickEvent) event);
        }
        else if (event instanceof RenderEvent){
            render((RenderEvent) event);
        }
    }

    @Override
    public void ComponentSetup() {
        System.out.println("TESTLEVEL 1 COMPONENT SETUP");
        LevelCameraComponent level_camera = new LevelCameraComponent(this);
        addComponent(level_camera.getClass(), level_camera);
    }
    
    
    

    @Override
    public void level_windup() {
        //add in some random gameobjects to display
        int total_spheres = 3; 
        int radius = 45;
        int count = 0;
        
        while(count < total_spheres){
            Vector3 pos = new Vector3(50,50,0).plus(Vector3.DOWN.getScaled(count * (radius+50)));
            TestSphere s = new TestSphere(Color.MAGENTA, radius, pos, Vector3.RIGHT, 100);
            
            addGameObject(s);
                        
            count ++;
        }
        
        PlayerCharacter player = new PlayerCharacter();
        addGameObject(player);
        
        LevelCameraComponent cam = getComponent(LevelCameraComponent.class);
        if(cam != null) cam.setTarget(player);
    }

    @Override
    public void level_windown() {
        super.level_windown(); //this super call ensures all gameobjects on this level are cleared
        //any other windown logic
    }
    
    
    protected void tick(TickEvent e) {
        LevelCameraComponent cam = getComponent(LevelCameraComponent.class);
        if(cam != null) cam.tick(e);
    }

    
    protected void render(RenderEvent e) {
        LevelCameraComponent cam = getComponent(LevelCameraComponent.class);
        if(cam != null) cam.render(e);
    }

    @Override
    public String toString() {
        return super.toString(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public int hashCode() {
        return super.hashCode(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
    
}
