package com.hpe.data;

public class Message {
	
	private String messageId;
	private String receiptHandle;
	private String mD5OfBody;
	private String body;
	private String client_id;
	private String action;
	private String certificateId;
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getReceiptHandle() {
		return receiptHandle;
	}
	public void setReceiptHandle(String receiptHandle) {
		this.receiptHandle = receiptHandle;
	}
	public String getmD5OfBody() {
		return mD5OfBody;
	}
	public void setmD5OfBody(String mD5OfBody) {
		this.mD5OfBody = mD5OfBody;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
	public String getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}
	
	
	

}
