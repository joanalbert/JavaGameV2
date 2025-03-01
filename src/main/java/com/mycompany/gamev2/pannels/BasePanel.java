/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.pannels;


import com.mycompany.gamev2.window.MyWindow;
import java.awt.Dimension;
import javax.swing.JPanel;


/**
 *
 * @author J.A
 */
public abstract class BasePanel extends JPanel {

    protected String name;
    
   
    public BasePanel(String name){
        this.name = name;
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        return MyWindow.DIMENSIONS;
    }

   
    
    
    public String getPanelName(){return this.name;}
}
