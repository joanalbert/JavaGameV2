window.ct_flags = {
    ct_brush: false,
    ct_area: false,
    ct_fill: false
}

window.ct_btns = {
    ct_brush: document.getElementById("col_walk"),
    ct_area: document.getElementById("col_block"),
    ct_fill: document.getElementById("col_surf")
}




//add listeners to al tool buttons
Object.values(ct_btns).forEach(item => {

    item.addEventListener("click", (e)=>{
        ct_resetAll();
        
        ct_updateStyles(e.target);
        ct_updateFlags(e.target);
        
        let clicked_btn = e.target;
        update_prev_img(clicked_btn);
    });
    
});



function update_prev_img(clicked_btn){
    let preview_img = document.getElementsByClassName("coll_prev_img")[0];
    
    let id = clicked_btn.getAttribute("id");
    
    switch(id){
        case "col_walk":
            preview_img.setAttribute("src", "/img/colision/WALK.png");   
            preview_img.setAttribute("colision_type","W");
        break;
        case "col_block":
            preview_img.setAttribute("src", "/img/colision/BLOCK.png");   
            preview_img.setAttribute("colision_type","B");
        break;
        case "col_surf":
            preview_img.setAttribute("src", "/img/colision/SURF.png");   
            preview_img.setAttribute("colision_type","S");
        break;
    }
}

function ct_resetAll(){    
    //set all flags to false
    Object.keys(window.ct_flags).forEach(key => window.ct_flags[key] = false );
   
    //remove the selectd class from all btns
    Object.values(window.ct_btns).forEach(i => i.classList.remove("border-white"));
}


function ct_updateStyles(active){
   active.classList.add("border-white");
}

function ct_updateFlags(active){
   window.ct_flags[active.id] = !window.ct_flags[active.id]
}