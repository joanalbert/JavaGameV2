/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.io;


import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV3;
import com.mycompany.gamev2.gamemath.Vector3;

/**
 *
 * @author J.A
 */
public class GridAndDimensionsWrapper {
    private LevelGridTileV3[][] grid;
    private Vector3 dimensions;
    
    public GridAndDimensionsWrapper(LevelGridTileV3[][] grid, Vector3 dimensions){
        this.grid = grid;
        this.dimensions = dimensions;
        
    }
    
    public LevelGridTileV3[][] getGrid(){return this.grid;}
    public Vector3 getDimensions(){return this.dimensions;}
}
