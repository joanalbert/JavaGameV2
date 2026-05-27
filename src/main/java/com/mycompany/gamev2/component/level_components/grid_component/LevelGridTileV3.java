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


public class LevelGridTileV3 {
    
    public static enum COLISION_TYPE{
        
        NONE("NONE",0),
        WALK("WALK",0),
        SURF("SURF",1),
        BLOCK("BLOCK",2);
        
    
        
        private final String label;
        private final int priority;

        COLISION_TYPE(String label, int priority) {
            this.label = label;
            this.priority = priority;
        }

        public String getLabel() {
            return label;
        }
        
        public int getPriority(){
            return priority;
        }
        
        public static COLISION_TYPE fromLabel(String label) {
            
            if(label == null || label.equals("")) return COLISION_TYPE.BLOCK;
            
            for (COLISION_TYPE type : values()) {
                if (type.label.equals(label)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown colision type label: " + label);
        }
    }

    //atlas location and grid location
    public String atlas_id;
    public Vector3 atlas_pos;
    public Vector3 tile_pos;
    
    //extra data
    public int layer_index;
    public COLISION_TYPE collision;
    
    //on screen render location 
    public Vector3 wndw_loc;
    
        
    public LevelGridTileV3(Vector3 tile_pos, Vector3 atlas_pos, Vector3 wndw_loc, String atlas_id, COLISION_TYPE collision){
        this.tile_pos = tile_pos;
        this.atlas_pos = atlas_pos;
        this.wndw_loc = wndw_loc;
        
        this.atlas_id = atlas_id;
        this.collision = collision;
    }
    
    public LevelGridTileV3(double[] tile_pos, double[] atlas_pos, double[] wndw_loc, String atlas_id, COLISION_TYPE collision){
        this.tile_pos = new Vector3(tile_pos[0], tile_pos[1], 0);
        this.atlas_pos = new Vector3(atlas_pos[0], atlas_pos[1], 0);
        this.wndw_loc = new Vector3(wndw_loc[0], wndw_loc[1], 0);
        
        this.atlas_id = atlas_id;
        this.collision = collision;
    }
    
    
    public void setWindowLocation(Vector3 loc){this.wndw_loc = loc;}
    
    public Vector3 getWindowLocation(){return this.wndw_loc;}
    public Vector3 getGridPosition(){return this.tile_pos;}
    
    public COLISION_TYPE getColision(){return this.collision;}
    public int getLayerIndex(){return this.layer_index;}
}
