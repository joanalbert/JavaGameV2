/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system;

import com.mycompany.gamev2.GameLoopV2;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.input_system.InputActions.InputAction;
import com.mycompany.gamev2.input_system.InputContexts.InputContext;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;
import com.mycompany.gamev2.interfaces.event_listeners.IWorldListener;
import com.mycompany.gamev2.window.MyWindow;
import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author J.A
 */
public class InputManager implements IGameUpdateListener {
    
    private static HashMap<Integer, Boolean> keyStates = new HashMap<>();
    private static InputManager instance;
    private List<InputContext> activeContexts;
    
    private InputManager(){
        this.activeContexts = new ArrayList<>();
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
    }
    
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

    @Override
    public void onEventReceived(BaseEvent event) {
        if(event instanceof TickEvent) UpdateContexts((TickEvent) event);
    }
    
    
    public void UpdateStates(KeyInput input){
        keyStates.put(input.getKeyCode(), input.isPressed());
        /*if(input.isPressed()) keyStates.put(input.getKeyCode(), input.isPressed());
        else if(!input.isPressed() && keyStates.containsKey(input.getKeyCode()))
        {
           keyStates.remove(input.getKeyCode()); 
        }*/
        
        //System.out.println(keyStates.toString());
    }
    
    public void UpdateContexts(TickEvent event){
       
        //LOOP CONTEXTS process all active inputContexts by priority order
        for(InputContext context : this.activeContexts){
            
            //LOOP BINDINGS
            for(InputBinding binding : context.getBindings()){
                           
                //this copy is for ease of debuggin only                
                HashMap<Integer, Boolean> copyStates = new HashMap<Integer, Boolean>(keyStates);
                boolean doKeysMatch = binding.matches(copyStates);
                if(!doKeysMatch || !binding.getIsHeld() ) continue;
                //else System.out.println("processing binding");
                
                
                InputAction action  = binding.getAction();          
                               
                //is axis
                if(binding.isAxis())
                {
                    //2D
                    if(action.getType() == InputAction.ActionType.AXIS_2D)
                    {
                        //System.out.println("Processing: "+context.getName()+": "+action.getName());         
                        
                        //set action data
                        Set<BindKey> activeBindKeys = binding.getActiveBindKeys(copyStates);
                        action.evaluateAxes(activeBindKeys);
                        
                        //System.out.println(action.getAxisValues()[0]+"/"+action.getAxisValues()[1]);
                        
                        //run action callback
                        double p = Math.PI;
                    }
                    //3D
                    else if (action.getType() == InputAction.ActionType.AXIS_1D)
                    {
                        // handle stuff
                        // no 1 dimensional axis input axtions exist as of yet
                    }
                    
                //is discrete
                } else{
                    // Handle triggered input
                    // no discreen input actions exist as of yet
                }
            }
        }
        
     
    }
    
    
    public void CanvasHook(Canvas CNV){
        //hook into our own event system
        CNV.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                UpdateStates(new KeyInput(keyCode, true));
                
                
                //HARDCODED EXIT
                if(e.getKeyCode() == 27){
                    GameLoopV2.getInstance().stop();
                    MyWindow.FRAME.remove(CNV);
                    MyWindow.FRAME.dispose();
                    System.out.println("EXITING...");
                    return;
                }
                
                //HARDCODED LEVEL SWITCH
                if(e.getKeyCode() == KeyEvent.VK_L){
                    EventManager.getInstance().post(new KeyPressEvent(KeyEvent.VK_L, new HashMap<Integer, Boolean>(keyStates)), IInputListener.class);
                    System.out.println("telling the level manager to switch level");
                }
            }
            
             @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                UpdateStates(new KeyInput(keyCode, false));
            }
        });
    }
}
