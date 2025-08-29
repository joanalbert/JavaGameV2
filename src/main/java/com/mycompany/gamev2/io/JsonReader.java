/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV2;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.io.gson_deserializers.ColisionTypeDeserializer;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 * @author J.A
 */
public class JsonReader {
    
    private static JsonReader instance = null;
    
    private JsonReader(){
    
    }
    
    public static JsonReader getInstance(){
        if(instance == null) instance = new JsonReader();
        return instance;
    }
    
    
    public GridAndDimensionsWrapper getTileMatrixFromJSON(String json_path, int size){
        //we use GsonBuilder in order to register our deserializer fro LevelGridTileV2.COLISION_TYPE
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LevelGridTileV2.COLISION_TYPE.class, new ColisionTypeDeserializer())
                        .create();
        
        LevelGridTileV2[][] grid = null;
        int w = 0;
        int h = 0;
        
        try {
            String full_path = ClassLoader.getSystemResource(json_path).getPath();
            FileReader reader = new FileReader(full_path);
          
            JsonObject map = JsonParser.parseReader(reader).getAsJsonObject();
            
            //get the tiles array
            JsonArray tiles = map.getAsJsonArray("tiles");
            
            //get the map dimensions
            w = map.getAsJsonPrimitive("width").getAsInt();
            h = map.getAsJsonPrimitive("height").getAsInt();
            
            grid = new LevelGridTileV2[w][h];
                
            int length = tiles.size();
            for(int i = 0; i < length; i++){
                JsonObject obj = tiles.get(i).getAsJsonObject();
                LevelGridTileV2 tile = gson.fromJson(obj, LevelGridTileV2.class);
                
                //here we need to check manually that the colision type was loaded correctly
                //just in case it wasn't defined in the json.
                //to handle sthis case there are security checks both in "LevelGridTileV2.COLISION_TYPE.fromLabel" and in "ColisionTypeDeserializer.deserialize"
                //but since these methods dont even run if a json propery/objet doesn't exists, we have to do the following here:
                //fallback to COLISION_TYPE.BLOCK
                if(tile.colision == null) tile.colision = LevelGridTileV2.COLISION_TYPE.WALK;
                
                Vector3 grid_pos   = tile.getGridPosition();
                Vector3 window_loc = grid_pos.getScaled(size);
                
                tile.setWindowLocation(window_loc);
                
                grid[(int)grid_pos.getX()][(int)grid_pos.getY()] = tile;
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        finally{
            return new GridAndDimensionsWrapper(grid, new Vector3(w,h,0));
        }
    }
    
    public LevelGridTileV2[][] getTileMatrixFromJSON(String json_path, int w, int h, int size){
        
        //we use GsonBuilder in order to register our deserializer fro LevelGridTileV2.COLISION_TYPE
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LevelGridTileV2.COLISION_TYPE.class, new ColisionTypeDeserializer())
                        .create();
        
        LevelGridTileV2[][] grid = new LevelGridTileV2[w][h];
        
        try {
            String full_path = ClassLoader.getSystemResource(json_path).getPath();
            FileReader reader = new FileReader(full_path);
          
            JsonObject map = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray tiles = map.getAsJsonArray("tiles");
                
            int length = tiles.size();
            for(int i = 0; i < length; i++){
                JsonObject obj = tiles.get(i).getAsJsonObject();
                LevelGridTileV2 tile = gson.fromJson(obj, LevelGridTileV2.class);
                
                //here we need to check manually that the colision type was loaded correctly
                //just in case it wasn't defined in the json.
                //to handle sthis case there are security checks both in "LevelGridTileV2.COLISION_TYPE.fromLabel" and in "ColisionTypeDeserializer.deserialize"
                //but since these methods dont even run if a json propery/objet doesn't exists, we have to do the following here:
                //fallback to COLISION_TYPE.BLOCK
                if(tile.colision == null) tile.colision = LevelGridTileV2.COLISION_TYPE.BLOCK;
                
                Vector3 grid_pos   = tile.getGridPosition();
                Vector3 window_loc = grid_pos.getScaled(size);
                
                tile.setWindowLocation(window_loc);
                
                grid[(int)grid_pos.getX()][(int)grid_pos.getY()] = tile;
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        finally{
            return grid;
        }
    }
}
