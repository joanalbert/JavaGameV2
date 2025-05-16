/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputContexts;

import com.mycompany.gamev2.input_system.InputActions.IA_Walk;
import com.mycompany.gamev2.input_system.InputBinding;
import com.mycompany.gamev2.input_system.BindKey;
import com.mycompany.gamev2.input_system.InputActions.IA_Shoot;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author J.A
 */
public class PlayerCharacter_InputContext extends InputContext {
    
    public PlayerCharacter_InputContext(){
        super("PlayerContext", 10);
        
        IA_Walk  walkAction = new IA_Walk();        
        Set<BindKey> walk_keys = new HashSet<>();
       
        walk_keys.add(new BindKey(KeyEvent.VK_W, -1.0f, BindKey.Axis.Y));
        walk_keys.add(new BindKey(KeyEvent.VK_S,  1.0f, BindKey.Axis.Y));
        walk_keys.add(new BindKey(KeyEvent.VK_A, -1.0f, BindKey.Axis.X));
        walk_keys.add(new BindKey(KeyEvent.VK_D,  1.0f, BindKey.Axis.X));
        
        addBinding(new InputBinding(walkAction, walk_keys, null, true, true, false));
        
        
        IA_Shoot shootAction = new IA_Shoot();
        Set<BindKey> shoot_keys = new HashSet<>();
        
        shoot_keys.add(new BindKey(KeyEvent.VK_SPACE, 0.0f, BindKey.Axis.NONE));
        
        addBinding(new InputBinding(shootAction, shoot_keys, null, false, true, true));
        
         // W: Forward (+Y)
        /*Set<Integer> forwardKeys = new HashSet<>();
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
        addBinding(new InputBinding(walkAction, rightKeys, null, true, 1.0f));*/
        
    }
}

