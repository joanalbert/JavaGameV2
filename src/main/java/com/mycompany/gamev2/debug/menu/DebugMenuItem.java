/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.debug.menu;

import com.mycompany.gamev2.Utils.ScreenDrawingUtils;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author J.A
 */
public class DebugMenuItem {
    
    private boolean selected;
    private String text;
    
    //styles
    private Font font;
    private Color color;
    private int width_pixels;
    
    public DebugMenuItem(String text, Color color, Font font, boolean selected) { //Font(this.font_family, this.font_style, this.font_size)
        
        this.selected = selected;
        
        this.text = text;
        this.color = color;
        this.font = font;
        
        this.width_pixels = ScreenDrawingUtils.get_text_width_pixels(this.text, this.getFont());;
    }

   
    public int getFont_size() {
        return this.font.getSize();
    }

    public void setFont_size(int font_size) {
        Font new_font = new Font(this.font.getFamily(), this.font.getStyle(), font_size);
        this.font = new_font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFont_family() {
        return this.font.getFamily();
    }

    public void setFont_family(String font_family) {
        Font new_font = new Font(font_family, this.font.getStyle(), this.font.getSize());
        this.font = new_font;
    }

    public int getFont_style() {
        return this.font.getStyle();
    }

    public void setFont_style(int font_style) {
        Font new_font = new Font(this.font.getFamily(), font_style, this.font.getSize());
        this.font  = new_font;
    }
    
    public int get_width(){
        return this.width_pixels;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    
    
    
    public Font getFont(){
        return this.font;
    }
    
}
