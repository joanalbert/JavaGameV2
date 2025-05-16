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
    private boolean isHeld;    
    
    public InputBinding(InputAction action, Set<BindKey> keyCodes, Set<Integer> modifiers, boolean isAxis, boolean isHeld){
        this.action = action;
        this.keyCodes = (keyCodes != null) ? keyCodes : new HashSet<>();
        this.modifiers = (modifiers != null) ? keyCodes : new HashSet<>();
        this.isAxis = isAxis;
        this.isHeld = isHeld;
    }
    
    public InputAction getAction(){
        return action;
    }
    
    public boolean matches(HashMap<Integer, Boolean> keyStates){
        
        boolean state  = false;
        boolean state2 = modifiers.isEmpty() ? true : false; 
        
        for(BindKey k : keyCodes){
            int keyCode = k.getKeyCode();
            if(keyStates.containsKey(keyCode) && keyStates.get(keyCode) == true) state = true;
        }
        
        for(BindKey k : modifiers){
            int modifier = k.getKeyCode();
            if(keyStates.containsKey(modifiers)) state = true;
        }
        
        return state && state2;
    }
    
    public Set<BindKey> getActiveBindKeys(HashMap<Integer, Boolean> keyStates){
        
        Set<BindKey> actives = new HashSet<>();
        
        for(BindKey k : keyCodes){
            int keyCode = k.getKeyCode();
            if(keyStates.containsKey(keyCode) && keyStates.get(keyCode) == true) actives.add(k);
        }
        
        return actives;
    }
    
    public Set<BindKey> getBindKeys(){return this.keyCodes;}
    
    public boolean isAxis() {
        return isAxis;
    }
    
    public boolean getIsHeld(){return this.isHeld;}
}
