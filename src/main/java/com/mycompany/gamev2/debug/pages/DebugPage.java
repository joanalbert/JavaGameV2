/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.debug.pages;

import com.mycompany.gamev2.debug.menu.VerticalDebugMenu;

/**
 *
 * @author J.A
 */
public class DebugPage {
    
    private String name;
    private VerticalDebugMenu menu;
    
    public DebugPage(String name){
        this.name = name;
        this.menu = new VerticalDebugMenu();
    }
    
    public String getName(){return this.name;}
}
