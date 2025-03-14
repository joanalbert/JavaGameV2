/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system;

import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.event_system.input_events.KeyReleaseEvent;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;
import com.mycompany.gamev2.window.MyWindow;
import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 *
 * @author J.A
 */
public class InputManager {
    
    private static HashMap<Integer, Boolean> keyStates = new HashMap<>();
    private static InputManager instance;
    
    private InputManager(){}
    
    public static InputManager getInstance(){
        if(instance == null) instance = new InputManager();
        return instance;
    }
    
    public void CanvasHook(Canvas CNV){
        //hook into our own event system
        CNV.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                
                int pressedKeyCode = e.getKeyCode();
                keyStates.put(pressedKeyCode, true);
                //BaseController controller = LevelManager.getInstance().getFirstActivePlayer().getController(); WE DON'T NEED THIS ATM
                
                //post a input event, with all the available keyinfo
                //if(pressedKeyCode == 72){ //h
                    HashMap<Integer, Boolean> copy_keystates = new HashMap<>(keyStates);
                    EventManager.getInstance().post(new KeyPressEvent(pressedKeyCode, copy_keystates), IInputListener.class);
                //}
                
                //HARDCODED EXIT
                if(e.getKeyCode() == 27){
                    GameLoopV2.getInstance().stop();
                    MyWindow.FRAME.remove(CNV);
                    MyWindow.FRAME.dispose();
                    System.out.println("EXITING...");
                    return;
                }
                
            }
            
             @Override
            public void keyReleased(KeyEvent e) {
                int releasedKeyCode = e.getKeyCode();
                keyStates.put(releasedKeyCode, false);
                HashMap<Integer, Boolean> copy_keystates = new HashMap<>(keyStates);
                EventManager.getInstance().post(new KeyReleaseEvent(releasedKeyCode, copy_keystates), IInputListener.class);
            }
        });
    }
}
