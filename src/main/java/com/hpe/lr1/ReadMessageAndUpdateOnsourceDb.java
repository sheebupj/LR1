package com.hpe.lr1;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.hpe.data.ExemptCertDTO;
import com.hpe.data.MessageBody;
import com.hpe.utilities.HttpClientConnectionUtility;
import com.hpe.utilities.JdbcUtilities;
import com.hpe.utilities.utility;

public class ReadMessageAndUpdateOnsourceDb {
	final static Logger logger = Logger.getLogger(ReadMessageAndUpdateOnsourceDb.class);

	ArrayBlockingQueue<ExemptCertDTO> queue;
	String clientId;
	String queueName;
	int stop;
	boolean delete;
	public boolean flag;

	public ReadMessageAndUpdateOnsourceDb() {
		// TODO Auto-generated constructor stub
		// queue = new QueueMocker().getArrayBlockingQueue();
		ResourceBundle rb = utility.getResourceBundle();

		clientId = rb.getString("clientId");
		queueName = rb.getString("queueName");
		stop = Integer.parseInt(rb.getString("stop"));
		delete = new Boolean(rb.getString("delete"));
		flag = true;
	}

	public ExemptCertDTO getMessage() {

		return queue.peek();
	}

	public void updateCertDetailstoDb() {

		logger.info("Queue Contain " + queue.size() + " Messages");
		for (ExemptCertDTO certDTO : queue) {

			if (JdbcUtilities.insertExemptDetailsToOnesourceDb(certDTO, true) == 1) {
				queue.poll();
				logger.info("@dxc-info:Queue Contain " + queue.size() + " Messages");
			}
		}
	}

	public static void main(String[] args) {
		ReadMessageAndUpdateOnsourceDb readUpdateOnsourceDb = new ReadMessageAndUpdateOnsourceDb();
		// readUpdateOnsourceDb.updateCertDetailstoDb();
		ReadDataFromCertCapture capture = new ReadDataFromCertCapture();
		ProcessMessageQueue messageQueue = new ProcessMessageQueue();
		int messagesize = messageQueue.messageQueueSize();
		//messagesize=1;
		logger.info("@dxc-info:Queue size:" + messagesize + "  process up to:" + readUpdateOnsourceDb.stop);
		while (messagesize > readUpdateOnsourceDb.stop) {
			List<MessageBody> messages = messageQueue.getMessges();
			//List<MessageBody> messages = messageQueue.getMockMessages();

			utility.analyseMessgeBodies(messages);
			HttpClientConnectionUtility.addCertCaptureApiUrl(messages);
			HttpClientConnectionUtility.addCustCertCaptureApiUrl(messages);
			ExemptCertDTO certDTO = new ExemptCertDTO();

			// CloseableHttpClient httpclient =
			// HttpClientConnectionUtility.getHttpSSLClient();
			CloseableHttpClient httpclient = HttpClientConnectionUtility.getHttpSSLClient1();
			
			for (MessageBody messageBody : messages) {
				if (utility.getClientIds().contains(messageBody.getClient_id())) {
					certDTO.setCertificateStatus(messageBody.getCertFlag());
					// custCertFlag true means it is certificate notification
					logger.info("@dxc-info:Certificate Notification=" + certDTO.isCertificateStatus());
					if (messageBody.getCertFlag()) {

						// logger.info("@dxc-info:certificate id " + certDTO.getExemptCertificateId());
						readUpdateOnsourceDb.processCertNotification(capture, certDTO, httpclient, messageBody,
								readUpdateOnsourceDb);

					} else {

						// logger.info("@dxc-info:customer id " + certDTO.getCustomerId());
						readUpdateOnsourceDb.processCustNotification(capture, certDTO, httpclient, messageBody,
								readUpdateOnsourceDb);

					}

				}
				else {
					logger.info("@dxc-info:"+"===========================This clientid:"+messageBody.getClient_id()+ " not  allowed in current processing========================= ");;
				}
			}

			//
			if (readUpdateOnsourceDb.delete) {
				messageQueue.deleteMessage();
			}
			 messagesize = messageQueue.messageQueueSize();
			logger.info("@dxc-info:Queue size: " + messagesize);
			//messagesize -= 1;
			//messagesize = 0;
			readUpdateOnsourceDb.flag = true;
		}
		logger.info("@dxc-info: ========= Processing  Ended =============");

	}

