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
    
    public static enum COLISION_TYPE{
        WALK("W"),
        BLOCK("B"),
        SURF("S");
        
        private final String label;

        COLISION_TYPE(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
        
        public static COLISION_TYPE fromLabel(String label) {
            for (COLISION_TYPE type : values()) {
                if (type.label.equals(label)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown colision type label: " + label);
        }
    }

    
    public Vector3 grid_pos;
    public Vector3 atls_pos;
    public Vector3 wndw_loc;
    public COLISION_TYPE colision;
    public int atls_id;
    
    public LevelGridTileV2(Vector3 grid_pos, Vector3 atls_pos, Vector3 wndw_loc, int atls_id, COLISION_TYPE colision){
        this.grid_pos = grid_pos;
        this.atls_pos = atls_pos;
        this.atls_id = atls_id;
        this.wndw_loc = wndw_loc;
        this.colision = colision;
    }
    
    public LevelGridTileV2(double[] grid_pos, double[] atls_pos, double[] wndw_loc, int atls_id, COLISION_TYPE colision){
        this.grid_pos = new Vector3(grid_pos[0], grid_pos[1], 0);
        this.atls_pos = new Vector3(atls_pos[0], atls_pos[1], 0);
        this.wndw_loc = new Vector3(wndw_loc[0], wndw_loc[1], 0);
        this.atls_id = atls_id;
        this.colision = colision;
    }
    
    
    public void setWindowLocation(Vector3 loc){this.wndw_loc = loc;}
    
    public Vector3 getWindowLocation(){return this.wndw_loc;}
    public Vector3 getGridPosition(){return this.grid_pos;}
    
    public COLISION_TYPE getColision(){return this.colision;}
}
