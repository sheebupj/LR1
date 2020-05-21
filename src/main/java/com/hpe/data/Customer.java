package com.hpe.data;

import java.util.HashMap;

public class Customer {
	private String id;

	private String name;
	private String customer_number ;
	private String  alternate_id ;
	private String created;
	private String  attn_name; 
	private String  address_line1; 
	private String address_line2;
	private String  city ;
	private String zip; 
	private String  phone_number; 
	private String  fax_number;
	private String email_address;
	private String contact_name;
	private HashMap<String,String> state= new HashMap<String,String>();
	private HashMap<String,String> country= new HashMap<String,String>();
	private HashMap<String,String> province= new HashMap<String,String>();
	
	
	
	
	
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public HashMap<String, String> getProvince() {
		return province;
	}
	public void setProvince(HashMap<String, String> province) {
		this.province = province;
	}
	public HashMap<String, String> getCountry() {
		return country;
	}
	public void setCountry(HashMap<String, String> country) {
		this.country = country;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
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
	public HashMap<String, String> getState() {
		return state;
	}
	public void setState(HashMap<String, String> state) {
		this.state = state;
	}
	public String getCustomer_number() {
		return customer_number;
	}
	public void setCustomer_number(String customer_number) {
		this.customer_number = customer_number;
	}
	public String getAlternate_id() {
		return alternate_id;
	}
	public void setAlternate_id(String alternate_id) {
		this.alternate_id = alternate_id;
	}
	public String getAttn_name() {
		return attn_name;
	}
	public void setAttn_name(String attn_name) {
		this.attn_name = attn_name;
	}
	public String getAddress_line1() {
		return address_line1;
	}
	public void setAddress_line1(String address_line1) {
		this.address_line1 = address_line1;
	}
	public String getAddress_line2() {
		return address_line2;
	}
	public void setAddress_line2(String address_line2) {
		this.address_line2 = address_line2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getFax_number() {
		return fax_number;
	}
	public void setFax_number(String fax_number) {
		this.fax_number = fax_number;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	  
	    

}
