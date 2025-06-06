/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level_components.grid_component;

import com.mycompany.gamev2.component.level_components.LevelComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.levels.grid.GridLevelBase;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author J.A
 */
public class LevelGridComponent extends LevelComponent {
    
    private LevelGridTile[][] tile_matrix;
    
    private int tile_width = 0;
    private int tile_height = 0;
    private int tile_size = 32;
    
    private GridLevelBase owning_level;
    
    public LevelGridComponent(GridLevelBase owning_level){
        this.owning_level = owning_level;
    }
    
    public LevelGridComponent config_width(int w){
        this.tile_width = w;
        return this;
    } 
    
    public LevelGridComponent config_height(int h){
        this.tile_height = h;
        return this;
    }
    
    public LevelGridComponent config_tile_size(int s){
        this.tile_size = s;
        return this;
    }
    
    public LevelGridComponent construct(){
        tile_matrix = new LevelGridTile[tile_width][tile_height];
        
        for(int x = 0; x < tile_width; x++){
            for(int y = 0; y < tile_height; y++){
                Vector3 grid_pos = new Vector3(x, y, 0);
                Vector3 wndw_loc = new Vector3(x * tile_size, y * tile_size, 0);
                tile_matrix[x][y] = new LevelGridTile("red", grid_pos, wndw_loc);
            }
        }
        
        System.out.println(tile_matrix.toString());
        return this;
    }

    @Override
    public void tick(TickEvent e) {
        
    }

    @Override
    public void render(RenderEvent e) {
        Graphics2D g = e.getGraphics();
        int count = 0;
        for(int x = 0; x < tile_width; x++){
            for(int y = 0; y < tile_height; y++){
                LevelGridTile tile = tile_matrix[x][y];
                Vector3 pos = tile.getWindowLocation();
                g.setColor(Color.pink); 
                g.fillRect((int)pos.getX(), (int)pos.getY(), tile_size, tile_size); 
                
                count++;
            }
        }
        
        System.out.println(count+" DRAW CALLS");
    }
    
    
    
}
