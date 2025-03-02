/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system.game_events;

import java.awt.Graphics2D;

/**
 *
 * @author J.A
 */
public class RenderEvent extends BaseEvent {
 
    private Graphics2D g;
    
    public RenderEvent(Graphics2D g){
        this.g = g;
    }
    
    public Graphics2D getGraphics(){
        return this.g;
    }
}
