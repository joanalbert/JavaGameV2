let global_isClicked = false;
window.global_isClicked = global_isClicked;
//we want to know whenever mouse click is held down
document.addEventListener("mousedown", (e)=> {
    global_isClicked = true;
    window.global_isClicked = global_isClicked;
});

document.addEventListener("mouseup", (e)=> {
    global_isClicked = false;
    window.global_isClicked = global_isClicked;
});



//depending on what tab is active, hide or show the tile selection menu
let editing_colisions = false;
let collisions_tab = document.getElementById("collisions_tab");
collisions_tab.addEventListener("click", () => {
    createCollisionsTable();
    editing_colisions = true;
    
    sideMenu.classList.remove("d-flex");
    sideMenu.classList.add("d-none");
});

let visual_tab = document.getElementById("visual_tab");
visual_tab.addEventListener("click", ()=> {
    editing_colisions = false;
    
    sideMenu.classList.remove("d-none");
    sideMenu.classList.add("d-flex");
});



/*loop();
function loop(){
    console.log(window.flags.brush);
    window.requestAnimationFrame(loop);
}*/


/////////////////////////////////////////////////////////////////////////// MAP TABLE SET AND RESET ///////////////////////////////////////////////////////
var mapTableElement = document.getElementById("mapTable"); 
var collisionsTable = document.getElementById("collisionsTable");
var widthInput = document.getElementById("width");
var heightInput = document.getElementById("height");
var changeTableBtn = document.getElementById("changeTable");

var TileWidth = 32;
var TileHeight = 32;

var TableTileWidth = widthInput.value;
var TableTileHeight = heightInput.value;

var TablePixelWidth = TableTileWidth * TileWidth;
var TablePixelHeight = TableTileHeight * TileHeight;


createTable();
changeTableBtn.addEventListener("click",changeTableFunction);



function createTable(){
    
    mapTableElement.classList.add("slidingTable");
    
    mapTableElement.style.width = `${TablePixelWidth}px`;
    mapTableElement.style.height = `${TablePixelHeight}px`;
    
    mapTableElement.style.border = "1px dashed red";
    mapTableElement.classList.add("bg-white");
    
    mapTableElement.style.display = "block";
    mapTableElement.style.marginBottom = "32px";
    mapTableElement.style.marginTop = "32px";
    mapTableElement.style.marginLeft = "auto";
    mapTableElement.style.marginRight = "auto";
    
    let tr,td;
    
    for(let y = 0; y < TableTileHeight; y++){
        
        tr = createRow();
        
        for(let x = 0; x < TableTileWidth; x++){
            td = createColumn();
            
            td.setAttribute("x",x);
            td.setAttribute("y",y);
            
            
            //RIGHT CLICK HANDLING (copy tile on rihgt click)
            td.addEventListener("contextmenu", (e)=>{
                e.preventDefault();
                let cell = e.currentTarget;
                let y = cell.getAttribute("y");
                let x = cell.getAttribute("x");
                
                let data = cell.cell_data;
                if(!data) return;
                
                console.log("ttt");
                
                select_tile_rightclick(data);
            });
            
            ////ON CLICK, ASSIGN IMAGE
            td.addEventListener("click", (e)=>{
                
                //we get the drawing context for the clicked grid cell
                let td_cnv = e.target.getElementsByTagName("canvas")[0];
                let td_ctx = td_cnv.getContext("2d");
                
                
               
                //first we close side menu for ease of use
                if(!hidden && !locked)togglerIcon.click();
                
                
                if(window.flags.fill){
                    //set 
                    let origin_position = {
                        "x": x,
                        "y": y
                    }
                    flood_fill_visual(origin_position, selected_cell_data);
                }
                else{
                    //we draw from temp canvas to the cell canvas
                    td_ctx.drawImage(temp_canvas, 0, 0);
                    e.target.setAttribute("set", true);
                    
                    //when a tile is painted we store the tile's pixel coords from the sheet
                    e.target.cell_data = selected_cell_data;
                }
                
                
            });
            
            
            //ON MOUSE ENTER/EXIT SHOW IMAGE BUT DONT ASSIGN IT 
            td.addEventListener("mouseenter", (e)=>{
                                
                //create a 'preview' canvas
                let td_cnv_preview = document.createElement("canvas");
                td_cnv_preview.style.width  = `${TileWidth}px`;
                td_cnv_preview.style.height = `${TileHeight}px`;
                td_cnv_preview.style.pointerEvents = "none";
                
                td_cnv_preview.setAttribute("width", TileWidth);
                td_cnv_preview.setAttribute("height", TileHeight);
                td_cnv_preview.id = "td_cnv_preview";
                
                //draw to the preview
                let preview_ctx = td_cnv_preview.getContext("2d");
                preview_ctx.drawImage(temp_canvas, 0, 0);
                
                //if there was already a preview canvas in this cell, remove it
                let previous_preview = e.target.querySelector("#td_cnv_preview");
                if(previous_preview) e.target.removeChild(previous_preview);
                
                //append the current preview
                e.target.appendChild(td_cnv_preview);
                
                //finally hide the real canvas for this cell
                let td_cnv = e.target.getElementsByTagName("canvas")[0];
                td_cnv.style.display = "none";
            });
            
            td.addEventListener("mouseleave", (e)=>{
                
                //get the real canvas for this cell and unhide it
                let td_cnv = e.target.getElementsByTagName("canvas")[0];
                td_cnv.style.display = "inline";
                
                //remove whatever preview canvas there was, if any
                let preview = e.target.querySelector("#td_cnv_preview");
                if(preview) e.target.removeChild(preview);
                
                //if the brush tool is active and mouse is pressed, set this cell on mouseleave by clicking it
                if(window.flags.brush && global_isClicked) e.target.click();
            });
            //////////////////////////////////////////
            
            tr.appendChild(td);
        }
        
        mapTableElement.appendChild(tr);
    }
    
    
}

