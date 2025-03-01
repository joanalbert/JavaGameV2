/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.window;
import com.mycompany.gamev2.pannels.MyPanel;
import java.awt.Dimension;
import javax.swing.JFrame;


/**
 *
 * @author J.A
 */
public abstract class MyWindow {
    
    public static JFrame FRAME;
    public static MyPanel PANNEL;
    public static final Dimension DIMENSIONS = new Dimension(1200,600);
    
    static void MakeFrame(String name){
        MyWindow.FRAME = new JFrame(name);
        
        
        MyWindow.FRAME.setResizable(false);
        MyWindow.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyWindow.FRAME.setLocationRelativeTo(null);
        
        if(MyWindow.PANNEL != null) MyWindow.FRAME.add(MyWindow.PANNEL);
        
        MyWindow.FRAME.pack();
        MyWindow.FRAME.setVisible( true );
    }
    
    
    static void MakePanel(){
        MyWindow.PANNEL = new MyPanel("MyPannel");
    }
    
    public static void Construct(String name){
        MyWindow.MakePanel();
        MyWindow.MakeFrame(name);
    }
}
