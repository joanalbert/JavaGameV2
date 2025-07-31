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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *
 * @author J.A
 */
public class LevelGridComponent extends LevelComponent {
    
    private LevelGridTileV2[][] tile_matrix;
    
    private int tile_width = 0;
    private int tile_height = 0;
    private int tile_size = 32;
    private GridLevelBase owning_level;
    private boolean viewpoert_culling;
    private HashMap<Integer, BufferedImage> atlases;
        
    public LevelGridComponent(GridLevelBase owning_level){
        this.owning_level = owning_level;
        this.viewpoert_culling = true;
        load_atlases();
    }
    
    public int getTileSize(){return this.tile_size;}
    public int getTileWidth(){return this.tile_width;}
    public int getTileHeight(){return this.tile_height;}
    
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
        System.out.println(tile_matrix);
        return this;
    }
    
    private void load_atlases(){
        
        this.atlases = new HashMap<Integer, BufferedImage>();
        
        try{
            BufferedImage atlas1 = ImageIO.read(ClassLoader.getSystemResource("textures/tilesets/1_interior.png"));
            BufferedImage atlas2 = ImageIO.read(ClassLoader.getSystemResource("textures/tilesets/2_exterior.png"));
            
            atlases.put(0, atlas1);
            atlases.put(1, atlas2);
           
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void tick(TickEvent e) {
        
    }

    @Override
    public void render(RenderEvent e) {
        if(!this.isActive()) return; //only render if active
        
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
                LevelGridTileV2 tile = tile_matrix[x][y];
                /*Vector3 pos = tile.getWindowLocation();
                g.setColor(Color.pink); 
                g.fillRect((int)pos.getX(), (int)pos.getY(), tile_size, tile_size); */
                render_tile(g, tile);
                draw_calls++;
            }
        }
        
        //System.out.println(draw_calls+" DRAW CALLS");
    }
    
    private void render_viewport_culling(Graphics2D g){
        LevelCameraComponent cam = owning_level.getComponent(LevelCameraComponent.class);
        
        if(cam == null) {
            System.out.println("Viewport culling requires a LevelCameraComponent to be present. We won't render.");
            return;
        }
        
        int draw_calls = 0;
        BoxBounds bounds = cam.getBounds(); 
        
        int startX = Math.max(0, (int) (bounds.getLeft() / tile_size));
        int endX   = Math.min(tile_width, (int) Math.ceil(bounds.getRight() / tile_size));
        
        int startY = Math.max(0, (int) (bounds.getTop() / tile_size));
        int endY   = Math.min(tile_height, (int) Math.ceil(bounds.getBottom() / tile_size));
        
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                LevelGridTileV2 tile = tile_matrix[x][y];
                
                if(tile == null) continue;
                
                render_tile(g, tile);
                
                draw_calls++;
                
            }
        }
        
        
        
        //System.out.println(draw_calls+" DRAW CALLS");
    }
    
    private void render_tile(Graphics2D g, LevelGridTileV2 tile){
        Vector3 pos = tile.getWindowLocation();
        BufferedImage atlas = atlases.get(tile.atls_id-1);
        
        if (atlas == null) {
            // Fallback to drawing a colored rectangle if atlas is missing
            g.setColor(java.awt.Color.RED);
            g.fillRect((int) pos.getX(), (int) pos.getY(), tile_size, tile_size);
            return;
        }
        
        // Source coordinates in the atlas
        int srcX = (int) tile.atls_pos.getX();
        int srcY = (int) tile.atls_pos.getY();
        // Destination coordinates on the screen
        int destX = (int) pos.getX();
        int destY = (int) pos.getY();

        // Draw the specific region of the atlas
        g.drawImage(atlas, 
            destX, destY, destX + tile_size, destY + tile_size, // Destination rectangle
            srcX, srcY, srcX + 16, srcY + 16,     // Source rectangle
            null);
    }
}
