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
    
    private Set<BindKey> keyCodes;
    private Set<BindKey> modifiers;
    
    private boolean isAxis;
        
    
    public InputBinding(InputAction action, Set<BindKey> keyCodes, Set<Integer> modifiers, boolean isAxis){
        this.action = action;
        this.keyCodes = (keyCodes != null) ? keyCodes : new HashSet<>();
        this.modifiers = (modifiers != null) ? keyCodes : new HashSet<>();
        this.isAxis = isAxis;
    }
    
    public InputAction getAction(){
        return action;
    }
    
    public boolean matches(HashMap<Integer, Boolean> keyStates){
        for(BindKey k : keyCodes){
            int keyCode = k.getKeyCode();
            if(!keyStates.getOrDefault(keyCode, false)) return false;
        }
        
        for(BindKey k : modifiers){
            int modifier = k.getKeyCode();
            if(!keyStates.getOrDefault(modifier, false)) return false;
        }
        
        return true;
    }
    
    
    public boolean isAxis() {
        return isAxis;
    }
}
