/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects;

import com.mycompany.gamev2.component.object.ObjectComponent;
import com.mycompany.gamev2.component.object.TransformComponent;
import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.interfaces.event_listeners.IGameUpdateListener;
import java.util.HashMap;

/**
 *
 * @author J.A
 */
public class GameObject implements IGameUpdateListener  {
    
    protected HashMap<Class<? extends ObjectComponent>, ObjectComponent> components = new HashMap<>();
    
    protected boolean isActive = true;
    
    public GameObject(){
        ComponentSetup();
        EventManager.getInstance().subscribe(this, IGameUpdateListener.class);
    }
    
    
    public void ComponentSetup(){
        TransformComponent transform = new TransformComponent(new Vector3(1,1,1),
                                                              new Vector3(0,0,0),
                                                              new Vector3(0,0,0));
        this.addComponent(TransformComponent.class, transform);        
    }
    
    public HashMap<Class<? extends ObjectComponent>, ObjectComponent> getComponents(){
        return this.components;
    }
    
    protected <T extends ObjectComponent> boolean addComponent(Class<T> type, ObjectComponent comp){
        if(this.components.put(type, comp) != null) return true;
        else return false;
    }
    
    protected <T extends ObjectComponent> T getComponent(Class<T> type){
        ObjectComponent comp = this.components.get(type);
        if(comp != null && type.isInstance(comp)) return type.cast(comp);
        else return null;
    }
    
    protected <T extends ObjectComponent> void removeComponent(ObjectComponent comp){
        this.components.remove(comp);
    }
    
    
    public Vector3 getObjectLocation(){
        return this.getComponent(TransformComponent.class).getLocation();
    }
    
    public void setObjectLocation(Vector3 newLocation){
        this.getComponent(TransformComponent.class).setLocation(newLocation);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
   
    
    
    @Override
    public void onEventReceived(BaseEvent event) {
        if (!isActive) return;
        
    }
}
