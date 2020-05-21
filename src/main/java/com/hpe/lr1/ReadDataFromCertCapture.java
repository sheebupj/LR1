package com.hpe.lr1;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.hpe.data.ExemptCertDTO;
import com.hpe.utilities.HttpClientConnectionUtility;
import com.hpe.utilities.JdbcUtilities;
import com.hpe.utilities.JsonParserUtility;

public class ReadDataFromCertCapture {
	final static Logger logger = Logger.getLogger(ReadDataFromCertCapture.class);
	static String base = "https://api.certcapture.com/v2/certificates/";
	static String certificates_prd = "https://api.certcapture.com/v2/certificates";
	static String excemptReasons_prd = "https://api.certcapture.com/v2/exempt-reasons";
	static String getCostomerFromCostNo = "https://api.certcapture.com/v2/customers/599";
	static String customers = "https://api.certcapture.com/v2/customers";

	static String singleCertificate_prd = "https://api.certcapture.com/v2/certificates/500111";
	static String customer_prd = "https://api.certcapture.com/v2/certificates/500111/customers";

	public void readCertDetailsFromCertCapture(ExemptCertDTO certDTO, CloseableHttpClient httpclient, String url,
			String clientId,ReadMessageAndUpdateOnsourceDb readMessageAndUpdateOnsourceDb) {
		// CloseableHttpClient httpclient
		// =HttpClientConnectionUtility.getHttpSSLClient();
		// String clientId = "12125";

		// HPE � Test: 12125 HPEFS: 12124 HPEFS Canada: 12101 HPE Canada: 12122 HPE
		// Canada Test: 12121

		HttpGet httpget = HttpClientConnectionUtility.prepareAndGetHttpGet(url, clientId,readMessageAndUpdateOnsourceDb);

		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// logger.info("@dxc-info:"+"Reading-------------------------------");
			if (response.getStatusLine().getStatusCode() == 200) {
				ObjectMapper mapper = HttpClientConnectionUtility.getObjectMapperFromResponse();
				String apiOutput = JsonParserUtility.convertResponseToString(response);

				JsonParserUtility.populateExemptDTO1(mapper, apiOutput, certDTO);
				response.close();
			}
			else {
				logger.info("@dxc-info:"+"certificate for certificate id"+certDTO.getCertificateNO() +" not found in certCapture");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("@dxc-error:"+"while reading from "+ url+" enexpected error happend");
			readMessageAndUpdateOnsourceDb.flag=false;
		}
	}

	public void readCustomerDetailsFromCertCapture(ExemptCertDTO certDTO, CloseableHttpClient httpclient, String url,
			String clientId,ReadMessageAndUpdateOnsourceDb readMessageAndUpdateOnsourceDb) {

		// CloseableHttpClient httpclient
		// =HttpClientConnectionUtility.getHttpSSLClient();
		// String clientId = "12125";

		// HPE � Test: 12125 HPEFS: 12124 HPEFS Canada: 12101 HPE Canada: 12122 HPE
		// Canada Test: 12121
		

		HttpGet httpget = HttpClientConnectionUtility.prepareAndGetHttpGet(url, clientId,readMessageAndUpdateOnsourceDb);
		if(readMessageAndUpdateOnsourceDb.flag) {
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
			ObjectMapper mapper = HttpClientConnectionUtility.getObjectMapperFromResponse();
			String apiOutput = JsonParserUtility.convertResponseToString(response);
			//System.out.println(apiOutput);

			JsonParserUtility.populateExemptDTO2(mapper, apiOutput, certDTO);
			response.close();
			}
			else
			{
				logger.error("@dxc-error:"+"Customer for customer id"+certDTO.getCustomerId() +" not found in certCapture");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("@dxc-error:"+"while reading from "+ url+" enexpected error happend");
			readMessageAndUpdateOnsourceDb.flag=false;
		}
		}
	}
	public void readCustomerDetailsFromCertCaptureSingle(ExemptCertDTO certDTO, CloseableHttpClient httpclient, String url,
			String clientId,ReadMessageAndUpdateOnsourceDb readMessageAndUpdateOnsourceDb) {

		// CloseableHttpClient httpclient
		// =HttpClientConnectionUtility.getHttpSSLClient();
		// String clientId = "12125";

		// HPE � Test: 12125 HPEFS: 12124 HPEFS Canada: 12101 HPE Canada: 12122 HPE
		// Canada Test: 12121
		

		HttpGet httpget = HttpClientConnectionUtility.prepareAndGetHttpGet(url, clientId,readMessageAndUpdateOnsourceDb);
		if(readMessageAndUpdateOnsourceDb.flag) {
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
			ObjectMapper mapper = HttpClientConnectionUtility.getObjectMapperFromResponse();
			String apiOutput = JsonParserUtility.convertResponseToString(response);
			//System.out.println(apiOutput);

			JsonParserUtility.populateExemptDTO2Single(mapper, apiOutput, certDTO);
			response.close();
			}
			else
			{
				logger.error("@dxc-error:"+"Customer for customer id"+certDTO.getCustomerId() +" not found in certCapture");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("@dxc-error:"+"while reading from "+ url+" enexpected error happend");
			readMessageAndUpdateOnsourceDb.flag=false;
		}
		}
	}

	public static void main(String[] args) {

		QueueMocker queue = new QueueMocker();
		ArrayBlockingQueue<ExemptCertDTO> certDTOs = queue.getArrayBlockingQueue();
		for (ExemptCertDTO certDTO : certDTOs) {
			logger.error("@dxc-error:"+certDTO);
			logger.error("@dxc-error:"+"-----------------------------------------");

		}

	}
}
