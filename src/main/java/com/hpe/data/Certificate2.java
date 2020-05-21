package com.hpe.data;

import java.util.HashMap;

public class Certificate2 {

	private String id;
	private String client_id;
	private String signed_date;
	private String  expiration_date;
	private String  filename;
	private String  never_renew;
	private String  renewable;
	private String  replacement ;
	private String  created ;
	private String modified ;
	private Boolean  valid ;
	private Boolean  verified ;
	private String  certificate_number;
	private String  verification_number ;
	private String unused_multi_cert ;
	private String  exmpt_percent ;
	private String  barcode_read ;
	private String tax_number;
	private Boolean is_single ;
	private String  legacy_certificate_id ;
	private String  calc_id ;
	private String  tax_number_type ;
	private String  business_number ;
	private String  business_number_type ;
	private String exempt_reason_description ;
	private String  sst_metadata ;
	private String page_count ;
	private String  communication_id;
	private String  custom1 ;
	private String  custom2 ;
	private String  custom3 ;
	private Exposure_zone exposure_zone;
	
	private HashMap<String, String> state= new HashMap<String, String>();
	private HashMap<String, String> expected_tax_code= new HashMap<String, String>();
	private HashMap<String, String> country= new HashMap<String, String>();

	
	


	
	
	
	public Exposure_zone getExposure_zone() {
		return exposure_zone;
	}
	public void setExposure_zone(Exposure_zone exposure_zone) {
		this.exposure_zone = exposure_zone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getSigned_date() {
		return signed_date;
	}
	public void setSigned_date(String signed_date) {
		this.signed_date = signed_date;
	}
	public String getExpiration_date() {
		return expiration_date;
	}
	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getNever_renew() {
		return never_renew;
	}
	public void setNever_renew(String never_renew) {
		this.never_renew = never_renew;
	}
	public String getRenewable() {
		return renewable;
	}
	public void setRenewable(String renewable) {
		this.renewable = renewable;
	}
	public String getReplacement() {
		return replacement;
	}
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	public String getCertificate_number() {
		return certificate_number;
	}
	public void setCertificate_number(String certificate_number) {
		this.certificate_number = certificate_number;
	}
	public String getVerification_number() {
		return verification_number;
	}
	public void setVerification_number(String verification_number) {
		this.verification_number = verification_number;
	}
	public String getUnused_multi_cert() {
		return unused_multi_cert;
	}
	public void setUnused_multi_cert(String unused_multi_cert) {
		this.unused_multi_cert = unused_multi_cert;
	}
	public String getExmpt_percent() {
		return exmpt_percent;
	}
	public void setExmpt_percent(String exmpt_percent) {
		this.exmpt_percent = exmpt_percent;
	}
	public String getBarcode_read() {
		return barcode_read;
	}
	public void setBarcode_read(String barcode_read) {
		this.barcode_read = barcode_read;
	}
	public String getTax_number() {
		return tax_number;
	}
	public void setTax_number(String tax_number) {
		this.tax_number = tax_number;
	}
	public Boolean getIs_single() {
		return is_single;
	}
	public void setIs_single(Boolean is_single) {
		this.is_single = is_single;
	}
	public String getLegacy_certificate_id() {
		return legacy_certificate_id;
	}
	public void setLegacy_certificate_id(String legacy_certificate_id) {
		this.legacy_certificate_id = legacy_certificate_id;
	}
	public String getCalc_id() {
		return calc_id;
	}
	public void setCalc_id(String calc_id) {
		this.calc_id = calc_id;
	}
	public String getTax_number_type() {
		return tax_number_type;
	}
	public void setTax_number_type(String tax_number_type) {
		this.tax_number_type = tax_number_type;
	}
	public String getBusiness_number() {
		return business_number;
	}
	public void setBusiness_number(String business_number) {
		this.business_number = business_number;
	}
	public String getBusiness_number_type() {
		return business_number_type;
	}
	public void setBusiness_number_type(String business_number_type) {
		this.business_number_type = business_number_type;
	}
	public String getExempt_reason_description() {
		return exempt_reason_description;
	}
	public void setExempt_reason_description(String exempt_reason_description) {
		this.exempt_reason_description = exempt_reason_description;
	}
	public String getSst_metadata() {
		return sst_metadata;
	}
	public void setSst_metadata(String sst_metadata) {
		this.sst_metadata = sst_metadata;
	}
	public String getPage_count() {
		return page_count;
	}
	public void setPage_count(String page_count) {
		this.page_count = page_count;
	}
	public String getCommunication_id() {
		return communication_id;
	}
	public void setCommunication_id(String communication_id) {
		this.communication_id = communication_id;
	}
	public String getCustom1() {
		return custom1;
	}
	public void setCustom1(String custom1) {
		this.custom1 = custom1;
	}
	public String getCustom2() {
		return custom2;
	}
	public void setCustom2(String custom2) {
		this.custom2 = custom2;
	}
	public String getCustom3() {
		return custom3;
	}
	public void setCustom3(String custom3) {
		this.custom3 = custom3;
	}
	public HashMap<String, String> getState() {
		return state;
	}
	public void setState(HashMap<String, String> state) {
		this.state = state;
	}
	public HashMap<String, String> getExpected_tax_code() {
		return expected_tax_code;
	}
	public void setExpected_tax_code(HashMap<String, String> expected_tax_code) {
		this.expected_tax_code = expected_tax_code;
	}
	public HashMap<String, String> getCountry() {
		return country;
	}
	public void setCountry(HashMap<String, String> country) {
		this.country = country;
	}
	
	
	 
}
