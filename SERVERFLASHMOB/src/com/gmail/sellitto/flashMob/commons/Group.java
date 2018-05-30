package com.gmail.sellitto.flashMob.commons;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/* 
 
 * (Pu� essere vista come una entry della lista di utenti del realm, con relativo ruolo, ovvero identifier + secret + role)
 * (Realm o User risultano non serializzabili, propriet� utile per la persistenza su file)
 */

public class Group implements Serializable {


	private static final long serialVersionUID = -9209880685041545499L;
/*
	public Group(String title, String description, Date startDate, Date endDate, ArrayList<String> images, ArrayList<String> partecipants) {
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.images = images;
		this.partecipants = partecipants;
	}
*/	
	public Group(String title, String description, String startDate, String endDate) {
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.images = new ArrayList<String>();
		this.partecipants = new ArrayList<String>();
	}
	
	public void init() {
		this.images = new ArrayList<>();
		this.partecipants = new ArrayList<>();
	}
	

	public void addPhotoToGroup(String path) {
		images.add(path);
	}
	
	public void addPartecipantToGroup(String partecipant) {
		partecipants.add(partecipant);
		for (String p : partecipants) {
			System.out.println("1:   ...." + p);
		}
	}
	
	

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public ArrayList<String> getImages() {
		return images;
	}


	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public ArrayList<String> getPartecipants() {
		return partecipants;
	}

	public void setPartecipants(ArrayList<String> partecipants) {
		this.partecipants = partecipants;
	}

	
	public String toString() {
		return "Group [title=" + title + ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	private String title, description;
	private String startDate;
	private String endDate;
	
	private ArrayList<String> images ;
	private ArrayList<String> partecipants;

	

}
