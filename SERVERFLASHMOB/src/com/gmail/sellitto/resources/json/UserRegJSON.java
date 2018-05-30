package com.gmail.sellitto.resources.json;

import java.text.ParseException;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.security.MemoryRealm;
import org.restlet.security.User;
import org.restlet.security.Verifier;

import com.gmail.sellitto.flashMob.backend.wrapper.UserRegistryAPI;
import com.gmail.sellitto.flashMob.commons.ErrorCodes;
import com.gmail.sellitto.flashMob.commons.InvalidException;
import com.gmail.sellitto.flashMob.frontend.GroupsRegistryWebApplication;
import com.google.gson.Gson;

public class UserRegJSON extends ServerResource {

	@Post
	public String addUser(String payload) throws ParseException {
		Gson gson = new Gson();
		UserRegistryAPI urapi = UserRegistryAPI.instance();
		
		User u = gson.fromJson(payload, User.class);
		
			try {
				if (GroupsRegistryWebApplication.realmAuth.findUser(u.getIdentifier()) == null) {
				urapi.add(u);
				GroupsRegistryWebApplication.verifier.getLocalSecrets().put(u.getIdentifier(), u.getSecret());
				return gson.toJson("added user: " + u.getIdentifier(), String.class);}
				else {
				Status s = new Status(ErrorCodes.INVALID_USERNAME);
				setStatus(s);
				return gson.toJson("Existent user", String.class);}
			} catch (InvalidException e) {
				Status s = new Status(ErrorCodes.INVALID_USERNAME);
				setStatus(s);
				return gson.toJson("Existent user", String.class);
			}	

	}

/*
	@Put
	public String checkUser(String payload) throws ParseException {
		Gson gson = new Gson();
		UserRegistryAPI urapi = UserRegistryAPI.instance();
		User u = gson.fromJson(payload, User.class);
		
		//if (GroupsRegistryWebApplication.verifier.verify(u.getIdentifier(), u.getSecret()) == Verifier.RESULT_VALID && urapi.get(u.getIdentifier()) != null) {
		try {
			urapi.get(u.getIdentifier());
			return gson.toJson("OK", String.class); 
		}
		catch (InvalidException e) {
			Status s = new Status(ErrorCodes.INVALID_USERNAME);
			setStatus(s);
			return gson.toJson("Invalid username", String.class);
		}
	}
	*/
	

	@Put
	public String checkUser(String payload) {
		Gson gson = new Gson();
		UserRegistryAPI urapi = UserRegistryAPI.instance();
		User u = gson.fromJson(payload, User.class);
		if (GroupsRegistryWebApplication.verifier.verify(u.getIdentifier(), u.getSecret()) == Verifier.RESULT_VALID) {
			return gson.toJson("Ok", String.class);
		} else {
			return gson.toJson("NOT Ok", String.class);
		}
	}

	@Get
	public List<User> check() throws InvalidException {
		
		UserRegistryAPI urapi = UserRegistryAPI.instance();
		
		return urapi.users();
	}
	
}
