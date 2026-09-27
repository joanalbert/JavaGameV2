/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.debug.menu;

import com.mycompany.gamev2.Utils.ScreenDrawingUtils;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author J.A
 */
public class VerticalDebugMenu extends DebugMenu {

    private int up  = KeyEvent.VK_UP;
    private int down = KeyEvent.VK_DOWN;

    @Override
    public void right(int key) {
        if(key != this.down) return;
        int max = this.menu_items.size();
        if(this.selection_index + 1 < max) this.selection_index++;
        else this.selection_index = 0;
        this.update_selected();
    }

    @Override
    public void left(int key) {
        if(key != this.up) return;
        if(this.selection_index - 1 >= 0) this.selection_index--;
        else this.selection_index = this.menu_items.size()-1;
        this.update_selected();
    }
    
    

    @Override
    public void render(RenderEvent e) {
        
       Graphics2D g = e.getGraphics();
        
       int i = 0;
       int height_previous=0;
       
       for(DebugMenuItem item : this.menu_items){
           
           int height = item.get_height();
           
           Vector3 y_offset = Vector3.DOWN.getScaled(height_previous);
           Vector3 final_drawing_position = this.menu_position.plus(y_offset);
           
           Color c = item.isSelected() ? Color.BLUE : item.getColor();
           ScreenDrawingUtils.draw_text(g, c, item.getText(), final_drawing_position, item.getFont());
           
           i++;
           height_previous += height + this.items_margin_px;
       }
       
    }
    
    
    
    
}
