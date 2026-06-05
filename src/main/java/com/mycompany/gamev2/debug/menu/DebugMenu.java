/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.debug.menu;

import com.mycompany.gamev2.Utils.ScreenDrawingUtils;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J.A
 */
public class DebugMenu {
    
    private Vector3 menu_position;
    private List<DebugPage> pages;
    private List<DebugMenuItem> menu_items;
    private int selection_index;
    private int items_margin_px;
    
    public DebugMenu(){
        
        //data
        this.pages = new ArrayList<DebugPage>();
        this.menu_items = new ArrayList<DebugMenuItem>();
        
        //rendering
        this.menu_position = new Vector3(10, 30, 0);
        this.items_margin_px = 10;
        
        //usage
        this.selection_index = 0;

    }
    
    public void add_page(DebugPage page){
        this.pages.add(page);
        this.refresh_menu();
    }
    
    public void remove_page(DebugPage page){
        if(this.pages.contains(page)) this.pages.remove(page);
        this.refresh_menu();
    }
    
       
    private void refresh_menu(){
        
        this.menu_items.clear();
        
        int count = 0;
        for(DebugPage page: this.pages){
            String name = page.getName();
            
            boolean selected = this.selection_index == count;
            DebugMenuItem item = new DebugMenuItem(name, Color.BLACK, new Font(Font.SANS_SERIF, Font.BOLD, 18), selected);
            this.menu_items.add(item);
            count++;
        }
    }
    
    public void render(RenderEvent e){
       Graphics2D g = e.getGraphics();
        
       int i = 0;
       int width_previous=0;
       
       for(DebugMenuItem item : this.menu_items){
           
           int width = item.get_width();
           
           Vector3 x_offset = Vector3.RIGHT.getScaled(width_previous);
           Vector3 final_drawing_position = this.menu_position.plus(x_offset);
           
           Color c = item.isSelected() ? Color.RED : item.getColor();
           ScreenDrawingUtils.draw_text(g, c, item.getText(), final_drawing_position, item.getFont());
           
           i++;
           width_previous += width + this.items_margin_px;
       }
       
    }
    
    public void left(){
        if(this.selection_index - 1 >= 0) this.selection_index--;
        else this.selection_index = this.menu_items.size()-1;
        
        this.update_selected();
    }
    
    public void right(){
        int max = this.menu_items.size();
        if(this.selection_index + 1 < max) this.selection_index++;
        else this.selection_index = 0;
        
        this.update_selected();
    }
    
    private void update_selected(){
        this.deselect_all();
        this.menu_items.get(this.selection_index).setSelected(true);
    }
    
    private void deselect_all(){
        for(DebugMenuItem item : this.menu_items) item.setSelected(false);
    }
}
