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
import com.mycompany.gamev2.component.level_components.grid_component.LevelGridTile;
import com.mycompany.gamev2.gamemath.Vector3;
import com.mycompany.gamev2.io.gson_deserializers.ColisionTypeDeserializer;
import com.mycompany.gamev2.io.gson_deserializers.Vector3TypeDeserializer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

/**
 *
 * @author J.A
 */
public class JsonReader {
    
    private static final class Holder{
        static final JsonReader INSTANCE = new JsonReader();
    }
    
    public static JsonReader getInstance(){
       return Holder.INSTANCE;
    }
        
    private JsonReader(){
    
    }
    
    
    
    public GridAndDimensionsWrapper getTileMatrixFromJSON(String json_path, int size){
        //we use GsonBuilder in order to register our deserializer fro LevelGridTileV2.COLISION_TYPE
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LevelGridTile.COLISION_TYPE.class, new ColisionTypeDeserializer())
                        .registerTypeAdapter(Vector3.class, new Vector3TypeDeserializer())
                        .create();

        //define the grid srtucture and it's dimensions before the try-catch block
        Vector3 map_dimensions = Vector3.ZERO;
        LevelGridTile[][][] grid = null;
        int layer_count = 0;

        try {
            //file
            String full_path = ClassLoader.getSystemResource(json_path).getPath();
            FileReader reader = new FileReader(full_path);
          
            //get main json elements (map and layers)
            JsonObject map = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray layers = map.getAsJsonArray("layers");
            
            //read the total amount of layers
            layer_count = layers.size();
            
            //initialize grid with it's dimensions as specified in the json
            map_dimensions = gson.fromJson(map.getAsJsonObject("size"), Vector3.class);
            int width = (int) map_dimensions.getX();
            int height = (int) map_dimensions.getY();
            
            grid = new LevelGridTile[width][height][layer_count];
            
            //iterate layers and deserialize individual tiles for each layer
            Iterator<JsonElement> iterator = layers.iterator();
            while (iterator.hasNext()) {
                
                //get the current layer and all its tiles
                JsonObject layer = iterator.next().getAsJsonObject();
                JsonArray tiles = layer.getAsJsonArray("tiles");
                    
                //iterate through this layer's tiles and deserialize them
                for(JsonElement json_tile : tiles){
                    //get the tile from json
                    LevelGridTile tile = gson.fromJson(json_tile.getAsJsonObject(), LevelGridTile.class);
                    
                    //compute what this tile's window render location should be
                    Vector3 tile_pos = tile.getGridPosition();
                    tile.setWindowLocation(tile.getGridPosition().getScaled(size));
                    
                    int grid_x = (int)tile_pos.getX();
                    int grid_y = (int)tile_pos.getY();
                                        
                    //FINALLY: add the tile to the grid
                    grid[grid_x][grid_y][tile.getLayerIndex()] = tile;
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
            return new GridAndDimensionsWrapper(grid, map_dimensions, layer_count);
        }
    }
    
    public GridAndDimensionsWrapper getTileMatrixFromJSON(String json_path, int w, int h, int size){
        
        //we use GsonBuilder in order to register our deserializers for LevelGridTileV2.COLISION_TYPE and Vector3
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LevelGridTile.COLISION_TYPE.class, new ColisionTypeDeserializer())
                        .registerTypeAdapter(Vector3.class, new Vector3TypeDeserializer())
                        .create();
        
        //define the grid srtucture and it's dimensions before the try-catch block
        LevelGridTile[][][] grid = null;
        int layer_count = 0;

        try {
            //file
            String full_path = ClassLoader.getSystemResource(json_path).getPath();
            FileReader reader = new FileReader(full_path);
          
            //get main json elements (map and layers)
            JsonObject map = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray layers = map.getAsJsonArray("layers");
            
            //read the total amount of layers
            layer_count = layers.size();
            
            
            //instead of using the dimensions in the JSON we instead set the user defined dimensions
            grid = new LevelGridTile[w][h][layer_count];
            
            //iterate layers and deserialize individual tiles for each layer
            Iterator<JsonElement> iterator = layers.iterator();
            while (iterator.hasNext()) {
                
                //get the current layer and all its tiles
                JsonObject layer = iterator.next().getAsJsonObject();
                JsonArray tiles = layer.getAsJsonArray("tiles");
                    
                //iterate through this layer's tiles and deserialize them
                for(JsonElement json_tile : tiles){
                    //get the tile from json
                    LevelGridTile tile = gson.fromJson(json_tile.getAsJsonObject(), LevelGridTile.class);
                    
                    //compute what this tile's window render location should be
                    Vector3 tile_pos = tile.getGridPosition();
                    tile.setWindowLocation(tile_pos.getScaled(size));
                    
                    int grid_x = (int)tile_pos.getX();
                    int grid_y = (int)tile_pos.getY();
                    
                    if(grid_x < 0 || grid_x >= w) continue;
                    if(grid_y < 0 || grid_y >= h) continue;
                                        
                    //FINALLY: add the tile to the grid
                    grid[grid_x][grid_y][tile.getLayerIndex()] = tile;
                }
            }
  
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }
        finally{
            return new GridAndDimensionsWrapper(grid, new Vector3(w,h,layer_count), layer_count);
        }
    }
}
