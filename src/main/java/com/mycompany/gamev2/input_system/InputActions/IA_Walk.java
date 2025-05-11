/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputActions;

import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.input_system.BindKey;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void evaluateAxes(Set<BindKey> activeKeys) {
        Set<BindKey> x_keys = activeKeys.stream().filter(n -> n.getAxis() == BindKey.Axis.X).collect(Collectors.toSet());
        Set<BindKey> y_keys = activeKeys.stream().filter(n -> n.getAxis() == BindKey.Axis.Y).collect(Collectors.toSet());
        
        float x,y;
        
        double x_scale = 0.0d;
        for(BindKey k : x_keys){
            x_scale = Math.clamp(x_scale + k.getAxisScale(), -1, 1);
        }
        
       
        double y_scale = 0.0d;
        for(BindKey k : y_keys){
            y_scale = Math.clamp(y_scale + k.getAxisScale(), -1, 1);
        }
        
        this.setAxisValues(x_scale, y_scale);
    }
 
}
