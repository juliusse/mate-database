

function wishGoodThirst(mateButton) {
    var origText = $(mateButton).html();
    var origClass = mateButton.className;
    var origOnClick = mateButton.onclick;
    
    $(mateButton).html(":-)");
    $(mateButton).toggleClass("btn-success");
    $(mateButton).toggleClass("btn-warning");
    mateButton.onclick = "";
    
    setTimeout(function() {
    	$(mateButton).html(origText);
        $(mateButton).toggleClass("btn-success");
        $(mateButton).toggleClass("btn-warning");
        mateButton.onclick = origOnClick;
    },3000);
}

function countMate(userName) {
    var xmlhttp = new XMLHttpRequest ();

    xmlhttp.onreadystatechange = function()
    {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200)
        {
            
            var row = document.getElementById("row-"+userName);
            row.innerHTML = xmlhttp.responseText;
            
            var mateButton = $(row).find("button")[0];
            wishGoodThirst(mateButton);
        }
    }

    xmlhttp.open("POST", "/count", true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("username=" + userName);
}