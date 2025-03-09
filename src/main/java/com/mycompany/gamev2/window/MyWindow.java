/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.window;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.event_system.level_events.LevelSwitchEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;
import com.mycompany.gamev2.interfaces.event_listeners.IWorldListener;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;


/**
 *
 * @author J.A
 */
public abstract class MyWindow {
    
    public static JFrame FRAME;
    public static Canvas CNV;
    public static BufferStrategy BUFFER_STRATEGY;
    
                
    public static final Dimension DIMENSIONS = new Dimension(1200,600);
    
    static void MakeFrame(String name){
        MyWindow.FRAME = new JFrame(name);
        
        
        MyWindow.FRAME.setResizable(false);
        MyWindow.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyWindow.FRAME.setLocationRelativeTo(null);
        
        if(CNV != null) FRAME.add(CNV);
        
        MyWindow.FRAME.pack();
        MyWindow.FRAME.setVisible( true );
        
        
        //we update thw frame's title on level change (something tells me this shouldn't be here, but oh well)
        EventManager.getInstance().subscribe(new IWorldListener(){
            @Override
            public void onEventReceived(BaseEvent event){
                if(event instanceof LevelSwitchEvent){
                    LevelSwitchEvent lse = (LevelSwitchEvent) event;
                    String newWindowTitle = lse.getNewLevel().getName();
                    MyWindow.FRAME.setName(newWindowTitle);
                }
            }
        }, IWorldListener.class);
                
        //or lambda
        /*EventManager.getInstance().subscribe((IWorldListener) (BaseEvent event) -> {
            if(event instanceof LevelSwitchEvent){
                LevelSwitchEvent lse = (LevelSwitchEvent) event;
                String newWindowTitle = lse.getNewLevel().getName();
                MyWindow.FRAME.setName(newWindowTitle);
            }
        }, IWorldListener.class);*/
    }

    
    

    public static void Construct_BUFFER_STRATEGY(){
        //init canvas and frame
        CNV = new Canvas();
        CNV.setPreferredSize(DIMENSIONS);
        
         //make the canvas able to receive input
        CNV.setFocusable(true);
        CNV.requestFocus();
        
        //hook into our own event system
        CNV.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                EventManager.getInstance().post(new KeyPressEvent(e.getKeyCode()), IInputListener.class);
            }
        });
        
        MyWindow.MakeFrame("TEST BUFFER STRAT");
                      
        CNV.createBufferStrategy(2);
        BUFFER_STRATEGY = CNV.getBufferStrategy();
    }
    
   
}
