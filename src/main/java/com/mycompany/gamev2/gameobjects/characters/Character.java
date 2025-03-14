/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.gameobjects.characters;

import com.mycompany.gamev2.component.object.MovementComponent;
import com.mycompany.gamev2.gameobjects.GameObject;
import com.mycompany.gamev2.input_system.controllers.BaseController;
import com.mycompany.gamev2.input_system.mappings.BaseInputMapping;

/**
 *
 * @author J.A
 */
public abstract class Character extends GameObject{
    private BaseController controller;

    
    @Override
    public void ComponentSetup() {
        super.ComponentSetup(); 
        
        //initialize character specific components
        this.addComponent(MovementComponent.class, new MovementComponent());
    }

    public BaseController getController() {
        return controller;
    }

    protected void setController(BaseController controller) {
        this.controller = controller;
        this.controller.setControlledObject(this);
    }
    
    protected void setInputMapping(BaseInputMapping mapping){
        if(this.controller != null){
            this.controller.setInputMapping(mapping);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if(this.controller != null){
            this.controller.destroy();
            this.controller = null;
        }
    }
    
    
}
