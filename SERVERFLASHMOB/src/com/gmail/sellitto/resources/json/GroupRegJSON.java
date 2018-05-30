package com.gmail.sellitto.resources.json;

import java.text.ParseException;
import java.util.ArrayList;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import org.restlet.resource.ServerResource;


import com.gmail.sellitto.flashMob.backend.wrapper.GroupRegistryAPI;
import com.gmail.sellitto.flashMob.commons.ErrorCodes;
import com.gmail.sellitto.flashMob.commons.InvalidException;
import com.gmail.sellitto.flashMob.commons.Group;

import com.google.gson.Gson;

public class GroupRegJSON extends ServerResource {


	@Post
	public String addGroup(String payload) throws ParseException{
		Gson gson = new Gson();
		GroupRegistryAPI grapi = GroupRegistryAPI.instance();
	
		Group group = gson.fromJson(payload, Group.class);
		
		try {
			group.init();
			
			grapi.add(group); 
			
			return gson.toJson("added group: " + group.getTitle(), String.class);
		} catch (InvalidException e){    		
    			Status s = new Status(ErrorCodes.INVALID_DATE_CODE);
    			setStatus(s);
    			return gson.toJson(e, InvalidException.class);
		}    
		
	}

}
