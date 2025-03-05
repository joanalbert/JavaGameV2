/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.window;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;


/**
 *
 * @author J.A
 */
public abstract class MyWindow {
    
    public static JFrame FRAME;
    public static Canvas CNV;
    public static BufferStrategy BUFFER_STRATEGY;
            
    public static final Dimension DIMENSIONS = new Dimension(1200,600);
    
    static void MakeFrame(String name){
        MyWindow.FRAME = new JFrame(name);
        
        
        MyWindow.FRAME.setResizable(false);
        MyWindow.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyWindow.FRAME.setLocationRelativeTo(null);
        
        if(CNV != null) FRAME.add(CNV);
        
        MyWindow.FRAME.pack();
        MyWindow.FRAME.setVisible( true );
    }
    
    
        
   
    public static void Construct_BUFFER_STRATEGY(){
        //init canvas and frame
        CNV = new Canvas();
        CNV.setPreferredSize(DIMENSIONS);
        
        MyWindow.MakeFrame("TEST BUFFER STRAT");
                      
        CNV.createBufferStrategy(2);
        BUFFER_STRATEGY = CNV.getBufferStrategy();
    }
    
   
}
