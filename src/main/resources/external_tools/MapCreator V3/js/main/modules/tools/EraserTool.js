import { BrushTool } from "./BrushTool.js";
import { TileInfo} from "../map/TileInfo.js";


export class EraserTool extends BrushTool{
    
    constructor(name, html_element, owner){
        super(name, html_element, owner);
        
    }
    
    highlight_ui(){
        console.log("highlight");
               
        //when highlighting, first we must 'restore' the previous highlighted cell
        //since highlighting doesnt modify the real map, we can ask it for atlas data
        //and combine it with our known mouse previous tile info
        //and send it for repainting
        if(this.mouse_prev_tileinfo){
            console.log("highlight - restore");
            
            let to_save = this.map.get_tile_at(this.mouse_prev_tileinfo.logical_map_pos);
            if(!to_save) return;
            
            let pre_highlight_info = new TileInfo(
                this.mouse_prev_tileinfo.logical_map_pos,
                this.mouse_prev_tileinfo.screen_pos,
                to_save.parent_atlas_id,
                to_save.parent_atlas_pos
            );
            
            this.map.canvas_wrapper.fill_tile(pre_highlight_info, "black");
        }
        
        
        //when we actually highlight we get atlas data from the selected tile in the menu
        //we combine with out current mouse data, and send it to paint without updating the map (highlighting)
        if(this.mouse_tileinfo){
            console.log("highlight - perform");
            
            let t_selection = this.owner.tile_selection;
            if(!t_selection) return;
            
            let highlight_info = new TileInfo(
                this.mouse_tileinfo.logical_map_pos,
                this.mouse_tileinfo.screen_pos,
                null,
                null
            );
            
            this.map.canvas_wrapper.fill_tile(highlight_info, "black");
        }
    }
    
    paint_ui(){
        
        let paint_info = new TileInfo(
            this.mouse_tileinfo.logical_map_pos,
            this.mouse_tileinfo.screen_pos,
            null,
            null
        );

        console.log("erasing");
        this.map.canvas_wrapper.fill_tile(paint_info, "black");
        this.map.update_logical_map(paint_info);
    }
}
