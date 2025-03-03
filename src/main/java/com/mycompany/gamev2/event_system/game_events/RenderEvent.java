/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.event_system.game_events;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

/**
 *
 * @author J.A
 */
public class RenderEvent extends BaseEvent {
 
    private Graphics2D g;
    private Canvas canvas;
    private BufferStrategy strat;
    
    public RenderEvent(Canvas canvas, BufferStrategy strat){
        this.canvas = canvas;
        this.strat = strat;
        this.g = (Graphics2D) strat.getDrawGraphics();
    }
    
    public Graphics2D getGraphics(){
        return this.g;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public BufferStrategy getStrat() {
        return strat;
    }
    
    
}
