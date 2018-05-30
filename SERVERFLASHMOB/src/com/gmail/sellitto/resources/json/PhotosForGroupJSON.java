package com.gmail.sellitto.resources.json;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.gmail.sellitto.flashMob.backend.wrapper.GroupRegistryAPI;
import com.gmail.sellitto.flashMob.commons.Group;
import com.google.gson.Gson;

public class PhotosForGroupJSON extends ServerResource {
    @Post
    public String upload(Representation entity) throws ResourceException {
        try {
            	
        		Gson gson = new Gson();
        		
        		GroupRegistryAPI grapi = GroupRegistryAPI.instance();
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        	    String dateInString = getAttribute("fdateGroup");
        	   
        	    Date result = null;
        	        
        	    //date = formatter.parse(dateInString);
        	    //result =  formatter.parse(dateInString);
        	    result = formatter.parse(dateInString);
        	    
        	    String fnameGroup = getAttribute("fnameGroup").replaceAll("%20{1,}", " ");
        	        	System.out.println(fnameGroup);
        	        	Group group = grapi.getByTitleandDate(fnameGroup, result);
        	        	
        	        	
        	        	if(group != null) {
        	        	
        	        		new File(System.getProperty("user.dir")+"/pics/"+ fnameGroup + getAttribute("fdateGroup")).mkdirs();
                	
                		File file = new File(System.getProperty("user.dir")+"/pics/"+ fnameGroup + getAttribute("fdateGroup") + "/" + getAttribute("fnameFile"));
          
                		entity.write(new FileOutputStream(file));
                
                		//ArrayList<String> test = group.getImages();
                		
                		//test.add(getAttribute("fnameFile"));
                		
                		grapi.addPhotoToGroup(getAttribute("fnameFile"), fnameGroup, result);
                		
                		//grapi.updateGroup(group);
                		
                		//grapi.addPhotoToGroup(getAttribute("fnameFile"),  getAttribute("fnameGroup") , result);
                   
                		System.out.println("Length image in upload... " + file.length() + " Bytes");
                	
        	        	}	
              
        } catch (Exception e) {
            throw new ResourceException(e);
        }
        return "Successfully";
    }  
    
    @Get
    public Representation getOldPhotos() throws ParseException{
    
    		String fnameGroup = getAttribute("fnameGroup").replaceAll("%20{1,}", " ");
    	
    	    File file = new File(System.getProperty("user.dir")+"/pics/"+ fnameGroup + getAttribute("fdateGroup") + "/" + getAttribute("fnameFile"));
    	    FileRepresentation fileRepresentation = new FileRepresentation(file, MediaType.IMAGE_PNG);
    	    
        	return fileRepresentation;
    }
    	    
    	  
}
