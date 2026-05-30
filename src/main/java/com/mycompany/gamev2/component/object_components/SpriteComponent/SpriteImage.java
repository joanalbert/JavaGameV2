/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.object_components.SpriteComponent;

import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.io.image.ImageMaker;
import java.awt.image.BufferedImage;

/**
 *
 * @author J.A
 */
public class SpriteImage {
    
    private Vector3 dimensions;
    private String path;
    private String name;
    private BufferedImage image;
    
    public SpriteImage(int width, int height, String image_path, String image_name){
        this.dimensions = new Vector3(width, height, 0);
        this.name = image_name;
        this.path = image_path;
        this.image = ImageMaker.BufferedImageFromSource(this.path);
    }

    public Vector3 getDimensions() {
        return dimensions;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
    
    
    public BufferedImage getImage(){return this.image;}
}
