/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputActions;

/**
 *
 * @author J.A
 */
public abstract class InputAction {
    protected boolean active = false;
    
    protected void setActive(){
        active = !active;
    }
}
