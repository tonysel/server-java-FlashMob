package com.gmail.sellitto.resources.json;

import java.text.ParseException;
import java.util.ArrayList;

import org.restlet.data.Status;
import org.restlet.resource.Get;


import org.restlet.resource.ServerResource;


import com.gmail.sellitto.flashMob.backend.wrapper.GroupRegistryAPI;
import com.gmail.sellitto.flashMob.commons.ErrorCodes;
import com.gmail.sellitto.flashMob.commons.InvalidException;
import com.gmail.sellitto.flashMob.commons.Group;

import com.google.gson.Gson;

public class TakeGroupsJSON extends ServerResource {

	 @Get
	    public String getGroups() throws ParseException  {   	
	    		Gson gson = new Gson();
	    		GroupRegistryAPI grapi = GroupRegistryAPI.instance();
	    		
	    			ArrayList<Group> groups = grapi.getGroups();
	    			
	    			return gson.toJson(groups);
	    }

}
