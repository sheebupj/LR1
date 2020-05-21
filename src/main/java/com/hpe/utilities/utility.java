package com.hpe.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.xml.bind.DatatypeConverter;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.hpe.data.Certificate2;
import com.hpe.data.Customer;
import com.hpe.data.ExemptCertDTO;
import com.hpe.data.MessageBody;

public class utility {
	final static Logger logger = Logger.getLogger(utility.class);
	private static Map<String, Integer> customerGroup;
	private static List<String> clientIds;
	
	static {
		customerGroup = new HashMap<String, Integer>();
		customerGroup.put("Ireland", 569);
		customerGroup.put("Italy", 570);
		customerGroup.put("US", 567);
		customerGroup.put("CA", 568);
		customerGroup.put("Worldwide", 571);
		clientIds=getClientIdList();

	}
	public static List<String> getClientIds() {
		return clientIds;
		
	}
	private static List<String> getClientIdList(){
		ResourceBundle rb = utility.getResourceBundle();
		String clientIdString= rb.getString("clientIds");
		String[] clientIds=clientIdString.split(",");
		List<String> clientIdList=Arrays.asList(clientIds);
		logger.info("@dxc-info:"+"Allowable clientids "+clientIdList);
		return clientIdList;
		
	}

	public static Map<String, Integer> getCustomerGroup() {
		return customerGroup;
	}

	public static String encodeCredentials(String userName, String password) {
		try {
			return DatatypeConverter.printBase64Binary((userName + ":" + userName).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:"+"ExcepTion happend while credetial encoding ");
		}
		return null;

	}

	public static void showData(String apiOutput, ObjectMapper mapper) {
		Object json = null;

		try {
			json = mapper.readValue(apiOutput, Object.class);
			String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			logger.info("@dxc-info:"+indented);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:"+"ExcepTion happend while json parsing ");
		}

	}

	public static ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	public static void analyseMessgeBodies(List<MessageBody> messageBodies) {
		for (MessageBody messageBody : messageBodies) {
			if (messageBody.getCustomer() == null) {
				messageBody.setCertFlag(true);
				logger.info("@dxc-info:"+"Certificate Notification : "+messageBody.getAction());
			}
			else {
				logger.info("@dxc-info:"+"Customer Notification "+messageBody.getAction());
			}
		}
	}
	
	public static ResourceBundle getResourceBundle() {
		//String dir="C:/Users/spj/Documents";
		String dir="/home/vkumard3";
		String fileName="lr1.properties";
		//return ResourceBundle.getBundle("lr1");
		try {
			return fromFile(dir, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	 private static ResourceBundle fromFile(String dir, String fileName) throws IOException {
	        FileInputStream fis = new FileInputStream(dir + "/" + fileName);
	        try {
	            return new PropertyResourceBundle(fis);
	        } finally {
	            fis.close();
	        }
	    }

}
