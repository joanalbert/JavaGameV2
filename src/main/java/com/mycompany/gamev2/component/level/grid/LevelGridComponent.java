/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level.grid;

import com.mycompany.gamev2.component.level.LevelComponent;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.levels.grid.GridLevelBase;

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
        System.out.println("QQQQQQQQQQQQQQQQQQQQQ");
        return this;
    }
}
