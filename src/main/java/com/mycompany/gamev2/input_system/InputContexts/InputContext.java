/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputContexts;

import com.mycompany.gamev2.input_system.InputActions.InputAction;
import java.util.ArrayList;

/**
 *
 * @author J.A
 */
public abstract class InputContext {
    private ArrayList<InputAction> actions;
    
    protected boolean addAction(InputAction ia){
        try{
            actions.add(ia);
            return true;
        } catch(Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
    }
    
    
    protected boolean removeAction(InputAction ia){
        try{
            actions.remove(ia);
            return true;
        } catch(Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
    }
}
