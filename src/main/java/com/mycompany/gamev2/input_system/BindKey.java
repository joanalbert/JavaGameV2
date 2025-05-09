/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system;

/**
 *
 * @author J.A
 */
public class BindKey {
    private int keyCode;
    private float axisScale;
    
    public BindKey(int code, float axisScale){
        this.keyCode = code;
        this.axisScale = axisScale;
    }
    
    public int getKeyCode(){return this.keyCode;}
    public float getAxisScale(){return this.axisScale;}
}
