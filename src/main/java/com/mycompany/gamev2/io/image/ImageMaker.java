/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.gamev2.io.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author J.A
 */
public abstract class ImageMaker {
    
    public static BufferedImage BufferedImageFromSource(String src){
        
        try{
            return ImageIO.read(ClassLoader.getSystemResource(src));
        } 
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        
        return null;
    }
}
