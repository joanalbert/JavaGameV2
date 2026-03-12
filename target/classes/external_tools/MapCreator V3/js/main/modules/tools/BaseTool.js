import { TileMap } from "../map/TileMap.js";

export class BaseTool {
    
    
    constructor(name, html_element, owner){
        this.name = name;
        this.active = false;
        this.html_element = html_element;
        this.owner = owner;
    }
    
    enable(){
        this.active = true
        
        this.html_element.classList.remove("bg-danger");
        this.html_element.classList.add("bg-primary");
    };
    
    disable(){
        this.active = false;
        
        this.html_element.classList.remove("bg-primary");
        this.html_element.classList.add("bg-danger");
    }
    
    
    //the main method for the tool to do it's job (overridden in each tool class)
    // 
    // map <- actual map to talk to and modify.
    //        this reference grants access to both the CanvasWrapper for UI state
    //        and to the TileMap to update the TileMap directly
    work(map){
        
        if(map == undefined || map == null || !(map instanceof TileMap) )
            throw new TypeError("Wrapper must be a CanvasWrapper object");
    }
}