/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.debug;

import com.mycompany.gamev2.GameLoopV2;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author J.A
 */
public class DebugUtils {
    
     
    private static final class Holder{
        static final DebugUtils INSTANCE = new DebugUtils();
    }
   
    public static DebugUtils getInstance(){
        return Holder.INSTANCE;
    }
    
    private DebugUtils(){
    
    }
    
        
    public void draw_string(String msg){
        Graphics2D g = GameLoopV2.getInstance().requestGraphics();
                
        
        
        g.setColor(Color.BLACK);               
        g.setFont(new Font("Arial", Font.BOLD, 24)); 
        
        g.drawString(msg, 10, 90); 
    }
}
