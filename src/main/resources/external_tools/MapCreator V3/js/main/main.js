import { TileMap } from './modules/map/TileMap.js';
import { Vector2 } from './Vector2.js'; 
import { FormInput } from './modules/input/FormInput.js';




let mapDimensions = new Vector2(5, 5);
let tileRenderSize = 128;


let map = new TileMap(mapDimensions, tileRenderSize);
map.initialize();

const i = FormInput.getInstance(map);


/*function update() {
    
    const clicked  = map.canvas_wrapper.clicked;
    const dragging = map.canvas_wrapper.dragging;
    const moving   = map.canvas_wrapper.is_mouse_moving;
    
    console.log(`clicked ${clicked}, moving: ${moving}, dragging: ${dragging}`);
       
    requestAnimationFrame(update);
}

update();*/