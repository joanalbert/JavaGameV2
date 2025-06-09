/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level_components.grid_component;

import com.mycompany.gamev2.component.level_components.LevelComponent;
import com.mycompany.gamev2.component.level_components.camera_component.LevelCameraComponent;
import com.mycompany.gamev2.event_system.game_events.RenderEvent;
import com.mycompany.gamev2.event_system.game_events.TickEvent;
import com.mycompany.gamev2.gamemath.BoxBounds;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.io.JsonReader;
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
    
    private boolean viewpoert_culling;
        
    public LevelGridComponent(GridLevelBase owning_level){
        this.owning_level = owning_level;
        this.viewpoert_culling = true;
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
    
    public LevelGridComponent config_viewport_culling(boolean setting){
        this.viewpoert_culling = setting;
        return this;
    }
    
    
    public LevelGridComponent construct_fromJSON(String json_path){
        JsonReader reader = JsonReader.getInstance();
        tile_matrix = reader.getTileMatrixFromJSON(json_path, this.tile_width, this.tile_height, this.tile_size); // thse dimensions should be defined in the json itself
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
        
        if(viewpoert_culling){
            render_viewport_culling(g);
        }
        else render_naive(g);
    }
    
    
    private void render_naive(Graphics2D g){
        int draw_calls = 0;
        for(int x = 0; x < tile_width; x++){
            for(int y = 0; y < tile_height; y++){
                LevelGridTile tile = tile_matrix[x][y];
                Vector3 pos = tile.getWindowLocation();
                g.setColor(Color.pink); 
                g.fillRect((int)pos.getX(), (int)pos.getY(), tile_size, tile_size); 
                
                draw_calls++;
            }
        }
        
        System.out.println(draw_calls+" DRAW CALLS");
    }
    
    private void render_viewport_culling(Graphics2D g){
        LevelCameraComponent cam = owning_level.getComponent(LevelCameraComponent.class);
        if(cam == null) return;
        
        int draw_calls = 0;
        BoxBounds bounds = cam.getBounds(); 
        
        int startX = Math.max(0, (int) (bounds.getLeft() / tile_size));
        int endX   = Math.min(tile_width, (int) Math.ceil(bounds.getRight() / tile_size));
        
        int startY = Math.max(0, (int) (bounds.getTop() / tile_size));
        int endY   = Math.min(tile_height, (int) Math.ceil(bounds.getBottom() / tile_size));
        
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                LevelGridTile tile = tile_matrix[x][y];
                
                if(tile == null) continue;
                
                Vector3 pos = tile.getWindowLocation();
                
                Color c = Color.decode(tile.getColor());
                
                g.setColor(c); 
                g.fillRect((int)pos.getX(), (int)pos.getY(), tile_size, tile_size); 
                
                draw_calls++;
            }
        }
        
        /*System.out.printf("Camera: left=%.0f, right=%.0f, top=%.0f, bottom=%.0f, w=%.0f, h=%.0f%n",
    bounds.getLeft(), bounds.getRight(), bounds.getTop(), bounds.getBottom(),
        bounds.getRight() - bounds.getLeft(), bounds.getBottom() - bounds.getTop());*/
        
  /*Divide by tile_size: Maps world pixels to grid units (32 pixels = 1 tile).
    Floor ((int)): Picks the tile containing left/top for loop start.
    Ceil: Includes partial tiles at right/bottom for loop end.
    Max(0): Handles negative bounds (camera near (0, 0)).
    Min(tile_width/height): Caps at grid size (1700).*/
        
        System.out.println(draw_calls+" DRAW CALLS");
    }
}
