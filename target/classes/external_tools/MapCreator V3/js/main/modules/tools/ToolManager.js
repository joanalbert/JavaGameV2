import { TileMap       } from "../map/TileMap.js";
import { CanvasWrapper } from "../map/CanvasWrapper.js";
import { BaseTool } from "./BaseTool.js";
import { BrushTool } from "./BrushTool.js";
import { BucketTool} from "./BucketTool.js";
import { Vector2 } from "../../Vector2.js";


export class ToolManager{
    
    
    
    
    constructor(owner, wrapper){
     
        if(owner == undefined || owner == null || !(owner instanceof TileMap) )
            throw new TypeError("Owner must be a TileMap object");
        
        
        if(wrapper == undefined || wrapper == null || !(wrapper instanceof CanvasWrapper) )
            throw new TypeError("Wrapper must be a CanvasWrapper object");
        
        
        this.owner = owner;
        this.canvas_wrapper = wrapper;
        
        this.tile_selection = null;
        
        this.init_tools_ui();
    }
    
    
    
    init_tools_ui(){
        
        //get buttons
        this.pen_btn = document.getElementById("pen_btn");
        this.bucket_btn = document.getElementById("bucket_btn");
        this.drop_btn = document.getElementById("drop_btn");
        
        //set tools array
        this.tools = [];
        this.tools.push(new BrushTool("brush", pen_btn, this));
        this.tools.push(new BucketTool("bucket", bucket_btn, this));
        this.tools.push(new BaseTool("drop", drop_btn, this));
        
        
        
        this.tools.forEach(tool => {
           let element = tool.html_element;
           console.log(element); 
            
            
           element.addEventListener("click", (e)=>{
               this.toggle(tool);
           });
        });
        
        this.disable_all();
    }
    

    
    disable_all(){
        this.tools.forEach(tool=>tool.disable());
    }
    
    enable_tool(tool){
        tool.enable();
    }
    
    disable_tool(tool){
        tool.disable();
    }
    
    get_active_tool(){
        let active = this.tools.filter(t=>t.active)[0];
        return active;
    }
    
    toggle(tool){
        this.disable_all();
        if(tool.active) this.disable_tool(tool);
        else this.enable_tool(tool);
    }
    
    
    notify(){
       let tool = this.get_active_tool();
       if(tool) tool.work(this.owner);
    }
    
    notify_tile_selection(selected_info){
        this.tile_selection = selected_info;
    }
}




