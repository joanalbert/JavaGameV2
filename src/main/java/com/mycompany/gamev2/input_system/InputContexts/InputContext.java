/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.input_system.InputContexts;

import com.mycompany.gamev2.input_system.InputBinding;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J.A
 */
public abstract class InputContext {
    private String name;
    private int priority; //higher priority contexts are evaluated first
    private List<InputBinding> bindings;
    
    public InputContext(String name, int priority){
        this.name = name;
        this.priority = priority;
        this.bindings = new ArrayList<>();
    }
    
    public boolean addBinding(InputBinding binding){
        try{
            bindings.add(binding);
            return true;
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
    }
    
    public boolean removeBinding(InputBinding binding){
        try{
            bindings.remove(binding);
            return true;
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }
    }
    
     public List<InputBinding> getBindings() {
        return bindings;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }
}
