window.c_flags = {
    c_brush: false,
    c_area: false,
    c_fill: false
}

window.c_btns = {
    c_brush: document.getElementById("c_brush"),
    c_area: document.getElementById("c_area"),
    c_fill: document.getElementById("c_fill")
}




//add listeners to al tool buttons
Object.values(c_btns).forEach(item => {

    item.addEventListener("click", (e)=>{
        c_resetAll();
        
        c_updateStyles(e.target);
        c_updateFlags(e.target);
        
    });
    
});


function c_resetAll(){    
    //set all flags to false
    Object.keys(window.c_flags).forEach(key => window.c_flags[key] = false );
   
    //remove the selectd class from all btns
    Object.values(window.c_btns).forEach(i => i.classList.remove("bg-danger"));
}


function c_updateStyles(active){
   active.classList.add("bg-danger");
}

function c_updateFlags(active){
   window.c_flags[active.id] = !window.c_flags[active.id]
}


function cell_events(){
    let cells = document.getElementsByClassName("col_table_cell");
    
    for(let i = 0; i < cells.length; i++){
        let cell = cells[i];
        
        
        //brush
        cell.addEventListener("mouseenter", (e)=>{
            if(window.global_isClicked && window.c_flags.c_brush){
                set_cell_collision_visual(e.target);
                set_cell_collision_logical(e.target);
            }    
        });
        
        cell.addEventListener("click", (e)=>{
                set_cell_collision_visual(e.target);
                set_cell_collision_logical(e.target);   
        });
    }
}


function set_cell_collision_logical(cell){
    
    let x = cell.getAttribute("x");
    let y = cell.getAttribute("y");

    let prev_image = document.getElementsByClassName("coll_prev_img")[0];
    let col_type   = prev_image.getAttribute("colision_type");
 
    let other_cell = document.querySelector(`td.mapTableColumn[x="${x}"][y="${y}"]`);
    other_cell.cell_data.colision = col_type;
    
}

//do come across

function set_cell_collision_visual(cell){
    
    let x = cell.getAttribute("x");
    let y = cell.getAttribute("y");

    let other_cell   = document.querySelector(`td.mapTableColumn[x="${x}"][y="${y}"]`);
    let other_canvas = other_cell.children[0];
    let other_ctx    = other_canvas.getContext("2d");
    
    
    let prev_image = document.getElementsByClassName("coll_prev_img")[0];
    
    let cnvs = cell.getElementsByTagName("canvas")[0];
    let ctx = cnvs.getContext("2d");
    
    
    ctx.clearRect(0,0,32,32);
    ctx.drawImage(other_canvas, 0, 0);
    ctx.globalAlpha = 0.6;
    ctx.drawImage(prev_image, 0, 0);
    ctx.globalAlpha = 1;
}












