/* 
 * theUrl to be called
 * op = GET / POST / PUT / DELETE
 * payload for POST & PU; null for GET & DELETE
 * ccallback, the function to be called to habdle the response
 */

function httpAsyncOperation(theUrl, op, payload, callback) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4)
        	if (xmlHttp.status == 200)
            	callback(xmlHttp.responseText);
        	else if (!customError(xmlHttp.status,xmlHttp.responseText)) {
        		alert("Error: "+xmlHttp.status+" - "+xmlHttp.statusText);
        		callback("");
        	}
    }
    xmlHttp.open(op, theUrl, true); // true for asynchronous 
    xmlHttp.send(payload);
}
