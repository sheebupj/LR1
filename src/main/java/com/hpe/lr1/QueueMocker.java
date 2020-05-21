package com.hpe.lr1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.http.impl.client.CloseableHttpClient;

import com.hpe.data.ExemptCertDTO;
import com.hpe.utilities.HttpClientConnectionUtility;

public class QueueMocker {

	private ArrayBlockingQueue<ExemptCertDTO> arrayBlockingQueue;

	public QueueMocker() {
		super();
		// TODO Auto-generated constructor stub
		setArrayBlockingQueue(getQueue());
	}

	public ArrayBlockingQueue<ExemptCertDTO> getArrayBlockingQueue() {
		return arrayBlockingQueue;
	}

	public void setArrayBlockingQueue(ArrayBlockingQueue<ExemptCertDTO> arrayBlockingQueue) {
		this.arrayBlockingQueue = arrayBlockingQueue;
	}

	private List<String[]> getCertCaptureUrls() {
		List<String[]> list = new ArrayList<String[]>();
		String cert1 = "https://api.certcapture.com/v2/certificates/500111";
		String cust1 = "https://api.certcapture.com/v2/certificates/500111/customers";
		String[] url1 = new String[2];
		url1[0] = cert1;
		url1[1] = cust1;

		String cert2 = "https://api.certcapture.com/v2/certificates/500110";
		String cust2 = "https://api.certcapture.com/v2/certificates/500110/customers";
		String[] url2 = new String[2];
		url2[0] = cert2;
		url2[1] = cust2;

		String cert3 = "https://api.certcapture.com/v2/certificates/500109";
		String cust3 = "https://api.certcapture.com/v2/certificates/500109/customers";
		String[] url3 = new String[2];
		url3[0] = cert3;
		url3[1] = cust3;

		String cert4 = "https://api.certcapture.com/v2/certificates/500108";
		String cust4 = "https://api.certcapture.com/v2/certificates/500108/customers";
		String[] url4 = new String[2];
		url4[0] = cert4;
		url4[1] = cust4;

		String cert5 = "https://api.certcapture.com/v2/certificates/500107";
		String cust5 = "https://api.certcapture.com/v2/certificates/500107/customers";
		String[] url5 = new String[2];
		url5[0] = cert5;
		url5[1] = cust5;
		
		String cert6 = "https://api.certcapture.com/v2/certificates/500105";
		String cust6 = "https://api.certcapture.com/v2/certificates/500105/customers";
		String[] url6 = new String[2];
		url6[0] = cert6;
		url6[1] = cust6;
		
		
		String cert7 = "https://api.certcapture.com/v2/certificates/500103";
		String cust7 = "https://api.certcapture.com/v2/certificates/500103/customers";
		String[] url7 = new String[2];
		url7[0] = cert7;
		url7[1] = cust7;
		
		String cert8 = "https://api.certcapture.com/v2/certificates/500104";
		String cust8 = "https://api.certcapture.com/v2/certificates/500104/customers";
		String[] url8 = new String[2];
		url8[0] = cert8;
		url8[1] = cust8;

		list.add(url1);
		list.add(url2);
		list.add(url3);
		list.add(url4);
		list.add(url5);
//		list.add(url6);
//		list.add(url7);
//		list.add(url8);

		return list;

	}

	private ArrayBlockingQueue<ExemptCertDTO> getQueue() {

		String clientId = "12125";
		ArrayBlockingQueue<ExemptCertDTO> queue = new ArrayBlockingQueue<ExemptCertDTO>(5);

		List<String[]> urlList = getCertCaptureUrls();
		ReadDataFromCertCapture capture = new ReadDataFromCertCapture();
		CloseableHttpClient httpclient = HttpClientConnectionUtility.getHttpSSLClient();
		for (String[] urlPair : urlList) {
			ExemptCertDTO certDTO = new ExemptCertDTO();
			System.out.println("Sarting to read message from Certcapture");
			//capture.readCertDetailsFromCertCapture(certDTO, httpclient, urlPair[0], clientId);
			//capture.readCustomerDetailsFromCertCapture(certDTO, httpclient, urlPair[1], clientId);
			try {
				queue.put(certDTO);
				System.out.println(" message loaded ...and Queue contain " + queue.size()+" Messages"); 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return queue;

	}

}
