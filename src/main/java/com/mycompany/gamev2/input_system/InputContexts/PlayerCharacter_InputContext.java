/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputContexts;

import com.mycompany.gamev2.input_system.InputActions.IA_Walk;
import com.mycompany.gamev2.input_system.InputBinding;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author J.A
 */
public class PlayerCharacter_InputContext extends InputContext {
    
    public PlayerCharacter_InputContext(){
        super("PlayerGameplay", 10);
        
        IA_Walk walkAction = new IA_Walk();
        
         // W: Forward (+Y)
        Set<Integer> forwardKeys = new HashSet<>();
        forwardKeys.add(KeyEvent.VK_W);
        addBinding(new InputBinding(walkAction, forwardKeys, null, true, 1.0f));
        
                
        // S: Backward (-Y)
        Set<Integer> backwardKeys = new HashSet<>();
        backwardKeys.add(KeyEvent.VK_S);
        addBinding(new InputBinding(walkAction, backwardKeys, null, true, -1.0f));

        // A: Left (-X)
        Set<Integer> leftKeys = new HashSet<>();
        leftKeys.add(KeyEvent.VK_A);
        addBinding(new InputBinding(walkAction, leftKeys, null, true, -1.0f));

        // D: Right (+X)
        Set<Integer> rightKeys = new HashSet<>();
        rightKeys.add(KeyEvent.VK_D);
        addBinding(new InputBinding(walkAction, rightKeys, null, true, 1.0f));
    }
}

