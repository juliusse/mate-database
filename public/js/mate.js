

function wishGoodThirst(mateButton) {
    var origText = mateButton.innerText;
    var origClass = mateButton.className;
    var origOnClick = mateButton.onclick;
    
    mateButton.innerText = "Guten Durst!";
    mateButton.className = "success";
    mateButton.onclick = "";
    
    setTimeout(function() {
        mateButton.innerText = origText;
        mateButton.className = origClass;
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
            
            var mateButton = row.childNodes[3].childNodes[0];
            wishGoodThirst(mateButton);
        }
    }

    xmlhttp.open("POST", "webservice.php", true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send("task=count&username=" + userName);
}