//copy the map table, and from that, create the collisions table (and assign its rows and columns the propper class name)
function createCollisionsTable(){
    
    let container       = document.getElementById("collisionsTableParent");
    let collisionsTable = document.createElement("table");
    
    collisionsTable.setAttribute("class", "bg-white");
    collisionsTable.setAttribute("style", "border: 1px dashed red; margin: 32px auto;");
    
    
    let width  = widthInput.valueAsNumber;
    let height = heightInput.valueAsNumber;
    
    if(container.children.length > 0) return; //container.removeChild(container.children[0]);
    
    for(let y = 0; y < height; y++){
        
        let row = document.createElement("tr");
        
        row.classList.add("d-flex");
        row.classList.add("center");
        
        for(let x = 0; x < width; x++){
            
            let cell = document.createElement("td");
            let c    = document.createElement("canvas");
            
            cell.setAttribute("style", "width: 32px; height: 32px; margin: 0px; padding: 0px; position: relative;");
            
            cell.setAttribute("x",x);
            cell.setAttribute("y",y);
            
            cell.classList.add("col_table_cell");
            
            c.setAttribute("width", 32);
            c.setAttribute("height", 32);
            
            c.style.height = "32px";
            c.style.width = "32px";
            c.style.pointerEvents = "none";
            
            collisions_table_add_cell_events(cell);
            
            let ctx  = c.getContext("2d");
            
            let other_td = document.querySelector(`td.mapTableColumn[x="${x}"][y="${y}"]`);
            let other_canvas = other_td.children[0];
            
            if(other_canvas == null) continue;
            
            ctx.drawImage(other_canvas,0,0);
            
            if(x == 0 && y == 0){
                let prev_img = collisions_table_create_preview_img();
                cell.appendChild(prev_img);
            }
            
            cell.appendChild(c);
            row.appendChild(cell);
        }
        
        collisionsTable.appendChild(row);
    }
    
    container.appendChild(collisionsTable);
    
    cell_events();
}

function collisions_table_add_cell_events(cell){
    
    //event to update the position fo the preview image based on what cell the mouse is over
    cell.addEventListener("mouseenter", (e)=>{
        let prev_img = document.getElementsByClassName("coll_prev_img")[0];
        let x = e.target.getAttribute("x");
        let y = e.target.getAttribute("y");
        
        let y_pos = y * 32;
        let x_pos = x * 32;
                
        prev_img.style.top  = `${y_pos}px`
        prev_img.style.left = `${x_pos}px`
    });
}

function collisions_table_create_preview_img(){
    let img = document.createElement("img");
    img.classList.add("coll_prev_img");
    
    
    return img;
}


