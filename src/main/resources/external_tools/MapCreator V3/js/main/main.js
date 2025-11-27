/*let canvas = document.getElementById("wtf");


let clicked = false;

canvas.addEventListener("mousedown", (i) => clicked = true);
canvas.addEventListener("mouseup", (i) => clicked = false);

canvas.addEventListener("mousemove", (e)=> {
    if(clicked) {
        let x = e.clientX;
        let y = e.clientY;
        console.log(`x:${x} y:${y}`)
    }
})*/


/*window.requestAnimationFrame(loop);
function loop(){
    console.log(clicked);
    window.requestAnimationFrame(loop);
}*/

let menu      = document.getElementById("atlas_menu");
let menu_open = false;

document.addEventListener("keydown", (e)=>{
    if(e.keyCode == 32) { //spacebar
     menu_open = !menu_open;   
    }
    
    if(menu_open) menu.style.display = "block";
    else menu.style.display = "none";
        
    
})


let i = {
    tabs: {
        
    }
}


initialize_atlases();
function initialize_atlases(){

    let atls_containers = Array.from(document.getElementsByClassName("atls_container"));
    
    atls_containers.forEach(container =>{
        let id = container.getAttribute("id");
        
        switch(id){
                case "houses":
                //init_houses_canvases(container);
                break;
                case "buildings":
                //
                break;
                case "trainer":
                //
                break;
                case "various":
                //
                break;
                case "bfrontier":
                //
                break;
                
        }
                
        console.log(c);
    });
}


function init_houses_canvases(container){
        
    let img = document.createElement("img");
    img.setAttribute("src", "usable_assets/tile_atlases/city/buildings/houses/basic_house_01.png");
    img.setAttribute("id", "basic_house_01");    
    img.classList.add("atlas_image");
    container.appendChild(img);
    
    img = document.createElement("img");
    img.setAttribute("src", "usable_assets/tile_atlases/city/buildings/houses/basic_house_02.png");
    img.setAttribute("id", "basic_house_02");  
    img.classList.add("atlas_image");
    container.appendChild(img);
}
    





