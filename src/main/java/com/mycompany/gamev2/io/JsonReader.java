/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gamev2.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV2;
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTileV3;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.io.gson_deserializers.ColisionTypeDeserializer;
import com.mycompany.gamev2.io.gson_deserializers.Vector3TypeDeserializer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

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
                        .registerTypeAdapter(LevelGridTileV3.COLISION_TYPE.class, new ColisionTypeDeserializer())
                        .registerTypeAdapter(Vector3.class, new Vector3TypeDeserializer())
                        .create();

        //define the grid srtucture and it's dimensions before the try-catch block
        Vector3 map_dimensions = Vector3.ZERO;
        ArrayList<LevelGridTileV3>[][] grid = null;

        try {
            //file
            String full_path = ClassLoader.getSystemResource(json_path).getPath();
            FileReader reader = new FileReader(full_path);
          
            //get main json elements (map and layers)
            JsonObject map = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray layers = map.getAsJsonArray("layers");
            
            //initialize grid with it's dimensions as specified in the json
            map_dimensions = gson.fromJson(map.getAsJsonObject("size"), Vector3.class);
            int width = (int) map_dimensions.getX();
            int height = (int) map_dimensions.getY();
            
            grid = (ArrayList<LevelGridTileV3>[][]) new ArrayList[width][height];
            
            //iterate layers and deserialize individual tiles for each layer
            Iterator<JsonElement> iterator = layers.iterator();
            while (iterator.hasNext()) {
                
                //get the current layer and all its tiles
                JsonObject layer = iterator.next().getAsJsonObject();
                JsonArray tiles = layer.getAsJsonArray("tiles");
                    
                //iterate through this layer's tiles and deserialize them
                for(JsonElement json_tile : tiles){
                    //get the tile from json
                    LevelGridTileV3 tile = gson.fromJson(json_tile.getAsJsonObject(), LevelGridTileV3.class);
                    
                    //compute what this tile's window render location should be
                    Vector3 tile_pos = tile.tile_pos;
                    tile.wndw_loc = tile.tile_pos.getScaled(size);
                    
                    int grid_x = (int)tile_pos.getX();
                    int grid_y = (int)tile_pos.getY();
                                        
                    //FINALLY: add the tile to the tile_stack at this point in the grid
                    ArrayList<LevelGridTileV3> tile_stack = grid[grid_x][grid_y];
                    if(tile_stack == null){
                        tile_stack = new ArrayList<LevelGridTileV3>();
                        grid[grid_x][grid_y] = tile_stack;
                    }
                    
                    //add to the tile stack and sort it by layer (ascending)
                    tile_stack.add(tile);
                    tile_stack.sort(Comparator.comparingInt(LevelGridTileV3::getLayerIndex));
                }
            }
            
            /*int length = tiles.size();
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
            }*/
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        finally{
            return new GridAndDimensionsWrapper(grid, map_dimensions);
        }
    }
    
    public LevelGridTileV2[][] getTileMatrixFromJSON(String json_path, int w, int h, int size){
        
        //we use GsonBuilder in order to register our deserializers for LevelGridTileV2.COLISION_TYPE and Vector3
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LevelGridTileV2.COLISION_TYPE.class, new ColisionTypeDeserializer())
                        .registerTypeAdapter(Vector3.class, new Vector3TypeDeserializer())
                        .create();
        
        LevelGridTileV2[][] grid = new LevelGridTileV2[w][h];
        
        try {
            String full_path = ClassLoader.getSystemResource(json_path).getPath();
            FileReader reader = new FileReader(full_path);
          
            JsonObject map = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray layers = map.getAsJsonArray("layers");
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
