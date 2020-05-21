package com.hpe.data;

import java.util.HashMap;

public class MessageBody {
	
	private String client_id;
	private String action;
	private HashMap<String, String > certificate;
	private HashMap<String, String > customer;
	private  Boolean certFlag=false;
	private String certUrl;
	private String custUrl;
	private String certCustUrl;
	
	
	
	
	public String getCertCustUrl() {
		return certCustUrl;
	}
	public void setCertCustUrl(String certCustUrl) {
		this.certCustUrl = certCustUrl;
	}
	public String getCertUrl() {
		return certUrl;
	}
	public void setCertUrl(String certUrl) {
		this.certUrl = certUrl;
	}
	public String getCustUrl() {
		return custUrl;
	}
	public void setCustUrl(String custUrl) {
		this.custUrl = custUrl;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public HashMap<String, String> getCertificate() {
		return certificate;
	}
	public void setCertificate(HashMap<String, String> certificate) {
		this.certificate = certificate;
	}
	
	
	public HashMap<String, String> getCustomer() {
		return customer;
	}
	public void setCustomer(HashMap<String, String> customer) {
		this.customer = customer;
	}
	public Boolean getCertFlag() {
		return certFlag;
	}
	public void setCertFlag(Boolean custCertFlag) {
		this.certFlag = custCertFlag;
	}
	public String toString() {

		return "client_id:" + getClient_id()+ "\nAction:" + getAction() + "\nCertificate Id:"
				+ getCertificate().get("id") ;
	
	}

}
