
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
            tr.appendChild(td);
        }
        
        mapTableElement.appendChild(tr);
    }
    
    createCollisionsTable();
}

//copy the map table, and from that, create the collisions table (and assign its rows and columns the propper class name)
function createCollisionsTable(){
    let parent = document.getElementById("collisionsTableParent");
    let t  = document.createElement("table");
    
    if(parent.children.length > 0) parent.removeChild(parent.children[0]);
    
    t = mapTableElement.cloneNode(true);
    
    t.id = "mapCollisionsTable";
    
    for(let i = 0; i < t.children.length; i++){
        let row = t.children[i];
        
        row.classList.remove("mapTableRow");
        row.classList.add("mapCollisionsRow");
        
        for(let p = 0; p < row.children.length; p++){
            let cell = row.children[p];
            cell.classList.remove("mapTableColumn");
            cell.classList.add("mapCollisionColumn");
        }
    }
    
    parent.appendChild(t);
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

    td.style.margin  = "none";
    td.style.padding = "none";
    td.style.border = "none";
    
    td.classList.add("mapTableColumn");
    mapTableElement.classList.add("bg-white");
    
    return td;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////////// SIDE MENU SCRIPTS ///////////////////////////////////////////////////////////////////////
let sideToggler = document.getElementById("toggler");
let togglerIcon = document.querySelector("#toggler > i");

let sideMenu = document.getElementById("sideMenu");
let movementLength = 1200;
let hidden = true;

let leftClassName  = "fas fa-arrow-alt-circle-left";
let rightClassName = "fas fa-arrow-alt-circle-right";


togglerIcon.addEventListener("mouseenter", reverseIconOnHover);
togglerIcon.addEventListener("mouseleave", reverseIconOnHover);
togglerIcon.addEventListener("click", (event)=>{
    if(hidden) showMenu(event.target);
    else hideMenu(event.target);
})

function reverseIconOnHover(){}

function hideMenu(element){
    sideMenu.style.left = "100%";
    hidden = true;
    element.className = leftClassName;
}

function showMenu(element){
    sideMenu.style.left = "45%";
    hidden = false;
    element.className = rightClassName;
}







/////////////////// TILE SHEETS EVENTS //////////////////
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
    
}




















