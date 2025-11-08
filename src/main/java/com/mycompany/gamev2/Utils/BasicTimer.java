/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.gamev2.Utils;

import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;

/**
 *
 * @author J.A
 */
public class BasicTimer implements IGameUpdateListener {
    
    private double limit = 0d;
    private double elapsed;
    private boolean running;
    
    public BasicTimer(){
        this.elapsed = 0;
        this.running = false;
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
    }
    
    public BasicTimer(double limit){
        this.limit = limit;
        this.elapsed = 0;
        this.running = false;
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
    }
    
    public void start(){
        this.running = true;
    }
    
    public void stop(){
        this.running = false;
    }
    
    public void reset(){
        this.elapsed = 0;
    }
    
    public void update(double delta){
        if(this.running){
            this.elapsed+=delta;
            
            if(this.limit > 0d && this.elapsed >= this.limit) {
                System.out.println("complete");
                this.stop();
            }
        }    
    }
    
    @Override
    public void onEventReceived(BaseEvent event) {
        if(event instanceof TickEvent tick_event) this.update(tick_event.getDeltaSeconds());
    }
    
    public void dispose(){
        EventManager.getInstance().unsubscribe(this, IGameUpdateListener.class);
    }

    public double getElapsed(){return this.elapsed;}
    public boolean isRunning(){return this.running;}
}
