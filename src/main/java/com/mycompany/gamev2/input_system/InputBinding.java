/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system;

import com.mycompany.gamev2.input_system.InputActions.InputAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author J.A
 */
public class InputBinding {
    private InputAction action;
    
    private Set<Integer> keyCodes;
    private Set<Integer> modifiers;
    
    private boolean isAxis;
    private float axisScale;
    
    
    public InputBinding(InputAction action, Set<Integer> keyCodes, Set<Integer> modifiers, boolean isAxis, float axisScale){
        this.action = action;
        this.keyCodes = (keyCodes != null) ? keyCodes : new HashSet<>();
        this.modifiers = (modifiers != null) ? keyCodes : new HashSet<>();
        this.isAxis = isAxis;
        this.axisScale = axisScale;
    }
    
    public InputAction getAction(){
        return action;
    }
    
    public boolean matches(HashMap<Integer, Boolean> keyStates){
        for(Integer keyCode : keyCodes){
            if(!keyStates.getOrDefault(keyCode, false)) return false;
        }
        
        for(Integer modifier : modifiers){
            if(!keyStates.getOrDefault(modifier, false)) return false;
        }
        
        return true;
    }
    
    public float getAxisScale() {
        return axisScale;
    }

    public boolean isAxis() {
        return isAxis;
    }
}
