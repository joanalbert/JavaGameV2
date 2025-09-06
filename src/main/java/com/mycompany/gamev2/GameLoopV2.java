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
import java.text.DecimalFormat;




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
    
    private Graphics2D g;
    
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
    
    public void run() {
        long lastTime = System.nanoTime();
        double deltaTimeAccumulator = 0.0;
        final double TARGET_TIME_SECONDS = 1.0 / FPS; // 1/60 ≈ 0.01667 seconds

        EventManager.getInstance().post(new GameStartEvent(), IGameUpdateListener.class);

        while (this.running) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            deltaTimeAccumulator += elapsedTime / 1_000_000_000.0; // Convert to seconds
            lastTime = now;

            // Process as many fixed-timestep updates as needed
            while (deltaTimeAccumulator >= TARGET_TIME_SECONDS) {
                // Update
                TickEvent t_event = new TickEvent(TARGET_TIME_SECONDS, frames);
                EventManager.getInstance().post(t_event, IGameUpdateListener.class);

                // Render
                Graphics2D g = (Graphics2D) MyWindow.BUFFER_STRATEGY.getDrawGraphics();
                this.g = g;
                g.clearRect(0, 0, MyWindow.DIMENSIONS.width, MyWindow.DIMENSIONS.height);

                RenderEvent r_event = new RenderEvent(g);
                EventManager.getInstance().post(r_event, IGameUpdateListener.class);
                render_debug_info(t_event, r_event);

                g.dispose();
                MyWindow.BUFFER_STRATEGY.show();

                // Update loop vars
                deltaTimeAccumulator -= TARGET_TIME_SECONDS;
                frames++;
            }

            // Sleep to yield CPU, but only briefly to check timing frequently
            try {
                Thread.sleep(1); // Short sleep to avoid busy-waiting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("STOPPING....");
        EventManager.getInstance().post(new GameFinishedEvent(), IGameUpdateListener.class);
    }
    /*@Override
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
                TickEvent t_event = new TickEvent(deltaTime, frames);
                EventManager.getInstance().post(t_event, IGameUpdateListener.class);
                
                //render
                Graphics2D g = (Graphics2D) MyWindow.BUFFER_STRATEGY.getDrawGraphics();
                this.g = g;
                g.clearRect(0, 0, MyWindow.DIMENSIONS.width, MyWindow.DIMENSIONS.height);
                
                RenderEvent r_event = new RenderEvent(g);
                EventManager.getInstance().post(r_event, IGameUpdateListener.class);
                render_debug_info(t_event, r_event);
                
                g.dispose();
                MyWindow.BUFFER_STRATEGY.show();
                
                //update loop vars
                lastTime = now;
                                
                //System.out.println("RUNNING");
            }
            
            //waitTime = 200;            
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
    }*/
    
    public Graphics2D requestGraphics(){
        return this.g;
    }
    
    public void render_debug_info(TickEvent t, RenderEvent r){
        Graphics2D g = r.getGraphics();
        double delta = t.getDeltaSeconds();
        
        g.setColor(Color.BLACK);               
        g.setFont(new Font("Arial", Font.BOLD, 24)); 
        
        
        DebugFlags flags = DebugFlags.getInstance();
        
        //System.out.println(flags.getShow_debug_FPS());
        
        if(flags.getShow_debug_FPS())
        {
            double fps = 1/delta;
            DecimalFormat df = new DecimalFormat("#.##");
            g.drawString("FPS: "+df.format(fps)  , 10, 30); 
            g.drawString("FRAME Nº: "+getFrames(), 10, 60);
        }
        
    }
    
    public double getFrames(){return this.frames;}
}
