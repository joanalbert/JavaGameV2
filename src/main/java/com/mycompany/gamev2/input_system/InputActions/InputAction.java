/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputActions;

import com.mycompany.gamev2.input_system.BindKey;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author J.A
 */
public abstract class InputAction {
  
    public enum ActionType {
        TRIGGERED, //for discrete events (e.g jump, shoot)
        AXIS_1D,   //for single-axis inputs (e.g throttle)
        AXIS_2D    //for 2d inputs (e.g movement)
    }
    
    
    protected String name;
    protected ActionType type;
    protected boolean active = false;
    
    protected double value = 0.0f; // data for axis inputs
    protected double[] axisValues = new double[2]; // data for 2d axis inputs
    protected double deltaTime;
    
    protected Consumer<InputAction> onTrigger; //callback for handling
    
    
    public InputAction(String name, ActionType type){
        this.name = name;
        this.type = type;
    }
    
    public void setActive(boolean active){
        this.active = active;
        if(active && onTrigger != null){
            onTrigger.accept(this);
        }
    }
    
    public void setValue(float value){
        this.value = value;
        if(onTrigger != null){
            onTrigger.accept(this);
        }
    }
    
    public void setAxisValues(double x, double y){
        this.axisValues[0] = x;
        this.axisValues[1] = y;
        if(onTrigger != null){
            onTrigger.accept(this);
        }
    }
    
    public void evaluateAxes(Set<BindKey> activeKeys, double deltaTime)
    {
        if(this.type != ActionType.AXIS_2D) return;
        
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
        this.deltaTime = deltaTime;
    }
    
    public void trigger(){
        if(this.type != ActionType.TRIGGERED) return;
        
        if(onTrigger != null){
            onTrigger.accept(this);
        }
    }
    
    public void setOnTriggered(Consumer<InputAction> callback) {
        this.onTrigger = callback;
    }

    public String getName() {
        return name;
    }

    public ActionType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public double getValue() {
        return value;
    }

    public double[] getAxisValues() {
        return axisValues;
    }
    
    public double getDeltaTime(){
        return deltaTime;
    }
}
