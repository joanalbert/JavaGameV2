/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputActions;

import java.util.function.Consumer;

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
    
    protected float value = 0.0f; // data for axis inputs
    protected float[] axisValues = new float[2]; // data for 2d axis inputs
    
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
    
    public void setAxisValues(float x, float y){
        this.axisValues[0] = x;
        this.axisValues[1] = y;
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

    public float getValue() {
        return value;
    }

    public float[] getAxisValues() {
        return axisValues;
    }
}
