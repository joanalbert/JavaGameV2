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
        
        console.log(flags);
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

