////////////////////////////////////////////////////////////////////////////

window.flags = {
    brush: false,
    area: false,
    fill: false
}

window.btns = {
    brush: document.getElementById("brush"),
    area: document.getElementById("area"),
    fill: document.getElementById("fill")
}



//add listeners to al tool buttons
Object.values(btns).forEach(item => {

    item.addEventListener("click", (e)=>{
        resetAll();
        
        updateStyles(e.target);
        updateFlags(e.target);
        
    });
    
});


function resetAll(){    
    //set all flags to false
    Object.keys(window.flags).forEach(key => window.flags[key] = false );
   
    //remove the selectd class from all btns
    Object.values(window.btns).forEach(i => i.classList.remove("bg-danger"));
}


function updateStyles(active){
   active.classList.add("bg-danger");
}

function updateFlags(active){
   window.flags[active.id] = !window.flags[active.id]
}

///////////////////////////////////////////////////////////////



function flood_fill_visual(origin_position, newCellData){
    let table = document.getElementById("mapTable");
    let grid = getMatrixFromTable(table);
    
    let grid_width  = Number.parseInt(table.style.width) / 32;
    let grid_height = Number.parseInt(table.style.height) / 32;
    
    let x = origin_position.x
    let y = origin_position.y;
    let data = grid[y][x];
    
    
    
    // Create a deep copy of data
    let data_copy = {
        atlas_id: data.atlas_id,
        atlas_coords: {
            x: data.atlas_coords.x,
            y: data.atlas_coords.y
        }
    };
    
    console.log(grid);
    
    
    if (compareCellData_visual(data, newCellData)) {
        return;
    }
    
    
    fill_iterativeV2(grid, grid_width, grid_height, origin_position, data_copy, newCellData);
    
    
    
    repaint_dirty(grid);
}


function fill_iterativeV2(grid, w, h, location, data, ndata){
    if (compareCellData_visual(data, ndata)) return;
    
    let queue = [[location.x, location.y]];
    
    while(queue.length > 0){
        
        let [current_x, current_y] = queue.pop();
        
        
        
        if(current_x < 0 || current_x >= w || current_y < 0 || current_y >= h)
        {
            console.log("out of bounds");        
            continue;
        }
        
        
        let current_data = grid[current_y][current_x];
        
        if(!current_data){
            console.log("unpainted cell");
            continue;
        }
        
        if(current_data.dirty){
            console.log("already processed");
            continue;
        }
        
        
        let td1 = document.querySelector(`td[x="${current_x+1}"][y="${current_y}"]`);
        let td2 = document.querySelector(`td[x="${current_x-1}"][y="${current_y}"]`);
        
        let td = document.querySelector(`td[x="${current_x}"][y="${current_y}"]`);
        //td.style.outline = "2px dashed red";
        
        if(!compareCellData_visual(current_data, data)){
            console.log("flood boundary");
            continue;
        }
        
        //td.style.outline = "2px dashed blue";
        
        
        grid[current_y][current_x].atlas_coords.x = ndata.atlas_coords.x;
        grid[current_y][current_x].atlas_coords.y = ndata.atlas_coords.y;
        grid[current_y][current_x].atlas_id = ndata.atlas_id;
        grid[current_y][current_x].dirty = true;
        
        td.cell_data = {
            "atlas_id": ndata.atlas_id,
            "atlas_coords": {
                "x": ndata.atlas_coords.x,
                "y": ndata.atlas_coords.y
            },
            dirty: true
        }
         
        
        queue.push([current_x + 1, current_y    ]);
        queue.push([current_x - 1, current_y    ]);
        queue.push([current_x    , current_y + 1]);
        queue.push([current_x    , current_y - 1]);
    }
}



function fill_recursive(grid, x, y, w, h, data, newData){
    if(x < 0 || y < 0 || x > w || y > h || !compareCellData_visual(grid[x][y], data)) return;
    grid[x][y] = newData;
    //grid[x][y].dirty = true;
    
    let td = document.querySelector(`td[x="${x}"][y="${y}"]`);
    td.style.border = "2px dashed blue";
    
    fill(grid, x+1,y,   w, h, data, newData);
    fill(grid, x-1,y,   w, h, data, newData);
    fill(grid, x  ,y+1, w, h, data, newData);
    fill(grid, x  ,y-1, w, h, data, newData);
}

//repaint the grid as per flood_fill modifications
function repaint_dirty(grid){
    for(let y = 0; y < grid.length; y++){
        
        for(let x = 0; x < grid[0].length; x++){
            
            //we only care about repainting the dirty tiles
            if(!grid[y][x] || !grid[y][x].dirty || grid[y][x].dirty == false) continue;
            
            //get drawing toold
            let   td = document.querySelector(`td[x="${x}"][y="${y}"]`);
            //td.style.outline="2px solid limegreen";
            let cnvs = td.children[0];
            let ctx  = cnvs.getContext("2d");
            
            //get the correct sheet to draw from
            let sheet_id = (grid[y][x].atlas_id == 1) ? "tileSheet1" : "tileSheet2";
            let sheet = document.getElementById(sheet_id);
            
            let coords = grid[y][x].atlas_coords;
            ctx.drawImage(sheet, coords.x, coords.y, 16, 16, 0, 0, 32, 32);
            
            
            //this cell is no longer dirty
            grid[y][x].dirty = false;
        }
    }
    
    //let dirties = grid.flat().filter(cell => cell && cell.dirty);
    //dirties.forEach(i=>i.dirty=false)
}

function compareCellData_visual(data1, data2){
    
    if(!data1 || !data2) return false;
    return (
        data1.atlas_id === data2.atlas_id &&
        data1.atlas_coords.x === data2.atlas_coords.x &&
        data1.atlas_coords.y === data2.atlas_coords.y
    );
}



function getMatrixFromTable(table){
        
    //init matrix
    let matrix = [];
    
    //get list
    //get rows
    let rows_dom = table.children;
    for(let i = 0; i < rows_dom.length; i++){
        //for wach row add an array to the matrix
        matrix[i] = [];
        let cells_dom = rows_dom[i].children;
        
        for(let p = 0; p < cells_dom.length; p++){
            //add each cell add data to that new array
            let cell = cells_dom[p];
                        
            let cd = cell.cell_data;
            
            if(cd)
            {
                
                let data_copy = {
                    atlas_id: cd.atlas_id,
                    colision: cd.colision,
                    atlas_coords: {
                        x: cd.atlas_coords.x,
                        y: cd.atlas_coords.y
                    }
                };
                matrix[i].push(data_copy);
            }
            else {
                let empty_data_copy = {
                    atlas_id: "0",
                    colision: "unset",
                    atlas_coords: {
                        x: 0,
                        y: 0
                    }
                }
                matrix[i].push(empty_data_copy);
            }
            
        }
    }
    
    //console.log(matrix);
    
    return matrix;    
}








