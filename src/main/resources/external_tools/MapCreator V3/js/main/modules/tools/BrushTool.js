import { BaseTool } from "./BaseTool.js";
import { TileInfo} from "../map/TileInfo.js";

export class BrushTool extends BaseTool {
    
    
    constructor(name, html_element, owner){
        super(name, html_element, owner);
        
        this.mouse_tileinfo = null;
        this.mouse_prev_tileinfo = null;
        this.map = null;
    }
    
    enable(){
        super.enable();
    }
    
    disable(){
        super.disable();
        
        if(!this.map) return;
        
        
        //on tool disable, un-highlight current tile
        let to_save = this.map.get_tile_at(this.mouse_prev_tileinfo.logical_map_pos);
        if(!to_save) return;
        
    
        let pre_highlight_info = new TileInfo(
            this.mouse_tileinfo.logical_map_pos,
            this.mouse_tileinfo.screen_pos,
            to_save.parent_atlas_id,
            to_save.parent_atlas_pos
        );   
        
        this.map.canvas_wrapper.fill_tile(pre_highlight_info, "blue");
    }
    
    
    work(map){
        super.work(map); //super call to validate arguments
        
        this.map = map;
        
        //gather important flasg
        const clicked  = map.canvas_wrapper.clicked;
        const rclicked = map.canvas_wrapper.rclicked;
        const moving   = map.canvas_wrapper.is_mouse_moving;
        
        
        if(rclicked){
            this.test();
            return;
        }
        
        //control current and previous tileinfo under the mouse
        let current_tileinfo_mouse = map.canvas_wrapper.tile_under_mouse;
        let changed = this.did_mouse_pos_change(current_tileinfo_mouse);
        
        if(!current_tileinfo_mouse || (!changed && !clicked)) return; // the clicked flag here is necessary because we only
        else this.track_tileinfo_mouse(current_tileinfo_mouse);       // care about the changed check when we're highlighting
        
        
        
        //depending on whether the user is clicking or not
        //we paint or highlight
        if(!clicked) this.highlight_ui();
        else this.paint_ui();
    }
    
    
    test(){
        
        const rclick_tileinfo = this.map.get_tile_at(this.mouse_tileinfo.logical_map_pos);
        
        if(!rclick_tileinfo.parent_atlas_id) return; //not painted
        
        this.map.tile_selector.update_selection({
            "atlas_id": rclick_tileinfo.parent_atlas_id,
            "atlas_pos": rclick_tileinfo.parent_atlas_pos
        });
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
                t_selection.atlas_id,
                t_selection.atlas_pos
            );
            
            this.map.canvas_wrapper.fill_tile(highlight_info, "blue");
        }

        
        
    }
    
    paint_ui(){
        //if(this.mouse_tileinfo) this.map.canvas_wrapper.fill_tile(this.mouse_tileinfo, "blue");
        
        console.log("painting");
        
        if(this.mouse_tileinfo){
            
            let t_selection = this.owner.tile_selection;
            if(!t_selection) return;
            
            let paint_info = new TileInfo(
                this.mouse_tileinfo.logical_map_pos,
                this.mouse_tileinfo.screen_pos,
                t_selection.atlas_id,
                t_selection.atlas_pos
            );
            
            this.map.canvas_wrapper.fill_tile(paint_info, "blue");
            this.map.update_logical_map(paint_info);
        }
    }
    
    
    
    
    track_tileinfo_mouse(current){
        if(this.mouse_tileinfo == null) this.mouse_tileinfo = current;
        else{
            if(!current.info_compare_screen(this.mouse_tileinfo)){
                this.mouse_prev_tileinfo = this.mouse_tileinfo;
                this.mouse_tileinfo = current;
            }
        }
    }
    
    did_mouse_pos_change(current_tileinfo_mouse){
        if(!this.mouse_tileinfo) return true;
            
        let m_pos = current_tileinfo_mouse.logical_map_pos;
        let   pos = this.mouse_tileinfo.logical_map_pos;
        return !m_pos.equals(pos);
    }
}











