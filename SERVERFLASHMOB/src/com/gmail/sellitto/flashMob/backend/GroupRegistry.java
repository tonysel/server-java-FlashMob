package com.gmail.sellitto.flashMob.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashSet;

import com.gmail.sellitto.flashMob.commons.InvalidException;

import com.gmail.sellitto.flashMob.commons.Group;

public class GroupRegistry implements Serializable {

	private static final long serialVersionUID = 1L;

		//CONSTRUCTOR
	public GroupRegistry() {
			dictGroups = new HashSet<Group>();
		}

		// return size dictionary

	public int size() {
			return dictGroups.size();
		}
	
	
	public ArrayList<Group> getGroups() {
		ArrayList<Group> groups = new ArrayList<Group>();
		for(Group group : dictGroups) {
			//Group newGroup = new Group(group.getTitle(), group.getDescription(), group.getStartDate(), group.getEndDate());
			groups.add(group);
			}
		
		return groups;
			
		}
		
		public Group getGroupByTitleAndDate(String title, Date startDate) throws InvalidException, ParseException{
			//ArrayList<Group> temp = new ArrayList<Group>();
			boolean founded = false;
			Group oneGroup = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
		   
			for(Group group : dictGroups) {
				
				Date result = null;
			    
			    result = formatter.parse(group.getStartDate());
				if (group.getTitle().equals(title) && result.equals(startDate)) {
					//temp.add(group);
					founded = true;
					oneGroup = group;
				}
			}
		
			//if (temp.isEmpty() == false) {
			//	throw new InvalidTitleException("Invalid title: " + title);}
			
			if (founded == false) throw new InvalidException("Invalid title: " + title);
			return oneGroup;
				
		}
		
	
		
		
		public ArrayList<Group> getRecentGroupSinceDate(Date startDate) throws InvalidException, ParseException {
			ArrayList<Group> temp = new ArrayList<Group>();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
			for (Group group : dictGroups) {
				Date result = null;
			    
			    result = formatter.parse(group.getStartDate());
				if ((result.compareTo(startDate)) >= 0) 
					temp.add(group);
			}
			
			if (temp.isEmpty()) 
				throw new InvalidException("There are no message since this date: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(startDate) );
			return temp;
		}

	

		// PUT new entry in dictionary

		public void addGroup(Group group) throws InvalidException, ParseException {
			//if (group == null)
			//	throw new InvalidTitleException("invalid group");
			//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
			//Date result = null;
			//Date now = new Date();
			//result = formatter.parse(group.getStartDate());
			if (group.getTitle().equals("")) throw new InvalidException("Invalid Title");
			//if (result.compareTo(now) >= 0) throw new InvalidException("Invalid Date");
			if (group.getStartDate().equals("")) throw new InvalidException("Invalid StartDate");
			if (group.getStartDate().compareTo(group.getEndDate()) > 0) {
				throw new InvalidException("The EndDate have to be different");
			}
			ArrayList<Group> temp = new ArrayList<Group>();
			for(Group group1 : dictGroups) {
				
				if (group.getTitle().equals(group1.getTitle()) && group.getStartDate().equals(group1.getStartDate())) {
					temp.add(group);
					
				}
				
			}
			
			if (!temp.isEmpty()) throw new InvalidException("there is another group for this name and date");
				
			
			dictGroups.add(group);
		}
			
		//ADD ONE PHOTO IN A SPECIFIC GROUP
	
		public void addPhotoToGroup(String file, String nameGroup, Date startDate) throws InvalidException, ParseException {
			
			boolean founded = false;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
			
			for(Group group1 : dictGroups) {
				Date result = null;
			    
			    result = formatter.parse(group1.getStartDate());
				if (nameGroup.equals(group1.getTitle()) && startDate.equals(result)) {
					founded = true;
					//dictGroups.remove(group1);
					group1.addPhotoToGroup(file);
				    dictGroups.add(group1);
					
				}
			}
			
			if (founded == false) throw new InvalidException("No Group with title: " + nameGroup);
			
			
		}
		

		
		
		public void addPartecipantToGroup(String partecipant, Group group) throws InvalidException {
			if (partecipant.equals("")) throw new InvalidException("Not valid user");
			if (group.getPartecipants().contains(partecipant)) throw new InvalidException("Existent user");
			dictGroups.remove(group);
			System.out.println(group.toString());
			
			group.addPartecipantToGroup(partecipant);
			dictGroups.add(group);
			
		}
		
	
		public void updateGroup(Group group) throws InvalidException {
			
			if (group == null) {
				throw new InvalidException("Invalid group");
			}
			
			boolean founded = true;
			
			for(Group group1 : dictGroups) {
				
				if (group.getTitle().equals(group1.getTitle()) && group.getStartDate().equals(group1.getStartDate())) {
					dictGroups.remove(group1);
					
				}
				
			}
			
			if (founded == true) throw new InvalidException("Invalid name group or date group for upload");
				
			dictGroups.add(group);
		}
		

		public void save(String fileOutName) throws IOException {
			FileOutputStream fileOut = new FileOutputStream(fileOutName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dictGroups);
			out.close();
			fileOut.close();
		}

		public void load(String fileName) throws IOException, ClassNotFoundException {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			@SuppressWarnings("unchecked")
			HashSet<Group> readObject = (HashSet <Group>) in.readObject();
			dictGroups = readObject;
			in.close();
			fileIn.close();
		}

		//public void printAll() {
		//	for (Group group : dictGroups) {
		//		System.err.println(group.getTitle() + " " + group.getStartDate() + " " + nota.getEndDate());
		//	}
		//}

		private HashSet<Group> dictGroups;

	}

