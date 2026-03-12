import { TileMap } from '../map/TileMap.js';
import { Vector2 } from '../../vector2.js';

export class FormInput {

    static #instance = null;

    constructor(map) {
        if (FormInput.#instance) {
            return FormInput.#instance;
        }
        
        if(map == null || !(map instanceof TileMap)) throw new TypeError("A TileMap object is needed")
        
        this.tile_map = map;
        console.log(this.tile_map);
        
        this.dimensions_init();
        this.rsize_init();

        FormInput.#instance = this;
    }

    static getInstance(map) {
        if(map == null || !(map instanceof TileMap)) throw new TypeError("A TileMap object is needed")
        return FormInput.#instance ?? new FormInput(map);
    }


    ///set 'render size' slider events
    rsize_init(){
        const rsize_slider = document.getElementById("dsize");
        const rsize_px_indicator = document.getElementById("rsize_px_indicator");
        
        //update slider text, update tile render size and restart canvas
        rsize_slider.addEventListener("input", (e)=>{
            rsize_px_indicator.innerText = `${e.target.value}px`;
            this.tile_map.tileRenderSize = Number.parseInt(e.target.value);
            this.tile_map.update_canvas_ui();
        });
    }
    

    //
    dimensions_init() {
        const dimensions_btn = document.getElementById("dimensions_btn");
        dimensions_btn.addEventListener("click", (e)=>this.dimensions_click(e))
    }


    dimensions_click(e){
        const w = Number.parseInt(document.getElementById("width").value);
        const h = Number.parseInt(document.getElementById("height").value);
        
        //console.log(`width: ${w}, height: ${h}`);
        const new_dimensions = new Vector2(w,h);
        
        this.tile_map.resize(new_dimensions);
    }        
}