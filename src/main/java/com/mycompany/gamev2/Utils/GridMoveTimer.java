/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

public class GridMoveTimer implements IGameUpdateListener{
 
    private double prev_t;
    private double t;
    public double duration;
    public double elapsed;
    private double excess_delta;
    private boolean isRunning;
    private boolean hasTriggeredComplete;
    private boolean loop;
    
    private Runnable onComplete;
    
    public GridMoveTimer(double duration, Runnable onComplete){
        this.duration = duration;
        this.onComplete = onComplete;
        this.elapsed = 0;
        this.hasTriggeredComplete = false;
        this.loop = false;
      
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
    }
        
    public GridMoveTimer(double duration, Runnable onComplete, boolean loop){
        this.duration = duration;
        this.onComplete = onComplete;
        this.elapsed = 0;
        this.hasTriggeredComplete = false;
        this.loop = loop;
      
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
    }
    
    @Override
    public void onEventReceived(BaseEvent event) {
        if(this.isRunning && event instanceof TickEvent t_event) this.update(t_event.getDeltaSeconds());
    }
    
    private void update(double delta){
        if(!this.isRunning) return;
        
        this.prev_t = this.getProgress();
        
        this.elapsed += delta;
        
        this.t = this.getProgress();
        
        if(!hasTriggeredComplete && this.getProgress() >= 1d){
            this.excess_delta = this.elapsed - this.duration;
            if(this.onComplete != null) this.onComplete.run();
            this.hasTriggeredComplete = true; //prevent repeated triggers 
            if(this.loop) this.reset();
        }
    }
    
    public void reset(){
        this.elapsed = 0;
        this.hasTriggeredComplete = false;
    }
    
    public void stop(){
        this.isRunning = false;
        this.elapsed = 0;
        this.excess_delta = 0;
        this.hasTriggeredComplete = false;
    }
    
    public void start(){
        this.isRunning = true;
        this.elapsed = (this.excess_delta > 0) ? this.excess_delta : 0;
        this.hasTriggeredComplete = false;
    }
    
    public void restart() {
        this.stop();
        this.excess_delta = 0; // Ensure fresh start
        this.start();
    }
    
    public double getProgress(){
        return Math.min(this.elapsed / this.duration, 1);
    }

    
    
    public double get_t(){return this.t;}
    public double get_prev_t(){return this.prev_t;}
}
