/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects;

import com.mycompany.gamev2.component.level_components.LevelComponent;
import com.mycompany.gamev2.component.object_components.ObjectComponent;
import com.mycompany.gamev2.component.object_components.TransformComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author J.A
 */
public class GameObject {
    
    protected HashMap<Class<? extends ObjectComponent>, ObjectComponent> components = new HashMap<>();
    protected boolean isActive = true;
        
    
    public GameObject(){
        ComponentSetup();
    }
    
    
        
    public void ComponentSetup(){
        System.out.println("GameObject.ComponentSetup for " + getClass().getSimpleName());
        TransformComponent transform = new TransformComponent<GameObject>(this, new Vector3(1,1,1),
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
    
    public <T extends ObjectComponent> T getComponent(Class<T> type){
        ObjectComponent comp = this.components.get(type);
        if(comp != null && type.isInstance(comp)) return type.cast(comp);
        else return null;
    }
    
    public <T extends ObjectComponent> Optional<T> tryGetComponent(Class<T> type) {
        ObjectComponent comp = this.components.get(type);
        if (comp == null || !type.isInstance(comp)) return Optional.empty();
        return Optional.of(type.cast(comp));
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
    
     
    
    public void destroy() {
        this.isActive = false;
        this.components.clear(); // Optional: clear components
    }
    
        
    public void tick(TickEvent e){
        this.tick_components(e);
    }
    
    public void render(RenderEvent e){
        this.render_components(e);
    }
    
    //<editor-fold desc="components updating">
    public void tick_components(TickEvent e){
        Set<Entry<Class<? extends ObjectComponent>, ObjectComponent>> entries = this.components.entrySet();
        Iterator<Entry<Class<? extends ObjectComponent>, ObjectComponent>> i = entries.iterator();
        while(i.hasNext()){
            ObjectComponent component = i.next().getValue();
            component.tick(e);
        }
    }
    
    public void render_components(RenderEvent e){
        Set<Entry<Class<? extends ObjectComponent>, ObjectComponent>> entries = this.components.entrySet();
        Iterator<Entry<Class<? extends ObjectComponent>, ObjectComponent>> i = entries.iterator();
        while(i.hasNext()){
            ObjectComponent component = i.next().getValue();
            component.render(e);
        }
    }
    //</editor-fold>
}
