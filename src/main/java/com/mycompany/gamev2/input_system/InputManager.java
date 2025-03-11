/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system;

/**
 *
 * @author J.A
 */
public class InputManager {
    
    private static InputManager instance;
    
    private InputManager(){}
    
    public InputManager getInstance(){
        if(this.instance == null) this.instance = new InputManager();
        return this.instance;
    }
    
    public void postInputEvent(){
        
    }
}
