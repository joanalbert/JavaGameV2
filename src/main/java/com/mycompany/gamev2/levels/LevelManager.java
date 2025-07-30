/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels;

import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.game_events.GameStartEvent;
import com.mycompany.gamev2.event_system.input_events.KeyPressEvent;
import com.mycompany.gamev2.event_system.level_events.LevelSwitchEvent;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.gameobjects.characters.PlayerCharacter;
import com.mycompany.gamev2.interfaces.ILevel;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;
import com.mycompany.gamev2.interfaces.event_listeners.IWorldListener;
import com.mycompany.gamev2.levels.grid.GridLevel_01;
import com.mycompany.gamev2.levels.grid.GridLevel_02;
import java.util.ArrayList;

/**
 *
 * @author J.A
 */
public class LevelManager implements IInputListener, IGameUpdateListener{

    private ArrayList<ILevel> levels = new ArrayList<>();
    private BaseLevel current_level;
    
    public static LevelManager instance;
        
    private LevelManager(){
        EventManager.getInstance().subscribe(this, IInputListener.class);
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
        
        levels.add(new TestLevel_01());
        levels.add(new TestLevel_02());
        levels.add(new GridLevel_01());
        levels.add(new GridLevel_02());
    }
    
    
    
    public static LevelManager getInstance(){
        if(instance == null) instance = new LevelManager();
        return instance;
    }
    
    public void switchLevel(BaseLevel level){
        if(level == null) throw new IllegalArgumentException("level can't be null");
        
        if(current_level != null)
        {
            current_level.setActive(false);
        }
        
        //send out a level switch event; can't think of any possible interested listeners as of yet, but hey, it'll prove useful in the future
        LevelSwitchEvent lse = new LevelSwitchEvent(current_level, level);
        EventManager.getInstance().post(lse, IWorldListener.class);


        //actually switch the level
        current_level = level;       
        current_level.setActive(true);
    }
    
    public ILevel nextLevel(){
        ILevel next = levels.remove(0);
        levels.add(next);
        return levels.get(0);
    }
    
        
    @Override
    public void onEventReceived(BaseEvent event) {
        
        if(event instanceof KeyPressEvent){

            KeyPressEvent kpe = (KeyPressEvent) event;
           
            if(kpe.getKeyCode() == 76){ //l
                System.out.println("Switching levels..");
                BaseLevel next = (BaseLevel) nextLevel();
                System.out.println("Switching to: "+next.getName());
                switchLevel(next);
            }
        }
        
        if(event instanceof GameStartEvent){
            GameStartEvent gse = (GameStartEvent) event;
            System.out.println("STARTING to level..");
                //BaseLevel next = (BaseLevel) nextLevel();
                //System.out.println("Switching to: "+next.getName());
                switchLevel((BaseLevel)levels.get(0));
        }
    }
    
    
    public <T extends GameObject> ArrayList<T> getAllGameobjectsOfType(Class<T> type){
        if(this.current_level == null) return null;
        return this.current_level.getAllGameobjectsOfType(type);
    }
    
    public PlayerCharacter getFirstActivePlayer(){
        if(this.current_level == null) return null;
        ArrayList<PlayerCharacter> result = getAllGameobjectsOfType(PlayerCharacter.class);
        if(result == null) return null;
        return result.get(0);
    }
    
    public BaseLevel getCurrentLevel(){return this.current_level;}
}
