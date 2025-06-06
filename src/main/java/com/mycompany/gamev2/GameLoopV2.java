/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2;

import com.mycompany.gamev2.debug.DebugFlags;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.GameFinishedEvent;
import com.mycompany.gamev2.event_system.game_events.GameStartEvent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.window.MyWindow;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;




/**
 *
 * @author J.A
 * Main game loop that runs on it's own thread. 
 * It leverages the event system (EventManager) to notify of GameTickEvents 
 * to all pertinent classes (mainly GameObjects)
 */
public class GameLoopV2 implements Runnable {
    
    private static final int FPS = 60;
    private static final long TARGET_TIME = 1000 / FPS;
    private int frames = 0;
    
    private boolean running = false;
    private Thread thread;
    
    private static GameLoopV2 instance;
    
    private GameLoopV2(){}
    
    public static GameLoopV2 getInstance(){
        if(instance == null) instance = new GameLoopV2();
        return instance;
    }
    
    public void start(){
        if(this.running) return;
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }
    
    public void stop(){
        this.running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e){
            String msg = "Wrror when killing loop thread: "+e.getMessage();
            System.out.println(msg);
        }
    }
    
    @Override
    public void run() {
        
        long lastTime = System.nanoTime();
        long now;
        long elapsedTime;
        long waitTime;
        double deltaTime = 0;
        
        EventManager.getInstance().post(new GameStartEvent(), IGameUpdateListener.class);
                
        while(this.running){
            
            now = System.nanoTime();
            elapsedTime = now - lastTime;
            waitTime = TARGET_TIME - elapsedTime / 1000000;
            deltaTime = elapsedTime / 1_000_000_000.0; // Convert nanoseconds to seconds

            if (elapsedTime >= TARGET_TIME * 1000000) {     
                //update
                EventManager.getInstance().post(new TickEvent(deltaTime, frames), IGameUpdateListener.class);
                
                //render
                Graphics2D g = (Graphics2D) MyWindow.BUFFER_STRATEGY.getDrawGraphics();
                g.clearRect(0, 0, MyWindow.DIMENSIONS.width, MyWindow.DIMENSIONS.height);
                EventManager.getInstance().post(new RenderEvent(g), IGameUpdateListener.class);
                render_debug_info(g);
                g.dispose();
                MyWindow.BUFFER_STRATEGY.show();
                
                //update loop vars
                lastTime = now;
                                
                //System.out.println("RUNNING");
            }
            
                        
            if (waitTime > 0) {
                try {
                    this.thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            frames++;
        }
        
        System.out.println("STOPPING....");
        EventManager.getInstance().post(new GameFinishedEvent(), IGameUpdateListener.class);
    }
    
    public void render_debug_info(Graphics2D g){
        
        g.setColor(Color.BLACK);               
        g.setFont(new Font("Arial", Font.BOLD, 24)); 
        
        
        DebugFlags flags = DebugFlags.getInstance();
        
        System.out.println(flags.getShow_debug_FPS());
        
        if(flags.getShow_debug_FPS())
        {
            g.drawString("FPS: 0", 10, 30); 
        }
        
    }
}
