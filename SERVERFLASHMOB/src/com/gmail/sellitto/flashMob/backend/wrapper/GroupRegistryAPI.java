package com.gmail.sellitto.flashMob.backend.wrapper;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.gmail.sellitto.flashMob.backend.GroupRegistry;
import com.gmail.sellitto.flashMob.commons.InvalidException;
import com.gmail.sellitto.flashMob.commons.Group;


public class GroupRegistryAPI {

	// create object singleton
	public static synchronized GroupRegistryAPI instance() 
	{
		if (instance == null)
			instance = new GroupRegistryAPI();
		return instance;
	}

	protected GroupRegistryAPI(){
		gr = new GroupRegistry();
	}

	public synchronized int size() {
		return gr.size();
	}
	
	public synchronized ArrayList<Group> getGroups() {
		return gr.getGroups();
	}
	
	
	public synchronized Group getByTitleandDate(String title, Date date) throws InvalidException, ParseException{
		return gr.getGroupByTitleAndDate(title, date);}
	
	
	public synchronized void add(Group group) throws InvalidException, ParseException{
		gr.addGroup(group);
		commit();
	}

	public synchronized void updateGroup(Group group) throws InvalidException{
		gr.updateGroup(group);
		commit();
	}
	
	
	public synchronized void addPhotoToGroup(String file, String nameGroup, Date startDate) throws InvalidException, ParseException {	
		gr.addPhotoToGroup(file, nameGroup, startDate);
		commit();
	}
	
	
	public synchronized void addPartecipantToGroup(String partecipant, Group group) throws InvalidException {	
		gr.addPartecipantToGroup(partecipant, group);
		commit();
	}
	
	
	public void setStorageFiles(String rootDirForStorageFile, String baseStorageFile) {
		this.rootDirForStorageFile = rootDirForStorageFile;
		this.baseStorageFile = baseStorageFile;
		System.err.println("Object Storage Directory: " + this.rootDirForStorageFile);
		System.err.println("Object Storage Base File: " + this.baseStorageFile);
	}

	protected int buildStorageFileExtension() {
		final File folder = new File(rootDirForStorageFile);
		int c;
		int max = -1;

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.getName().substring(0, baseStorageFile.length()).equalsIgnoreCase(baseStorageFile)) {
				try {
					c = Integer.parseInt(fileEntry.getName().substring(baseStorageFile.length() + 1));
				} catch (NumberFormatException | StringIndexOutOfBoundsException e) {
					c = -1;
				}
				if (c > max)
					max = c;
			}
		}
		return max;
	}

	// save commit modifications

	public synchronized void commit() {
		int extension = buildStorageFileExtension();
		String fileName = rootDirForStorageFile + baseStorageFile + "." + (extension + 1);
		System.err.println("Commit storage to: " + fileName);
		try {
			gr.save(fileName);
		} catch (IOException e) {
			System.err.println("Commit filed " + e.getMessage() + " " + e.getCause());
		}
	}

	public synchronized void restore() {
		int extension = buildStorageFileExtension();
		if (extension == -1) {
			System.err.println("No data to load - starting a new registry");
		} else {
			String fileName = rootDirForStorageFile + baseStorageFile + "." + extension;
			System.err.println("Restore storage from: " + fileName);
			try {
				gr.load(fileName);
			} catch (ClassNotFoundException | IOException e) {
				System.err.println("Restore filed - starting a new registry " + e.getCause() + " " + e.getMessage());
				gr = new GroupRegistry();
			}
		}
	}

	//public void printAll() {
	//	gr.printAll();
	//}

	private static GroupRegistryAPI instance;
	private GroupRegistry gr;
	private String rootDirForStorageFile;
	private String baseStorageFile;


}