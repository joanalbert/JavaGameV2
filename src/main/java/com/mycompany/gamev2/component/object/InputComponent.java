/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object;

import com.mycompany.gamev2.input_system.mappings.BaseInputMapping;

/**
 *
 * @author J.A
 */
public class InputComponent extends ObjectComponent {
    private BaseInputMapping currentMapping;

    
    public BaseInputMapping getMapping() {
        return currentMapping;
    }

    public void setMapping(BaseInputMapping currentMapping) {
        this.currentMapping = currentMapping;
    }
    
    
} 
