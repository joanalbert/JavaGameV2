import { Vector2 } from '../../Vector2.js'; 


export class TileInfo{
    
    logical_map_pos;
    screen_pos;
    parent_atlas_pos;
    parent_atlas_id;
    
    
    constructor(logical_map_pos, screen_pos, parent_atlas_pos, parent_atlas_id){
        
        if(!(logical_map_pos instanceof Vector2))
            throw new TypeError("map position must be a Vector2");
        
        if(!(screen_pos instanceof Vector2))
            throw new TypeError("screen position must be a Vector2");
        
        if(parent_atlas_pos != null && !(parent_atlas_pos instanceof Vector2))
            throw new TypeError("if not null; parent atlas position must be a Vector2 ");
        
        
        if( (parent_atlas_pos instanceof Vector2) && (parent_atlas_id == null || !Number.isInteger(parent_atlas_id)) )
           throw new TypeError("if parent atlas position is set, then the atlas id must also be set");
        
        
        // Assign validated values to instance fields
        this.logical_map_pos = logical_map_pos;
        this.screen_pos = screen_pos;
        this.parent_atlas_pos = parent_atlas_pos;
        this.parent_atlas_id = parent_atlas_id;
    }
}