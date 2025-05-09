/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system;

/**
 *
 * @author J.A
 */
public class KeyInput {
    private int keyCode;
    private boolean isPressed;
    
    public KeyInput(int keyCode, boolean isPressed){
        this.keyCode = keyCode;
        this.isPressed = isPressed;
    }
        
    public int getKeyCode(){
        return this.keyCode;
    }
    
    public boolean isPressed(){
        return this.isPressed;
    }
}
