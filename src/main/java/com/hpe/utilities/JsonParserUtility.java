package com.hpe.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.hpe.data.Certificate2;
import com.hpe.data.Customer;
import com.hpe.data.ExemptCertDTO;
import com.hpe.lr1.ProcessMessageQueue;

public class JsonParserUtility {
	final static Logger logger = Logger.getLogger(JsonParserUtility.class);

	public static String convertResponseToString(CloseableHttpResponse httpResponse) {
		HttpEntity httpEntity = httpResponse.getEntity();
		try {
			return EntityUtils.toString(httpEntity);
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("@dxc-error:" + "Exception happend while converting response to string");

		}
		return null;
	}

	public static String encodeCredentials(String userName, String password) {
		try {
			return DatatypeConverter.printBase64Binary((userName + ":" + userName).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:" + "Exception happend while encoding credentials");
		}
		return null;

	}

	public static void showData(String apiOutput, ObjectMapper mapper) {
		Object json = null;

		try {
			json = mapper.readValue(apiOutput, Object.class);
			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			System.out.println(indented);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:" + "Exception happend while parsing string to json object");
		}

	}

	public static void populateExemptDTO1(ObjectMapper mapper, String apiOutput, ExemptCertDTO certDTO) {
		Certificate2 cert;
		try {
			// System.out.println(apiOutput);
			cert = mapper.readValue(apiOutput, Certificate2.class);
			certDTO.setExemptCertificateId(cert.getId());
			certDTO.setExemptReasonCode(cert.getExpected_tax_code().get("name"));
			certDTO.setExpiration_date(cert.getExpiration_date());
			certDTO.setCertCreated(cert.getCreated());
			certDTO.setCertCountry(cert.getExposure_zone().getCountry().get("name"));
			certDTO.setExposureZoneName(cert.getExposure_zone().getName());
			certDTO.setExmpt_percent(cert.getExmpt_percent());
			certDTO.setCertificateNO(cert.getCertificate_number());
			certDTO.setTax_id(cert.getTax_number());
			setexemptValues(certDTO, cert);
			if (!validCertNo(certDTO.getCertificateNO())) {
				logger.info("@dxc-info "
						+ "Since Certificate no is null or empty, certificate id is using as certificate No ");
				certDTO.setCertificateNO(certDTO.getExemptCertificateId());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:" + "Exception happend while parsing json string to object");
		}

	}
	public static void setExposureZoneForCanada(ExemptCertDTO certDTO,Certificate2 cert) {
		if(certDTO.getCertCountry().equals("Canada")) {
			certDTO.setExposureZoneName(cert.getExposure_zone().getName());
		}
	}

	public static void populateExemptDTO2(ObjectMapper mapper, String apiOutput, ExemptCertDTO certDTO) {
		Customer[] customers;
		try {
			// System.out.println(apiOutput);
			customers = mapper.readValue(apiOutput, Customer[].class);

			certDTO.setCustomerId(customers[0].getId());
			certDTO.setCustomerNo(customers[0].getCustomer_number());
			certDTO.setCustomerName(customers[0].getName());
			if (customers[0].getState() != null) {
				certDTO.setExemptState(customers[0].getState().get("name"));
				certDTO.setCustState(customers[0].getState().get("initials"));
			} else {
				certDTO.setExemptState(null);
				certDTO.setCustState(null);
			}
			certDTO.setCustCreated(customers[0].getCreated());
			certDTO.setCustCountry(customers[0].getCountry().get("initials"));
			certDTO.setCustAddress(customers[0].getAddress_line1());
			certDTO.setCustCity(customers[0].getCity());
			certDTO.setCustPostCode(customers[0].getZip());

//			if(certDTO.getCustCountry().equals("CA")) {
//				//certDTO.setCustState(customers[0].getState().get("initials"));
//				certDTO.setCustProvince(customers[0].getState().get("initials"));
			// }
			certDTO.setContact_name(customers[0].getContact_name());
			certDTO.setContact_email_address(customers[0].getEmail_address());
			certDTO.setContat_phone_number(customers[0].getPhone_number());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("@dxc-error:" + "Exception happend while parsing json string to object");
		} catch (Exception e) {
			logger.error("@dxc-error:" + "Exception happend while parsing json string to object");

		}

	}

	public static void populateExemptDTO2Single(ObjectMapper mapper, String apiOutput, ExemptCertDTO certDTO) {
		Customer customer;
		try {
			// System.out.println(apiOutput);
			customer = mapper.readValue(apiOutput, Customer.class);

			certDTO.setCustomerId(customer.getId());
			certDTO.setCustomerNo(customer.getCustomer_number());
			certDTO.setCustomerName(customer.getName());
			certDTO.setExemptState(customer.getState().get("name"));
			certDTO.setCustCreated(customer.getCreated());
			certDTO.setCustCountry(customer.getCountry().get("initials"));
			certDTO.setCustAddress(customer.getAddress_line1());
			certDTO.setCustCity(customer.getCity());
			certDTO.setCustPostCode(customer.getZip());
			if (certDTO.getCustCountry().equals("US")) {
				certDTO.setCustState(customer.getState().get("initials"));
			}
			if (certDTO.getCustCountry().equals("CA")) {
				// certDTO.setCustState(customer.getState().get("initials"));
				certDTO.setCustProvince(customer.getState().get("initials"));
			}
			certDTO.setContact_name(customer.getContact_name());
			certDTO.setContact_email_address(customer.getEmail_address());
			certDTO.setContat_phone_number(customer.getPhone_number());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:" + "Exception happend while parsing json string to object");
		} catch (Exception e) {
			logger.error("@dxc-error:" + "Exception happend while parsing json string to object");

		}

	}

	public static boolean validCertNo(String no) {
		if (no == null || no.length() < 1) {
			return false;
		}
		return true;
	}

	public static void setexemptValues(ExemptCertDTO certDTO, Certificate2 cert) {
		
		certDTO.setExmpt_percent(cert.getExmpt_percent());
		if (certDTO.getExmpt_percent().equals("100")) {
			certDTO.setIsFullyExempt("Y");
			return;
		}
		certDTO.setIsFullyExempt("N");

	}

}
