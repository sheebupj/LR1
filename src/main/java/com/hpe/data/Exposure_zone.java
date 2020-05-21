package com.hpe.data;

import java.util.HashMap;

public class Exposure_zone {
	private String id;
	private String name;
	private String description;
	private String auto_ship_to;
	private String tag;
	private HashMap<String, String> state= new HashMap<String, String>();
	private HashMap<String, String> country= new HashMap<String, String>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuto_ship_to() {
		return auto_ship_to;
	}
	public void setAuto_ship_to(String auto_ship_to) {
		this.auto_ship_to = auto_ship_to;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public HashMap<String, String> getState() {
		return state;
	}
	public void setState(HashMap<String, String> state) {
		this.state = state;
	}
	public HashMap<String, String> getCountry() {
		return country;
	}
	public void setCountry(HashMap<String, String> country) {
		this.country = country;
	}
	
	

}
