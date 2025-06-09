/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.component.level_components.grid_component;

import com.mycompany.gamev2.gamemath.Vector3;

/**
 *
 * @author J.A
 */
public class LevelGridTile {
    
    private String color;
    private Vector3 window_location;
    private Vector3 grid_position;
    
    public LevelGridTile(String color, Vector3 grid_position, Vector3 window_location){
        this.color = color;
        this.grid_position = grid_position;
        this.window_location = window_location;
    }
    
    //constructor for json parsing
    public LevelGridTile(String color, int[] grid_position){
        this.color = color;
        this.grid_position = new Vector3(grid_position[0], grid_position[1], grid_position[2]);
    }
    
    public void setWindowLocation(Vector3 loc){this.window_location = loc;}
    
    public Vector3 getWindowLocation(){return this.window_location;}
    public Vector3 getGridPosition(){return this.grid_position;}
    public String getColor(){return this.color;}
}
