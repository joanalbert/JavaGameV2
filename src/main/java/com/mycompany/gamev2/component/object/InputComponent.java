/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object;

import com.mycompany.gamev2.input_system.mappings.ActionMapping;

/**
 *
 * @author J.A
 */
public class InputComponent extends ObjectComponent {
    private ActionMapping currentMapping;

    
    public ActionMapping getMapping() {
        return currentMapping;
    }

    public void setMapping(ActionMapping currentMapping) {
        this.currentMapping = currentMapping;
    }
    
    
} 
