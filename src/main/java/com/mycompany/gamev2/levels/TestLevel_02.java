/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels;

import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.gameobjects.TestSphere;
import java.awt.Color;

/**
 *
 * @author J.A
 */
public class TestLevel_02 extends BaseLevel {

    public TestLevel_02(){
        super("TestLevel_02");
    }
    
    @Override
    public void onEventReceived(BaseEvent event) {
        super.onEventReceived(event); 
        
    }
    
    @Override
    public void level_windup() {
        //add in some random gameobjects to display
        int total_spheres = 2; 
        int radius = 45;
        int count = 0;
        
        while(count < total_spheres){
            Vector3 pos = new Vector3(50,50,0).plus(Vector3.DOWN.getScaled(count * (radius+50)));
            TestSphere s = new TestSphere(Color.YELLOW, radius, pos, Vector3.RIGHT, 100);
            
            addGameObject(s);
            
            count ++;
        }
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
