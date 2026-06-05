/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.Utils;


import com.mycompany.gamev2.gamemath.Vector3;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


/**
 *
 * @author J.A
 */
public class ScreenDrawingUtils {
    
    public static void draw_text(Graphics2D g, String message){
        g.setColor(Color.BLACK);                   
        g.drawString(message, 0,0); 
    }
    
    public static void draw_text(Graphics2D g ,Color color, String message, Vector3 location, Font font){
        
        g.setColor(color);               
        g.setFont(font); 
        
        int x = (int)location.getX();
        int y = (int)location.getY();
        
        g.drawString(message, x, y); 
    }
    
    public static int get_text_width_pixels(String text, Font font) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        FontMetrics fm = g2d.getFontMetrics(font);
        return fm.stringWidth(text);
    }
}
