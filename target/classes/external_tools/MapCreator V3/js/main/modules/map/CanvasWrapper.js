import { TileMap } from "./TileMap.js";
import { TileInfo      } from "./TileInfo.js";
import { Vector2 } from '../../Vector2.js'; 


export class CanvasWrapper{
    
    
    constructor(canvas_id, owner){

        if(!(owner instanceof TileMap)) throw new TypeError("Owner must be a TileMap")
        this.owner = owner;
        
        //find prerequisite dom element
        this.canvas = document.getElementById(canvas_id);
        this.ctx = this.canvas.getContext("2d");
        
        //control variables
        this.tile_under_mouse = null;
        this.tile_clicked = null;
        
        //input flags
        this.clicked  = false;
        this.rclicked = false;
        this.dragging = false;
        this.is_mouse_moving = false;
        this.move_timeout = null;
        
        
        
        //initialization calls
        this.init_canvas_ui();
        this.init_canvas_logic();  
    }
    
   
   
    
    /* -------------------------------- INITIALIZATION -------------------------- */
    init_canvas_ui(){
        
        const w = this.owner.dimensionsTiles.x * this.owner.tileRenderSize;
        const h = this.owner.dimensionsTiles.y * this.owner.tileRenderSize;
        
        this.canvas.setAttribute("width" , w);
        this.canvas.setAttribute("height", h);
        
        this.canvas.style.width  = w+"px";
        this.canvas.style.height = h+"px";
        
    }
    
    init_canvas_logic(){
        
        this.canvas.addEventListener("mousemove", (e)=>{
            
            //detect the END of mousemove
            this.compute_mousestop();
            
            //actual hover logic
            this.canvas_hover(e)
        });
        
        
        
        //control clicked flags
        this.canvas.addEventListener("mouseup", (e)=>{
            if(e.button === 2) this.rclicked = false;
            else this.clicked = false;
            
            this.notify_toolmanager();
        });
        
        this.canvas.addEventListener("mousedown", (e)=> {
            if(e.button === 2) this.rclicked = true;
            else this.clicked = true;
            
            
            this.notify_toolmanager();
        });
        
        
        this.canvas.addEventListener("click", (e)=>this.canvas_click(e));
        
        
        //we want to be able to catch right clicks on the canvas, so we stop the contextmenu
        this.canvas.addEventListener('contextmenu', (e) => {
            event.preventDefault();  
            this.canvas_right_click();
        });
    }
    /* --------------------------------------------------------------------------- */
    
    
    
    /* ----------------------------------- STATE CONTROL ------------------------- */
    canvas_click(e){
        //console.log("click");
        const tile_info = this.get_tile_info_from_event(e);
        const coords = tile_info == null ? null : tile_info.logical_map_pos;

        this.tile_clicked = tile_info;
        this.notify_toolmanager();      
    }
    
    canvas_right_click(e){
        //console.log('Right click');
        this.notify_toolmanager(); 
    }
 
    
    canvas_hover(e){
        
        //maintain 'dragging' flag based on 'clicked' flag
        if(this.clicked && this.is_mouse_moving) this.dragging = true;
        else this.dragging = false;
        
        
        const tile_info = this.get_tile_info_from_event(e);
        const coords = tile_info == null ? null : tile_info.logical_map_pos;
        
        this.tile_under_mouse = tile_info;
        this.notify_toolmanager();
    }
    
    

    compute_mousestop(){
        this.is_mouse_moving = true;
        
        //for each event fire we clear the move timeout
        clearTimeout(this.move_timeout);

        // For each event fire we also set the timeout, which means when the event stops firing.
        // The last timeout clear will run, turning the flag off.
        // And since the event isn't firing anymore we don't set the next timeout.
        // Hence detecting a 'mousestop' of sorts
        this.move_timeout = setTimeout(() => {
            //console.log("Mouse stopped moving");
            this.is_mouse_moving = false;
            this.notify_toolmanager();
        }, 5);
    }
    /* ------------------------------------------------------------------------ */
    
    
    /* ------------------------------------ UTILITY --------------------------- */
    notify_toolmanager(){
        this.owner.tools.notify();
    }
    
    get_context(){return this.ctx;}
    
    get_tile_info_from_event(e){
        
        //get true hover coords over the canvas based on bounding box
        const rect = e.target.getBoundingClientRect();
        const hover_pos = new Vector2(
            e.clientX - rect.left,
            e.clientY - rect.top,
        );

        //transform hover coords in pixels to logical map-indexed coords
        const logic_coords = hover_pos.scale(1/this.owner.tileRenderSize).round_components();


        //try get tile at that position
        const tile_info = this.owner.get_tile_at(logic_coords);
        return tile_info;
   }
   /* ------------------------------------------------------------------------  */
    
    
    
    
   /* --------------------------- PAINT OPERATIONS ---------------------------  */
   clear_tile(tileinfo){
       console.log("clear");
       let {x:dx, y:dy} = tileinfo.screen_pos;
       let dWidth  = this.owner.tileRenderSize;
       let dHeight = this.owner.tileRenderSize;

       ctx.clearRect(dx, dy, dWidth, dHeight);
   }    
    
   fill_tile(tileinfo, color, highlight = false){
       
       let ctx = this.get_context();
       
       if(!tileinfo.parent_atlas_id || !tileinfo.parent_atlas_pos){
           let {x, y} = tileinfo.screen_pos;
           ctx.fillStyle = color;
           ctx.fillRect(x, y, this.owner.tileRenderSize, this.owner.tileRenderSize);
           return;
       }
       
              
       let atlas_image = document.getElementById(tileinfo.parent_atlas_id);

       if(atlas_image){
            let {x:sx, y:sy} = tileinfo.parent_atlas_pos;
            let sWidth  = 32;
            let sHeight = 32;
           
            let {x:dx, y:dy} = tileinfo.screen_pos;
            let dWidth  = this.owner.tileRenderSize;
            let dHeight = this.owner.tileRenderSize;
           
            ctx.clearRect(dx, dy, dWidth, dHeight);
            ctx.drawImage(atlas_image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);        
       }
       
       if(highlight){
            let {x:dx, y:dy} = tileinfo.screen_pos;
            let dWidth  = this.owner.tileRenderSize;
            let dHeight = this.owner.tileRenderSize;
            
            ctx.lineWidth = 1;
            ctx.strokeStyle = color;
            ctx.strokeRect(dx, dy, dWidth, dHeight);
           
           
            this.clear_timeout = setTimeout(()=>{
                ctx.clearRect(dx, dy, dWidth, dHeight);
                
            },10);
       }
   }    
}