	private void processCertNotification(ReadDataFromCertCapture capture, ExemptCertDTO certDTO,
			CloseableHttpClient httpClient, MessageBody messageBody,
			ReadMessageAndUpdateOnsourceDb readMessageAndUpdateOnsourceDb) {
		// Long cert_id;
		if (messageBody.getAction().equals("delete")) {
			deleteCertificate(messageBody);
		} else if (messageBody.getAction().equals("update")) {
			// updateCertificate(messageBody);
			capture.readCertDetailsFromCertCapture(certDTO, httpClient, messageBody.getCertUrl(),
					messageBody.getClient_id(), readMessageAndUpdateOnsourceDb);

			if (messageBody.getCertCustUrl() != null) {
				capture.readCustomerDetailsFromCertCapture(certDTO, httpClient, messageBody.getCertCustUrl(),
						messageBody.getClient_id(), readMessageAndUpdateOnsourceDb);
				JdbcUtilities.updateCustomer(certDTO, flag);
			}
			JdbcUtilities.updateCertificate(certDTO, flag);

		} else if (messageBody.getAction().equals("create")) {
			capture.readCertDetailsFromCertCapture(certDTO, httpClient, messageBody.getCertUrl(),
					messageBody.getClient_id(), readMessageAndUpdateOnsourceDb);
			capture.readCustomerDetailsFromCertCapture(certDTO, httpClient, messageBody.getCertCustUrl(),
					messageBody.getClient_id(), readMessageAndUpdateOnsourceDb);
			JdbcUtilities.insertExemptDetailsToOnesourceDb(certDTO, flag);
		}
	}

	private void deleteCertificate(MessageBody messageBody) {

		Long cert_id = Long.valueOf(messageBody.getCertificate().get("id"));
		Boolean certExist = JdbcUtilities.checkCertificateWithThisId(cert_id, flag);
		if (certExist) {
			JdbcUtilities.deleteCertificat(cert_id, flag);
		} else {
			logger.info("Since one source database contain no certificate with this id, no deletion needed");
		}

	}

	private void processCustNotification(ReadDataFromCertCapture capture, ExemptCertDTO certDTO,
			CloseableHttpClient httpClient, MessageBody messageBody,
			ReadMessageAndUpdateOnsourceDb readMessageAndUpdateOnsourceDb) {
		if (messageBody.getAction().equals("delete")) {
			Long cust_Id = Long.valueOf(messageBody.getCustomer().get("id"));
			JdbcUtilities.deleteCustomer(cust_Id, flag);

		} else if (messageBody.getAction().equals("update")) {
			// updateCertificate(messageBody);

			capture.readCustomerDetailsFromCertCaptureSingle(certDTO, httpClient, messageBody.getCustUrl(),
					messageBody.getClient_id(), readMessageAndUpdateOnsourceDb);
			// JdbcUtilities.updateCertificate(certDTO, flag);
			JdbcUtilities.updateCustomer(certDTO, flag);
		} else if (messageBody.getAction().equals("create")) {

			capture.readCustomerDetailsFromCertCaptureSingle(certDTO, httpClient, messageBody.getCustUrl(),
					messageBody.getClient_id(), readMessageAndUpdateOnsourceDb);
			if (certDTO.getCustomerId() != null) {
				JdbcUtilities.insertCustomer(certDTO, flag);
			} else {
				logger.error("@dxc-error" + "Fetching data from certCapture failed");
			}

		}

	}

}
