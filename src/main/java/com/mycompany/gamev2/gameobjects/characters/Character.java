/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects.characters;

import com.mycompany.gamev2.component.object_components.MovementComponent;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.input_system.InputContexts.InputContext;
import com.mycompany.gamev2.input_system.InputManager;
/**
 *
 * @author J.A
 */
public abstract class Character extends GameObject{
   
    protected InputContext input_context;
    
    @Override
    public void ComponentSetup() {
        super.ComponentSetup(); 
        
        //initialize character specific components
        this.addComponent(MovementComponent.class, new MovementComponent());
    }

    
    @Override
    public void destroy() {
        super.destroy();
        if(this.input_context != null){
            InputManager.getInstance().removeContext(input_context);
        }
    }
    
    protected boolean setInputContext(InputContext ctx){
        if(ctx == null) return false;
        this.input_context = ctx;
        return true;
    }
    
    protected abstract void input_setup();
      
}
