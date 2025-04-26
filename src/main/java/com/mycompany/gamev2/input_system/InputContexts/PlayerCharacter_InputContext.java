/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputContexts;

import com.mycompany.gamev2.input_system.InputActions.IA_Walk;

/**
 *
 * @author J.A
 */
public class PlayerCharacter_InputContext extends InputContext {
    
    public PlayerCharacter_InputContext(){
        addAction(new IA_Walk());
    }
}
