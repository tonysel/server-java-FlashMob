package com.gmail.sellitto.resources.json;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import org.restlet.resource.ServerResource;

import com.gmail.sellitto.flashMob.backend.wrapper.GroupRegistryAPI;
import com.gmail.sellitto.flashMob.commons.Group;
import com.gmail.sellitto.flashMob.commons.InvalidException;
import com.google.gson.Gson;



public class SubscribersForGroupJSON extends ServerResource {
	
	 
    @Post
    public String add(String payload) throws ParseException  {   	
    	Gson gson = new Gson();
  
   GroupRegistryAPI grapi = GroupRegistryAPI.instance();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
    String dateInString = getAttribute("fdateGroup");
    
    String fnameGroup = getAttribute("fnameGroup").replaceAll("%20{1,}", " ");
    
		Date result = null;
	    //date = formatter.parse(dateInString);
	    result = formatter.parse(dateInString);
		//result.setTime(formatter.parse("2017-12-12_08:12"));
	    //System.out.println(result);
	    Group group = null;
		try {
			group = grapi.getByTitleandDate(fnameGroup, result);
		} catch (InvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
    	
    	
	 	String username = gson.fromJson(payload, String.class);
	 	
	 	System.out.println(username);
    	
   
	 	try {
			grapi.addPartecipantToGroup(username, group);
			return gson.toJson("Insered user successfully: " + username, String.class);
		} catch (InvalidException e1) {
			Status s = new Status(com.gmail.sellitto.flashMob.commons.ErrorCodes.INVALID_USERNAME);
    			setStatus(s);
    			return gson.toJson(e1, InvalidException.class);
		}
	     //Group group = grapi.getByTitleandDate(getAttribute("fnameGroup"), result);
    
    }
    
    
    @Get
    public String getPartecipants() throws ParseException  {   	
    	Gson gson = new Gson();
  
    GroupRegistryAPI grapi = GroupRegistryAPI.instance();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
    String dateInString = getAttribute("fdateGroup");
    
    String fnameGroup = getAttribute("fnameGroup").replaceAll("%20{1,}", " ");
    
		Date result = null;
	    //date = formatter.parse(dateInString);
	    result = formatter.parse(dateInString);
		//result.setTime(formatter.parse("2017-12-12_08:12"));
	    //System.out.println(result);
	    Group group = null;
		try {
			group = grapi.getByTitleandDate(fnameGroup, result);
		} catch (InvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			return gson.toJson(group.getPartecipants());
	     
    
    }  	    
    	  
}
