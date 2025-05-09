/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system;

import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.event_system.input_events.KeyReleaseEvent;
import com.mycompany.gamev2.input_system.InputActions.InputAction;
import com.mycompany.gamev2.input_system.InputContexts.InputContext;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;
import com.mycompany.gamev2.window.MyWindow;
import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author J.A
 */
public class InputManager {
    
    private static HashMap<Integer, Boolean> keyStates = new HashMap<>();
    private static InputManager instance;
    private List<InputContext> activeContexts;
    
    private InputManager(){this.activeContexts = new ArrayList<>();}
    
    public static InputManager getInstance(){
        if(instance == null) instance = new InputManager();
        return instance;
    }
    
    public boolean addContext(InputContext context){
        try{this.activeContexts.add(context);}
        catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
        //sort by priority (higher first)
        this.activeContexts.sort(Comparator.comparingInt(InputContext::getPriority).reversed());
        return true;
    }
    
    public boolean removeContext(InputContext context){
        try{
            this.activeContexts.remove(context);
            return true;
        } catch(Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
    }
    
    public void processInput(KeyInput input){
        keyStates.put(input.getKeyCode(), input.isPressed());
        
        //process all active inputContexts by priority order
        for(InputContext context : this.activeContexts){
            for(InputBinding binding : context.getBindings()){
                
                InputAction action  = binding.getAction();
                
                System.out.println(action.getName());
                
                //is axis
                if(binding.isAxis()){
                    //2D
                    if(action.getType() == InputAction.ActionType.AXIS_2D){
                    
                        // whatever should be done here, should be handled by each
                        // 'receiiver' or 'user' or 'controller' of this action
                        float[] currentValues = action.getAxisValues();
                        action.setAxisValues(currentValues[0], currentValues[1]);
                        
                    }
                    //3D
                    else if (action.getType() == InputAction.ActionType.AXIS_1D){
                        
                    }
                    
                //is discrete
                } else {
                    // Handle triggered input
                    action.setActive(input.isPressed());
                }
            }
        }
        
        // Post input events for legacy compatibility
        HashMap<Integer, Boolean> copyKeyStates = new HashMap<>(keyStates);
        if (input.isPressed()) {
            EventManager.getInstance().post(new KeyPressEvent(input.getKeyCode(), copyKeyStates), IInputListener.class);
        } else {
            EventManager.getInstance().post(new KeyReleaseEvent(input.getKeyCode(), copyKeyStates), IInputListener.class);
        }
    }
    
    
    public void CanvasHook(Canvas CNV){
        //hook into our own event system
        CNV.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                processInput(new KeyInput(keyCode, true));
                
                
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
                int keyCode = e.getKeyCode();
                processInput(new KeyInput(keyCode, false));
            }
        });
    }
}
