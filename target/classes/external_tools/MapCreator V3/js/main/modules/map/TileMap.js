import { TileInfo      } from "./TileInfo.js";
import { CanvasWrapper } from "./CanvasWrapper.js";
import { ToolManager   } from "../tools/ToolManager.js"; 
import { Vector2       } from '../../Vector2.js'; 
import { TileSelector } from '../TileSelector.js';

export class TileMap{
    

    map = new Map();
    
    
    constructor(dimensionsTiles, tileRenderSize){
        
        if(!(dimensionsTiles instanceof Vector2)) throw new TypeError("dimensionsTiles must be a Vector3")
        
        this.dimensionsTiles = dimensionsTiles;
        this.tileRenderSize  = tileRenderSize;
        this.initialzied = false;
    }

    initialize(){
        this.init();
        this.init_map();
        this.initialized = true;
    }

    init(){
        this.tile_selector = new TileSelector(this);
        this.tile_selector.init();
        
        this.canvas_wrapper = new CanvasWrapper("levelCanvas", this);
        this.tools = new ToolManager(this, this.canvas_wrapper);
    }

    init_map(){
    
        const ctx = this.canvas_wrapper.get_context();
        
        ctx.strokeStyle = "red";
        
        for(let x = 0; x < this.dimensionsTiles.x; x++){
            for(let y = 0; y < this.dimensionsTiles.y; y++){
                ctx.strokeRect(x * this.tileRenderSize, y * this.tileRenderSize,
                                   this.tileRenderSize,     this.tileRenderSize);
                
                const logical_pos = new Vector2(x, y);
                const  screen_pos  = logical_pos.scale(this.tileRenderSize);
                
                const tileInfo = new TileInfo(logical_pos, screen_pos, null, null);
                
                this.map.set(logical_pos.toString(), tileInfo);
            }    
        }
        
        console.log(this.map);
    }


    update_map(){
        const copy = new Map(this.map);
        this.map = new Map();
        
        this.canvas_wrapper = new CanvasWrapper("levelCanvas", this);
        
        for(let x = 0; x < this.dimensionsTiles.x; x++){
            for(let y = 0; y < this.dimensionsTiles.y; y++){
                
                const logical_pos = new Vector2(x, y);
                const key = logical_pos.toString();
                if(copy.has(key)){
                    const i = copy.get(key);
                    
                    if(i.parent_atlas_id){
                        this.map.set(key, i);
                        this.canvas_wrapper.fill_tile(i, "blue");    
                    }
                    
                }
                else {
                    const screen_pos  = logical_pos.scale(this.tileRenderSize);
                    const tileInfo = new TileInfo(logical_pos, screen_pos, null, null);
                    this.map.set(logical_pos.toString(), tileInfo);
                    this.canvas_wrapper.fill_tile(tileInfo, "blue");    
                }
            }    
        }
        
        
    }


    resize(newDimensions){
      
        if((this.dimensionsTiles.x > newDimensions.x) ||
           (this.dimensionsTiles.y > newDimensions.y)) console.log("error");
        else console.log("resize");
           
        let {x:new_cols, y:new_rows} = newDimensions;
        
        this.dimensionsTiles = newDimensions;
        this.update_map();
    }

    
    render_visual_map(){
        
    }

    update_logical_map(paint_info){
        let key = paint_info.logical_map_pos.toString();
        this.map.set(key, paint_info);
    }


    update_canvas_ui(){
        this.canvas_wrapper.init_canvas_ui();
        this.initialize();
    }

    set_tile_at(pos, info){
        
        let tileinfo = new TileInfo(pos, info.screen_xy, info.atlas_id, info.atlas_xy);
        
        
        //UPDATE LOGIC AND UI SIDE
        this.update_logical_map(tileinfo);
        this.canvas_wrapper.fill_tile(tileinfo, "blue");
    }


    get_tile_at(v){
        if(!(v instanceof Vector2))
            throw new TypeError("error getting tile: a vector position must be specified");
        
        const key = v.toString();
        
        if(this.map.has(key)) return this.map.get(key);
        else return null;
    }


    notify_tile_selection(selected_info){
        this.tools.notify_tile_selection(selected_info);
    }
}