function changeTableFunction(){
    
    //first we empty the current table
    emptyMapTable();
    
    //update table dimensions variables
    TableTileWidth = widthInput.value;
    TableTileHeight = heightInput.value;
    
    TablePixelWidth = TableTileWidth * TileWidth;
    TablePixelHeight = TableTileHeight * TileHeight;
    
    //create the table again
    createTable();
}
          
function emptyMapTable(){
    let i = mapTableElement.children.length-1;
    while(mapTableElement.children.length > 0){
        mapTableElement.removeChild(mapTableElement.children[i]);
        i--;
    }    
}

function createRow(){
    let tr = document.createElement("tr");
    
    tr.style.width   = `${TablePixelWidth}px`;
    tr.style.height  = `${TileHeight}px`;
    tr.style.display = "flex";
    
    tr.style.margin  = "none";
    tr.style.padding = "none";
    tr.style.border = "none";
    
    tr.classList.add("mapTableRow");
    mapTableElement.classList.add("bg-white");
    
    return tr;
}

function createColumn(){
    let td = document.createElement("td");
    
    td.style.width  = `${TileWidth}px`;
    td.style.height = `${TileHeight}px`;

    td.style.margin  = "0px";
    td.style.padding = "0px";
    td.style.border = "none";
    
    //we add a canvas to each individual tile
    let td_cnv = document.createElement("canvas");
    td_cnv.style.border = "none";
    td_cnv.style.width  = `${TileWidth}px`;
    td_cnv.style.height = `${TileHeight}px`;
    td_cnv.style.pointerEvents = "none";
    td_cnv.setAttribute("width", TileWidth);
    td_cnv.setAttribute("height", TileHeight);
    
    td.appendChild(td_cnv);
    
    td.classList.add("mapTableColumn");
    mapTableElement.classList.add("bg-white");
        
    return td;
}
                        
