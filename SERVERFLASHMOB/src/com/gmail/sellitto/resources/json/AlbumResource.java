package com.gmail.sellitto.resources.json;

import java.io.File;
import java.text.ParseException;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.gmail.sellitto.flashMob.commons.InvalidException;
import com.google.gson.Gson;

public class AlbumResource extends ServerResource{

@Get
public String getNamesOldPhotos() throws ParseException{
    
	String fnameGroup = getAttribute("fnameGroup").replaceAll("%20{1,}", " ");
	new File(System.getProperty("user.dir")+"/pics/"+ fnameGroup + getAttribute("fdateGroup")).mkdir();
    File path = new File(System.getProperty("user.dir")+"/pics/"+ fnameGroup + getAttribute("fdateGroup"));
    Gson gson = new Gson();
    //String[] imgInDir = null;
   
    			String[] imgInDir =  new String[path.list().length];
    			imgInDir = path.list();
    			
    
    
    			if(imgInDir.length != 0)  return gson.toJson(imgInDir);
    			
    			else {
    				//Status s = new Status(com.gmail.sellitto.flashMob.commons.ErrorCodes.INVALID_USERNAME);
    				//setStatus(s);
    				//return gson.toJson("No Photo");
    				return gson.toJson(imgInDir);
    				}
    	
   
    }
   
}