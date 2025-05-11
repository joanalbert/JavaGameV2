/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system.game_events;

/**
 *
 * @author J.A
 */
public class TickEvent extends BaseEvent {
    private double deltaSeconds;
    private int frames = 0;
    
    public TickEvent(double ds){
        this.deltaSeconds = ds;
    }
    
    public TickEvent(double ds, int frames){
        this.deltaSeconds = ds;
        this.frames = frames;
    }
    
    public int getFrames(){return this.frames;}
    public double getDeltaSeconds(){return this.deltaSeconds;}
}
