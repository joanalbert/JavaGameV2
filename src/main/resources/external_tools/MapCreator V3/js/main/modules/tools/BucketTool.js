import { BaseTool } from "./BaseTool.js";
import { TileInfo} from "../map/TileInfo.js";
import { Vector2 } from "../../vector2.js";


export class BucketTool extends BaseTool {
    
    constructor(name, html_element, owner){
        super(name, html_element, owner);
        
        this.mouse_tileinfo = null;
        this.map = null;
    }
    
    work(map){
        this.map = map;
        
        const clicked  = this.map.canvas_wrapper.clicked;
        const moving   = this.map.canvas_wrapper.is_mouse_moving;
        if(clicked && !moving) this.floodfill();
    }
    
    //BFS
    floodfill(){
        console.log("flood fill");
        
        const under_mouse = this.map.canvas_wrapper.tile_under_mouse;
        const seed = this.map.get_tile_at(under_mouse.logical_map_pos);
        
            
        
        let queue = [];
        const _seed = {
            "atlas_id": seed.parent_atlas_id,
            "atlas_xy": seed.parent_atlas_pos,
            "map_xy": seed.logical_map_pos,
            "screen_xy": seed.logical_map_pos.scale(this.map.tileRenderSize)
        }
        
        
        
        let counter = 0;
        const fill_tile = this.owner.tile_selection;
        
        queue.push(_seed);
        while(queue.length > 0){
            counter++;
            const current = queue.shift();
            
            
            //avoid repainting tiles
            if(current.atlas_id == fill_tile.atlas_id && 
               current.atlas_xy.equals(fill_tile.atlas_pos)) continue;
            
            //unpainted tile, we don't run any checks and paint directly
            if((!current.atlas_id && !current.atlas_pos) /*&& (_seed.atlas_id && _seed.atlas_xy)*/) {
                this.paint_and_expand(current, fill_tile, queue);
                continue;
            }            

                        
            //if currently checked tile is from another atlas, or same atlas but diff.position (boundary), continue
            if(current.atlas_id != _seed.atlas_id) continue;
            if(current.atlas_id == _seed.atlas_id && !current.atlas_xy.equals(_seed.atlas_xy)) continue;
                        
            //do the thing
            this.paint_and_expand(current, fill_tile, queue);
        }
        
        console.log(`floodfill iterations: ${counter}`);
    }
    
    paint_and_expand(current, fill_tile, queue){
        console.log(`painting: ${current.map_xy.toString()}
                         with: ${fill_tile.atlas_id}/${fill_tile.atlas_pos.toString()} `)
        
        this.map.set_tile_at(current.map_xy, {"map_xy":    current.map_xy,
                                              "screen_xy": current.screen_xy,
                                              "atlas_id":  fill_tile.atlas_id,
                                              "atlas_xy":  fill_tile.atlas_pos
                                             });
            
        this.expand_to_neighbors(queue, current.map_xy, current);
    }
    
    expand_to_neighbors(queue, map_xy, current){
        
        
        //north
        const north_pos  = map_xy.add(Vector2.up);
        const north_tile = this.map.get_tile_at(north_pos); 
        if(north_tile){
            const north = {
                "atlas_id": north_tile.parent_atlas_id,
                "atlas_xy": north_tile.parent_atlas_pos,
                "map_xy": north_pos,
                "screen_xy": map_xy.add(Vector2.up).scale(this.map.tileRenderSize)
            }
            
            queue.push(north);
        }
        
        //south
        const south_pos = map_xy.add(Vector2.up.scale(-1));
        const south_tile = this.map.get_tile_at(south_pos);
        if(south_tile){
            const south = {
                "atlas_id": south_tile.parent_atlas_id,
                "atlas_xy": south_tile.parent_atlas_pos,
                "map_xy": south_pos,
                "screen_xy": map_xy.add(Vector2.up.scale(-1)).scale(this.map.tileRenderSize)
            }
            
            queue.push(south);
        }
        
        //west
        const west_pos = map_xy.add(Vector2.right.scale(-1));
        const west_tile = this.map.get_tile_at(west_pos);
        if(west_tile){
            const west = {
                "atlas_id": west_tile.parent_atlas_id,
                "atlas_xy": west_tile.parent_atlas_pos,
                "map_xy": west_pos,
                "screen_xy": map_xy.add(Vector2.right.scale(-1)).scale(this.map.tileRenderSize)
            }
            
            queue.push(west);
        }
        
        
        //east
        const east_pos = map_xy.add(Vector2.right);
        const east_tile = this.map.get_tile_at(east_pos);
        if(east_tile){
            const east = {
                "atlas_id": east_tile.parent_atlas_id,
                "atlas_xy": east_tile.parent_atlas_pos,
                "map_xy": east_pos,
                "screen_xy": map_xy.add(Vector2.right).scale(this.map.tileRenderSize)
            }
            
            queue.push(east);
        }
        
        
        
        
        
        
    }
}


