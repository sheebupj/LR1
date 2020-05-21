package com.hpe.data;

import oracle.jdbc.proxy.annotation.GetCreator;

public class ExemptCertDTO {

	private String exemptReasonCode;
	private String exemptState;
	private String certificateNO;
	private String exemptCertificateId;
	private String certCreated;
	private String certCountry;
	private String expiration_date;
	private String exmpt_percent;
	private String isFullyExempt;
	private String tax_id;
	private String exposureZoneName;
	
	private String customerNo;
	private String customerName;
	private String customerId;
	private String custCountry;
	private String custCreated;
	private String custAddress;
	private String custCity;
	private String custState;
	private String custPostCode;
	private String custProvince;
	private String  contat_phone_number; 
	private String  fax_number;
	private String contact_email_address;
	private String contact_name;
	private boolean certificateStatus;

	
	
	

	public String getExposureZoneName() {
		return exposureZoneName;
	}

	public void setExposureZoneName(String exposureZoneName) {
		this.exposureZoneName = exposureZoneName;
	}

	public String getTax_id() {
		return tax_id;
	}

	public void setTax_id(String tax_id) {
		this.tax_id = tax_id;
	}

	public String getIsFullyExempt() {
		return isFullyExempt;
	}

	public void setIsFullyExempt(String isFullyExempt) {
		this.isFullyExempt = isFullyExempt;
	}

	public String getExmpt_percent() {
		return exmpt_percent;
	}

	public void setExmpt_percent(String exmpt_percent) {
		this.exmpt_percent = exmpt_percent;
	}

	public String getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}

	public String getContat_phone_number() {
		return contat_phone_number;
	}

	public void setContat_phone_number(String contat_phone_number) {
		this.contat_phone_number = contat_phone_number;
	}

	public String getContact_email_address() {
		return contact_email_address;
	}

	public void setContact_email_address(String contact_email_address) {
		this.contact_email_address = contact_email_address;
	}

	public String getFax_number() {
		return fax_number;
	}

	public void setFax_number(String fax_number) {
		this.fax_number = fax_number;
	}

	

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public boolean isCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(boolean certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public String getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

	public String getCustCity() {
		return custCity;
	}

	public void setCustCity(String custCity) {
		this.custCity = custCity;
	}

	public String getCustState() {
		return custState;
	}

	public void setCustState(String custState) {
		this.custState = custState;
	}

	public String getCustPostCode() {
		return custPostCode;
	}

	public void setCustPostCode(String custPostCode) {
		this.custPostCode = custPostCode;
	}

	public String getCustProvince() {
		return custProvince;
	}

	public void setCustProvince(String custProvince) {
		this.custProvince = custProvince;
	}

	public String getCertCountry() {
		return certCountry;
	}

	public void setCertCountry(String certCountry) {
		this.certCountry = certCountry;
	}

	public String getCustCountry() {
		return custCountry;
	}

	public void setCustCountry(String custCountry) {
		this.custCountry = custCountry;
	}

	public String getCustCreated() {
		return custCreated;
	}

	public void setCustCreated(String custCreated) {
		this.custCreated = custCreated;
	}

	public String getCertCreated() {
		return certCreated;
	}

	public void setCertCreated(String certCreated) {
		this.certCreated = certCreated;
	}

	public String getExemptReasonCode() {
		return exemptReasonCode;
	}

	public void setExemptReasonCode(String exemptReasonCode) {
		this.exemptReasonCode = exemptReasonCode;
	}

	public String getExemptState() {
		return exemptState;
	}

	public void setExemptState(String exemptState) {
		this.exemptState = exemptState;
	}

	public String getCertificateNO() {
		return certificateNO;
	}

	public void setCertificateNO(String certificateNO) {
		this.certificateNO = certificateNO;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getExemptCertificateId() {
		return exemptCertificateId;
	}

	public void setExemptCertificateId(String exemptCertificateId) {
		this.exemptCertificateId = exemptCertificateId;
	}

	public String toString() {

		return "Customer No:" + getCustomerNo() + "\nExempt Cert Id:" + getExemptCertificateId() + "\nCustomer Id:"
				+ getCustomerId() + "\nCustomer Name:" + getCustomerName() + "\nCertificate No:" + getCertificateNO()
				+ "\nExempt Reason:" + getExemptReasonCode() + "\nExempt State:" + getExemptState()
				+"\nCustomer created date:" + getCustCreated()+"\nCert Created date:" + getCertCreated();
	}

}
