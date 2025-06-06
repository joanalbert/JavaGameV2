/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.levels;

import com.mycompany.gamev2.component.level_components.LevelComponent;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.event_system.level_events.LevelSwitchEvent;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.interfaces.ILevel;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import com.mycompany.gamev2.interfaces.event_listeners.IWorldListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author J.A
 */
public abstract class BaseLevel implements IWorldListener, IGameUpdateListener, ILevel {

    protected HashMap<Class<? extends LevelComponent>, LevelComponent> LevelComponents = new HashMap<>();
    
    protected HashMap<Class<? extends GameObject>, ArrayList<GameObject>> LevelObjects = new HashMap<>();
    
    protected boolean active = false;
    protected String name;

    
    
    public BaseLevel(String name){
        this.name = name;
        EventManager.getInstance().subscribe(this, IWorldListener.class);
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
    }
    
    @Override
    public void onEventReceived(BaseEvent event){
        if(event instanceof LevelSwitchEvent){
            LevelSwitchEvent lse = (LevelSwitchEvent) event;
            if(lse.getNewLevel().equals(this)) level_windup();
            if(lse.getOldLevel() != null && lse.getOldLevel().equals(this)) level_windown();
        }
    }

    @Override
    public void level_windown(){
        for (Map.Entry<Class<? extends GameObject>, ArrayList<GameObject>> entry : LevelObjects.entrySet()) {
            ArrayList<GameObject> game_objects = entry.getValue();
            for(GameObject obj : game_objects) obj.destroy();
            game_objects.clear();
        }
        
        clearGameObjectsV2();
    }

    @Override
    public abstract void level_windup();
    
    
            
    ///////////////////////////////////////////////////////// gameobjects management ///////////////////////
    public GameObject addGameObject(GameObject obj){
        if(obj == null) throw new IllegalArgumentException("argument can't be null");
        
        Class<? extends GameObject> type =  obj.getClass();
        ArrayList<GameObject> objects = LevelObjects.get(type);
        
        if(objects == null){
            objects = new ArrayList<>();
            LevelObjects.put(type, objects);
        }
        
        if(objects.contains(obj)){
            return null;
        }
        
        objects.add(obj);
        return obj;
    }
    
    //this should throw exceptions, i'll look at it later
    public boolean removeGameObject(GameObject obj){
        if(obj == null) throw new IllegalArgumentException("argument can't be null");
        
        Class<? extends GameObject> type =  obj.getClass();
        ArrayList<GameObject> objects = LevelObjects.get(type);
                
        if(objects == null){
            throw new IllegalArgumentException("No objects of type " + type.getSimpleName() + " in level");
        }
        
        boolean removed = objects.remove(obj); 
        if(!removed){
            //throw new IllegalArgumentException("GameObject not found in level: " + obj);
            return false;
        }   
        
        if(objects.isEmpty()) LevelObjects.remove(type);
        
        return true;
    }
    
    public void clearGameObjects(){
        Iterator<Map.Entry<Class<? extends GameObject>, ArrayList<GameObject>>>  i = LevelObjects.entrySet().iterator();
        while(i.hasNext()){
            Map.Entry<Class<? extends GameObject>, ArrayList<GameObject>> entry = i.next();
            ArrayList<GameObject> game_objects = entry.getValue();
            game_objects.clear();
        }
        
        LevelObjects.clear();
        LevelObjects = null;
        System.gc();
    }
    
    public void clearGameObjectsV2(){
        LevelObjects = new HashMap<>();
    }
    
    public <T extends GameObject> ArrayList<T> getAllGameobjectsOfType(Class<T> type){
        ArrayList<GameObject> objects = new ArrayList<>(LevelObjects.get(type));
        ArrayList<T> result_objects = new ArrayList<>();
        for(GameObject obj : objects){
            if(obj.getClass() == type) result_objects.add((T) obj);
        }
        return result_objects;
    }
    ///////////////////////////////////////////////////////////////////////////////////

    
    
    
    
    /////////////////////// components management ///////////////////////////////////
    protected <T extends LevelComponent> boolean addComponent(Class<T> type, LevelComponent comp){
        if(this.LevelComponents.put(type, comp) != null) return true;
        else return false;
    }
    
    public <T extends LevelComponent> T getComponent(Class<T> type){
        LevelComponent comp = this.LevelComponents.get(type);
        if(comp != null && type.isInstance(comp)) return type.cast(comp);
        else return null;
    }
    
    protected <T extends LevelComponent> void removeComponent(LevelComponent comp){
        this.LevelComponents.remove(comp);
    }
    
    public void ComponentSetup(){}
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
