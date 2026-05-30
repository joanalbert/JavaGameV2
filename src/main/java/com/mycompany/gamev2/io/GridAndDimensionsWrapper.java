/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.io;


import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTile;
import com.mycompany.gamev2.gamemath.Vector3;


/**
 *
 * @author J.A
 */
public class GridAndDimensionsWrapper {
    private LevelGridTile[][][] grid;
    private Vector3 dimensions;
    private int layer_count;
    
    public GridAndDimensionsWrapper(LevelGridTile[][][] grid, Vector3 dimensions, int layer_count){
        this.grid = grid;
        this.dimensions = dimensions;
        this.layer_count = layer_count;
    }
    
    public LevelGridTile[][][] getGrid(){return this.grid;}
    public Vector3 getDimensions(){return this.dimensions;}
    public int getLayerCount(){return this.layer_count;}
}
