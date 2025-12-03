import { TileInfo      } from "./TileInfo.js";
import { CanvasWrapper } from "./CanvasWrapper.js";
import { Vector2       } from '../../Vector2.js'; 

export class TileMap{
    

    map = new Map();
    
    constructor(dimensionsTiles, tileRenderSize){
        
        if(!(dimensionsTiles instanceof Vector2)) throw new TypeError("dimensionsTiles must be a Vector3")
        this.dimensionsTiles = dimensionsTiles;
        
        this.tileRenderSize = tileRenderSize;
        
        
    }

    initialize(){
        this.init_canvas();
        this.init_map();
    }

    init_canvas(){
        
        this.canvas_wrapper = new CanvasWrapper("levelCanvas", this);
    }

    init_map(){
    
        const ctx = this.canvas_wrapper.get_context();
        
        ctx.strokeStyle = "red";
        
        for(let x = 0; x < this.dimensionsTiles.x; x++){
            for(let y = 0; y < this.dimensionsTiles.y; y++){
                ctx.strokeRect(x * this.tileRenderSize, y * this.tileRenderSize,
                                   this.tileRenderSize,     this.tileRenderSize);
                
                const log_map_pos = new Vector2(x, y);
                const screen_pos  = log_map_pos.scale(this.tileRenderSize);
                
                const tileInfo = new TileInfo(log_map_pos, screen_pos, null, null);
                
                this.map.set(log_map_pos.toString(), tileInfo);
            }    
        }
        
        console.log(this.map);
    }

    
    render_visual_map(){
        
    }

    update_logical_map(){
        
    }


    get_tile_at(v){
        if(!(v instanceof Vector2))
            throw new TypeError("error getting tile: a vector position must be specified");
        
        const key = v.toString();
        
        if(this.map.has(key)) return this.map.get(key);
        else return null;
    }
}