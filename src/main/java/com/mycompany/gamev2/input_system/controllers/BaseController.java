/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.controllers;

import com.mycompany.gamev2.event_system.EventManager;
import com.mycompany.gamev2.event_system.game_events.BaseEvent;
import com.mycompany.gamev2.gameobjects.characters.Character;
import com.mycompany.gamev2.input_system.mappings.ActionMapping;
import com.mycompany.gamev2.interfaces.event_listeners.IInputListener;

/**
 *
 * @author J.A
 */
public abstract class BaseController implements IInputListener{
    
    protected Character controlledObject;
    protected ActionMapping<?> mapping;
    protected int id = 0;
    
    public static int count = 0;

    public BaseController(){
        EventManager.getInstance().subscribe(this, IInputListener.class);
        BaseController.count ++;
        this.id = BaseController.count;
    }
    
    @Override
    public abstract void onEventReceived(BaseEvent event);
    

    public void setControlledObject(Character controlledCharacter) {
        BaseController c = controlledCharacter.getController();
        if(c != null && c.equals(this))
        {
            this.controlledObject = controlledCharacter;
        }
    }
    
    public void setActionMapping(ActionMapping<?> am){
        if(am == null) return;
        this.mapping = am;
        this.controlledObject.updateActionMapping(am);
    }

    public ActionMapping<?> getMapping() {
        return mapping;
    }
    
    
   
    public void destroy(){
        EventManager.getInstance().unsubscribeAll(this);
    }
}
