/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputActions;

/**
 *
 * @author J.A
 */
public class IA_Walk extends InputAction {
    
    public IA_Walk(){
        super("IA_Walk", ActionType.AXIS_2D);
    }
    
    @Override
    public void setAxisValues(float x, float y){
        super.setAxisValues(x, y);
    }
}
