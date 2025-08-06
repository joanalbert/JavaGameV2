
let btn = document.getElementById("export");

btn.addEventListener("click", (e)=>{
    let json = make_json();
    download_file(json);
});


function make_json(){
    
    let t     = document.getElementById("mapTable");
    let arr2d = getMatrixFromTable(t); 
    
    let w = Number.parseInt((document.getElementById("width")).value);
    let h = Number.parseInt((document.getElementById("height")).value);
    
    
    console.clear();
    console.log(arr2d);
    
    
    let json = {
        width: w,
        height: h,
        tiles: []
    }

    
    for(let y = 0; y < h; y++){
        for(let x = 0; x < w; x++){
            
            let data = arr2d[y][x];
            
            let tile = {
                grid_pos: {
                    x: x,
                    y: y
                },
                atls_pos: {
                    x: data.atlas_coords.x,
                    y: data.atlas_coords.y
                },
                colision: data.colision,
                atls_id: data.atlas_id,
            }
        
            
            json.tiles.push(tile);
        }
    }
    
    let str_json = JSON.stringify(json, null, 2);
    console.log(str_json);
    
    return str_json;
}

function download_file(json){
    
    const blob = new Blob([json], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    
    link.href = url;
    link.download = `map_${Date.now()}.json`;
    
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
    
    alert("Map exported successfully!");
}