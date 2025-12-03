export class TileSelector{
    
    static tile_dimensions = 32;
    static tile_scale = 2;
    static tile_threshold = TileSelector.tile_dimensions * TileSelector.tile_scale;
    
    constructor(){
        
        //cache dom elements (indicators)
        this.coords_indicator = document.getElementById("preview_coords");
        this.atlas_indicator  = document.getElementById("preview_atlas");
        this.tile_previewer = document.getElementById("preview_tile");
        
        //marker div and all atlasses
        this.tile_selector  = document.getElementsByClassName("tile_selector")[0];
        this.atlases = Array.from(document.getElementsByClassName("asset-img"));
    }
    
    
    init(){
        this.atlases.forEach(atlas=>{
            
            this.set_atlas_id(atlas);
            
            atlas.addEventListener("mousemove", (e)=>this.atlas_mousemove(e));
            atlas.addEventListener("click", (e)=>this.atlas_click(e));
        });
    }

    set_atlas_id(atlas){
        const src_chunks = atlas.getAttribute("src").split("/");
        const last_chunk = src_chunks[src_chunks.length-1];
        const id = last_chunk.split(".")[0];
        atlas.setAttribute("id", id);
    }

    atlas_click(event){
        
        //prepare needed refs
        const ctx = this.tile_previewer.getContext("2d");
        const target_image = event.target;
        
        
        //update ui indicators
        this.coords_indicator.innerHTML = `coords: [x = ${this.clicked_x}, y = ${this.clicked_y}]`;
        this.atlas_indicator.innerHTML = `atlas: ${target_image.getAttribute("id")}`
                
        
        //source coordinates
        const sx = this.clicked_x * TileSelector.tile_dimensions;
        const sy = this.clicked_y * TileSelector.tile_dimensions;
        const sWidth  = TileSelector.tile_dimensions;
        const sHeight = TileSelector.tile_dimensions;
        
        
        //setination coordinates
        const dx = 0;
        const dy = 0;
        const dWidth  = TileSelector.tile_threshold;
        const dHeight = TileSelector.tile_threshold;
        
        //update preview canvas
        ctx.clearRect(0, 0, dWidth, dHeight);
        ctx.drawImage(target_image, sx, sy, sWidth, sHeight,
                                    dx, dy, dWidth, dHeight);
        
        /*console.log(this.clicked_x);
        console.log(`sx: ${sx}, sy: ${sy}, sWidth: ${sWidth}, sHeight: ${sHeight}`);
        console.log(`dx: ${dx}, dy: ${dy}, dWidth: ${dWidth}, dHeight: ${dHeight}`);*/
    }


    atlas_mousemove(event) {
        const rect = event.target.getBoundingClientRect();

        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;

        

        const tile_remainder_x = x % TileSelector.tile_threshold;
        const tile_remainder_y = y % TileSelector.tile_threshold;


        const snappedX = event.clientX - tile_remainder_x;
        const snappedY = event.clientY - tile_remainder_y;
        
        
        this.clicked_x = Math.floor(x/TileSelector.tile_threshold);
        this.clicked_y = Math.floor(y/TileSelector.tile_threshold);
                       
        

        this.tile_selector.style.left = snappedX + "px";
        this.tile_selector.style.top  = snappedY + "px";
        this.tile_selector.style.display = "block"; // make sure it's visible
    }
}









