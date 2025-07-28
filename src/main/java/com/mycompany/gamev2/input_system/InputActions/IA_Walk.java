/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputActions;


import com.mycompany.gamev2.input_system.BindKey;
import java.util.Set;

/**
 *
 * @author J.A
 */
public class IA_Walk extends InputAction {
    
    public IA_Walk(){
        super("IA_Walk", ActionType.AXIS_2D);
    }
    
    @Override
    public void setAxisValues(double x, double y){
        super.setAxisValues(x, y);
    }

    @Override
    public void evaluateAxes(Set<BindKey> activeKeys, double deltaTime) {
        super.evaluateAxes(activeKeys, deltaTime);
    }
 
}
