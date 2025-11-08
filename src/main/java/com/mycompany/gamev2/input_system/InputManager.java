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
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.window.MyWindow;
import java.awt.Canvas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
    
    private static HashMap<Integer, Boolean> keyStates      = new HashMap<>();
    private static HashMap<Integer, Boolean> keyStates_prev = new HashMap<>();
    private Vector3 mousePosition      = Vector3.ZERO;
    private Vector3 mousePosition_prev = Vector3.ZERO;
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
       
        /*if(input.isPressed()) keyStates.put(input.getKeyCode(), input.isPressed());
        else if(!input.isPressed() && keyStates.containsKey(input.getKeyCode()))
        {
           keyStates.remove(input.getKeyCode()); 
        }*/
        
        if (input.isPressed()) {
            keyStates.put(input.getKeyCode(), true);
        } else {
            keyStates.remove(input.getKeyCode()); 
        }
        
        //System.out.println(keyStates.toString());
    }
    
    public void UpdateContexts(TickEvent event){

        //copy the map for ease of debugging
        HashMap<Integer, Boolean> copyStates = new HashMap<Integer, Boolean>(keyStates);
        
        //LOOP CONTEXTS process all active inputContexts by priority order
        for(InputContext context : this.activeContexts){
            
            //LOOP BINDINGS
            for(InputBinding binding : context.getBindings()){
                           
                InputAction action  = binding.getAction();          
                               
                //is axis
                if(binding.isAxis())
                {
                    //2D
                    if(action.getType() == InputAction.ActionType.AXIS_2D)
                    {
                        //System.out.println("Processing: "+context.getName()+": "+action.getName());         
                        
                        
                        Set<BindKey> activeBindKeys = binding.getActiveBindKeys(copyStates);
                        action.evaluateAxes(activeBindKeys, event.getDeltaSeconds());
                                            
                        
                        //System.out.println(action.getAxisValues()[0]+"/"+action.getAxisValues()[1]);
                    }
                    //1D
                    else if (action.getType() == InputAction.ActionType.AXIS_1D)
                    {
                        // handle stuff
                        // no 1 dimensional axis input axtions exist as of yet
                    }
                    
                //is discrete
                } else{
                    // Handle trigger inputs
                    // no discrete input actions exist as of yet
                    
                    if(!ShouldSkip(binding)){
                        if(binding.getIsOneShot()){
                            if(isNewlyPressed(binding, new HashMap<Integer, Boolean>(keyStates))) {
                                action.trigger();
                            }
                        }
                        else action.trigger();
                        
                    } 
                    
                }
            }
        }
        
        keyStates_prev = new HashMap<Integer, Boolean>(keyStates);
        mousePosition_prev = mousePosition;
    }
    
    // Check if any bound key was just pressed 
    private boolean isNewlyPressed(InputBinding binding, HashMap<Integer, Boolean> currentStates) {
        for (BindKey k : binding.getBindKeys()) {
            
            int keyCode = k.getKeyCode();
            boolean isCurrentlyPressed = currentStates.containsKey(keyCode) && currentStates.get(keyCode);
            boolean wasPreviouslyPressed = keyStates_prev.containsKey(keyCode) && keyStates_prev.get(keyCode);
            
            if (isCurrentlyPressed && !wasPreviouslyPressed) {
                return true; // Key was just pressed
            }
        }
        return false;
    }
    
    //if no keys are pressed for this binding it should be skipped, unless it's "held"
    public boolean ShouldSkip(InputBinding binding){
        HashMap<Integer, Boolean> copy_states = new HashMap<Integer, Boolean>(keyStates);
        boolean doKeysMatch = binding.matches(copy_states);
        if(!doKeysMatch && !binding.getIsHeld()){
            return true;
        }
        else return false;
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
                
                //TOGGLE DEBUG TEXT
                int code = e.getKeyCode();
                if(code == KeyEvent.VK_P || code == KeyEvent.VK_I){
                    EventManager.getInstance().post(new KeyPressEvent(code, new HashMap<Integer, Boolean>(keyStates)), IInputListener.class);
                }
            }
            
             @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                UpdateStates(new KeyInput(keyCode, false));
            }
        });
        
        
        // Add mouse motion listener
        CNV.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                InputManager.getInstance().mousePosition = new Vector3(e.getX(), e.getY(), 0);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                InputManager.getInstance().mousePosition = new Vector3(e.getX(), e.getY(), 0); // Update during drags too
            }
        });
        
        // Mouse click listener
        CNV.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Left click
                    //InputManager.mousePosition = new Vector3(e.getX(), e.getY(), 0);
                    //System.out.println(mousePosition);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    //InputManager.mousePosition = new Vector3(e.getX(), e.getY(), 0);
                }
            }
        });
    }
    
    
    
    
    public Vector3 getMousePos(){return mousePosition;}
    public Vector3 getMousePos_prev(){return mousePosition_prev;}
}
