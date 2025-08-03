/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputContexts;

import com.mycompany.gamev2.input_system.BindKey;
import com.mycompany.gamev2.input_system.InputActions.IA_Walk;
import com.mycompany.gamev2.input_system.InputBinding;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author J.A
 */
public class GridPlayerCharacter_InputContext extends InputContext {
    
    public GridPlayerCharacter_InputContext(){
        super("Grid Player Context", 10);
        
        //WALK
        IA_Walk  walkAction = new IA_Walk();        
        Set<BindKey> walk_keys = new HashSet<>();
        
        //arrows
        walk_keys.add(new BindKey(KeyEvent.VK_UP, -1.0f, BindKey.Axis.Y));
        walk_keys.add(new BindKey(KeyEvent.VK_DOWN, 1.0f, BindKey.Axis.Y));
        walk_keys.add(new BindKey(KeyEvent.VK_LEFT, -1.0f, BindKey.Axis.X));
        walk_keys.add(new BindKey(KeyEvent.VK_RIGHT, 1.0f, BindKey.Axis.X));
        
        addBinding(new InputBinding(walkAction, walk_keys, null, true, true, true));
    }
}
