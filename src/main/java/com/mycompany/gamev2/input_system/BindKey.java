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
    
    public enum Axis{
        X,
        Y,
        Z,
        NONE
    }
    
    private int keyCode;
    private double axisScale;
    private Axis axis;
    
    public BindKey(int code, float axisScale, Axis axis){
        this.keyCode = code;
        this.axisScale = axisScale;
        this.axis = axis;
    }
    
    public int getKeyCode(){return this.keyCode;}
    public double getAxisScale(){return this.axisScale;}
    public Axis getAxis(){return this.axis;}
}