function test(){
    console.log("a");    
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////////// SIDE MENU SCRIPTS ///////////////////////////////////////////////////////////////////////

//sidemenu dom
let sideToggler = document.getElementById("toggler");
let togglerIcon = document.querySelector("#toggler button#toggler_btn");
let tiles_tray  = document.getElementById("tile_tray");

//lock btn dom
let lockToggler = document.getElementById("toggler"); 
let lockIcon    = document.querySelector("#toggler button#lock_btn"); 

//sidemenu vars
let sideMenu = document.getElementById("sideMenu");
let movementLength = 1200;
let hidden = true;

//lock vars
let locked = false;

//sidemenu icons 
let leftClassName  = "fas fa-arrow-alt-circle-left";
let rightClassName = "fas fa-arrow-alt-circle-right";

//lock icons
let lockedClassName = "fas fa-lock";
let unlockedClassName = "fas fa-lock-open";


togglerIcon.addEventListener("click", (event)=>{
    if(hidden) {
        mapTableElement.style.marginLeft = "0px";
        showMenu(event.target);
    }
    else {
        mapTableElement.style.marginLeft = "auto";
        hideMenu(event.target);
    }
})

lockIcon.addEventListener("click", (event)=>{
    if(locked) unlockMenu(event.target);
    else lockMenu(event.target);
});


//sidemenu actions
function hideMenu(element){
    sideMenu.style.left = "100%";
    hidden = true;
    
    let i = element.children[0];
    if(i) i.className = leftClassName;
    else element.className = leftClassName;
}

function showMenu(element){
    sideMenu.style.left = "45%";
    hidden = false;
    
    let i = element.children[0];
    if(i) i.className = rightClassName;
    else element.className = rightClassName;
}

//lock menu actions
function unlockMenu(element){
    locked = false;
    
    let i = element.children[0];
    if(i) i.className = unlockedClassName;
    else element.className = unlockedClassName;
}

function lockMenu(element){
    locked = true;
    
    let i = element.children[0];
    if(i) i.className = lockedClassName;
    else element.className = lockedClassName;
}





////////////////////////////////////////////////// TILE SHEETS EVENTS ////////////////////////////////////////////////////
var tileSheet1 = document.getElementById("tileSheet1");
var marker1 = document.getElementById("marker1");
var imageTable1 = document.getElementById("imageTable1");

var tileSheet2 = document.getElementById("tileSheet2");
var marker2 = document.getElementById("marker2");
var imageTable2 = document.getElementById("imageTable2");

var imageDimension = 16;

tileSheet1.style.pointerEvents = "none";
tileSheet2.style.pointerEvents = "none";

imageTable1.style.width = tileSheet1.width+"px";
imageTable1.style.height = tileSheet1.height+"px";

imageTable2.style.width = tileSheet2.width+"px";
imageTable2.style.height = tileSheet2.height+"px";

createImageTable1();
createImageTable2();

function createImageTable1(){
    
    for(let y = 0; y < tileSheet1.height/imageDimension; y++){
    
       let r = document.createElement("tr"); 
       r.style.width = tileSheet1.width+"px";
       r.style.height = imageDimension+"px";    

       for(let x = 0; x < tileSheet1.width/imageDimension; x++){

           let c = document.createElement("td");
           c.style.width = imageDimension+"px";
           c.style.height = imageDimension+"px"; 

           c.addEventListener("mouseenter",(event)=>{
              marker1.style.top = event.target.offsetTop+"px";
              marker1.style.left = event.target.offsetLeft+"px";  
              //console.log(event.target);
           });
           
           

           r.appendChild(c);
       }

       imageTable1.appendChild(r);        
    }
    
    
    
    imageTable1.addEventListener("click", (e)=> {
        //select tile
        let tile_pos = select_tile(e, marker1);
    })
}

function createImageTable2(){
    
    for(let y = 0; y < tileSheet2.height/imageDimension; y++){
    
       let r = document.createElement("tr"); 
       r.style.width = tileSheet2.width+"px";
       r.style.height = imageDimension+"px";    

       for(let x = 0; x < tileSheet2.width/imageDimension; x++){

           let c = document.createElement("td");
           c.style.width = imageDimension+"px";
           c.style.height = imageDimension+"px"; 

           c.addEventListener("mouseenter",(event)=>{
              marker2.style.top = event.target.offsetTop+"px";
              marker2.style.left = event.target.offsetLeft+"px";   
           });

           r.appendChild(c);
       }

       imageTable2.appendChild(r);        
    }
    
    imageTable2.addEventListener("click", (e)=>{
        //select tile
        let tile_pos = select_tile(e, marker2);   
        //todo: more stuff later
    })
}


let temp_canvas = document.getElementById("cnvs");
let temp_ctx = temp_canvas.getContext("2d");
let selected_cell_data;

function select_tile(event, marker, imageTable){
    
    //use the correct tile sheet depending on the marker
    let tile_sheet = (marker.getAttribute("idx") == "1") ? tileSheet1 : tileSheet2; 
    
    //get the tile sheet PIXEL coordinates based on marker style position
    let x = Number.parseInt(marker.style.left);
    let y = Number.parseInt(marker.style.top);
    
    //get the tile sheet GRID coorinates based on the PIXEL coordinates
    let pos = {
        "x": Number.parseInt(marker.style.left) / imageDimension,
        "y": Number.parseInt(marker.style.top ) / imageDimension
    }
    
    //we save the PIXEL coordinates (and other cell data) for later use
    //(to attach them to the world grid cells when one is painted)
    selected_cell_data = {
        "atlas_id": marker.getAttribute("idx"),
        "atlas_coords": {
            "x": x,
            "y": y
        }   
    }
     
    
        
    //finally we draw a 16*16 square from the tile sheet starting at PIXEL coords onto the temp_canvas, at 0,0
    temp_ctx.drawImage(tile_sheet, x, y, 16, 16, 0, 0, 32, 32);
    

    return pos;
}

function select_tile_rightclick(data){
    //use the correct tile sheet depending on right-clicked cell
    let tile_sheet = (data.atlas_id == "1") ? tileSheet1 : tileSheet2; 
    let x = data.atlas_coords.x;
    let y = data.atlas_coords.y;
    
    // Update the selected_cell_data to match the right-clicked tile
    selected_cell_data = {
        "atlas_id": data.atlas_id,
        "atlas_coords": {
            "x": x,
            "y": y
        }
    };
    
    temp_ctx.drawImage(tile_sheet, x, y, 16, 16, 0, 0, 32, 32);
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



//FINALLY, UPDATE MAP GRID















