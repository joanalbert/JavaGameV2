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
public class LevelGridTileV2 {
    
    public Vector3 grid_pos;
    public Vector3 atls_pos;
    public Vector3 wndw_loc;
    public int atls_id;
    
    public LevelGridTileV2(Vector3 grid_pos, Vector3 atls_pos, Vector3 wndw_loc, int atls_id){
        this.grid_pos = grid_pos;
        this.atls_pos = atls_pos;
        this.atls_id = atls_id;
        this.wndw_loc = wndw_loc;
    }
    
    public LevelGridTileV2(double[] grid_pos, double[] atls_pos, double[] wndw_loc, int atls_id){
        this.grid_pos = new Vector3(grid_pos[0], grid_pos[1], 0);
        this.atls_pos = new Vector3(atls_pos[0], atls_pos[1], 0);
        this.wndw_loc = new Vector3(wndw_loc[0], wndw_loc[1], 0);
        this.atls_id = atls_id;
    }
    
    
    public void setWindowLocation(Vector3 loc){this.wndw_loc = loc;}
    
    public Vector3 getWindowLocation(){return this.wndw_loc;}
    public Vector3 getGridPosition(){return this.grid_pos;}
}
