package com.hpe.lr1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.hpe.data.MessageBody;
import com.hpe.utilities.HttpClientConnectionUtility;
import com.hpe.utilities.QueueReadUtilty;
import com.hpe.utilities.utility;

public class ProcessMessageQueue {
	final static Logger logger = Logger.getLogger(ProcessMessageQueue.class);
	AmazonSQS sqs;
	List<Message> messagesQueue;
	String myQueueUrl;
	String queueName;

	public List<Message> getMessagesQueue() {
		return messagesQueue;
	}

	public void setMessagesQueue(List<Message> messagesQueue) {
		this.messagesQueue = messagesQueue;
	}

	public AmazonSQS getSqs() {
		return sqs;
	}

	public void setSqs(AmazonSQS sqs) {
		this.sqs = sqs;
	}

	public ProcessMessageQueue() {
		// TODO Auto-generated constructor stub
		ResourceBundle rb = utility.getResourceBundle();
		queueName = rb.getString("queueName");
		ProfileCredentialsProvider credentialsProvider = QueueReadUtilty.getProfileCredentialsProviderr();
		sqs = QueueReadUtilty.getAmazonSQS(credentialsProvider);
		GetQueueUrlResult getQueueUrlResult = sqs.getQueueUrl(queueName);
		myQueueUrl = getQueueUrlResult.getQueueUrl();
	}

	public List<MessageBody> getMessges() {
		List<MessageBody> messageList = new ArrayList<MessageBody>();

		try {

			// Receive messages
			logger.info("@dxc-info:"+"Receiving messages from MyQueue.\n");
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
			messagesQueue = sqs.receiveMessage(receiveMessageRequest).getMessages();
			logger.info("Received " + messagesQueue.size() + " Messages");
			// String messageBody=null;
			ObjectMapper mapper = HttpClientConnectionUtility.getObjectMapperFromResponse();

			for (Message message : messagesQueue) {
				
				//displayMessage(message);
				try {
					MessageBody messageBody = mapper.readValue(message.getBody(), MessageBody.class);
					messageList.add(messageBody);
					logger.info("@dxc-info:Received Message with");
					logger.info("@dxc-info:id "+ message.getMessageId());
					logger.info("@dxc-info:clientid "+ messageBody.getClient_id());
					logger.info("@dxc-info:message body "+ message.getBody());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("@dxc-error: an exception occured while getting data from message");
				}

			}

		} catch (AmazonServiceException ase) {
			logger.error("@dxc-error:"+"Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SQS, but was rejected with an error response for some reason.");
			logger.error("@dxc-error:"+"Error Message:    " + ase.getMessage());
			logger.error("@dxc-error:"+"HTTP Status Code: " + ase.getStatusCode());
			logger.error("@dxc-error:"+"AWS Error Code:   " + ase.getErrorCode());
			logger.error("@dxc-error:"+"Error Type:       " + ase.getErrorType());
			logger.error("@dxc-error:"+"Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.error("@dxc-error:"+"Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SQS, such as not "
					+ "being able to access the network.");
			logger.error("@dxc-error:"+"Error Message: " + ace.getMessage());
		}
		return messageList;
	}
	
	public List<MessageBody> getMockMessages(){
		List<MessageBody> messageList = new ArrayList<MessageBody>();
		MessageBody messageBody= new MessageBody();
		messageBody.setAction("create");
		messageBody.setClient_id("12121");
		HashMap<String, String> certificate= new HashMap<String, String>();
		certificate.put("id", "951232");
		messageBody.setCertificate(certificate);
		messageList.add(messageBody);
		
		
		
		
		
		return messageList;
		
	}

	void deleteMessage() {
		try {

			logger.info("@dxc-info:"+"Deleting a message.\n");

			String messageReceiptHandle = messagesQueue.get(0).getReceiptHandle();
			sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));

			logger.info("@dxc-info:"+"Deleted a message.\n");
			logger.info("@dxc-info:"+"message size:" + messageQueueSize());

		} catch (AmazonServiceException ase) {
			logger.error("@dxc-error:"+"Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SQS, but was rejected with an error response for some reason.");
			logger.error("@dxc-error:"+"Error Message:    " + ase.getMessage());
			logger.error("@dxc-error:"+"HTTP Status Code: " + ase.getStatusCode());
			logger.error("@dxc-error:"+"AWS Error Code:   " + ase.getErrorCode());
			logger.error("@dxc-error:"+"Error Type:       " + ase.getErrorType());
			logger.error("@dxc-error:"+"Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.error("@dxc-error:"+"Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with SQS, such as not "
					+ "being able to access the network.");
			logger.error("@dxc-error:"+"Error Message: " + ace.getMessage());
		}
	}

	int messageQueueSize() {
		List<String> attributeNames = new ArrayList<String>();
		attributeNames.add("ApproximateNumberOfMessages");
		GetQueueAttributesRequest request = new GetQueueAttributesRequest(myQueueUrl);
		request.setAttributeNames(attributeNames);
		Map<String, String> attributes = sqs.getQueueAttributes(request).getAttributes();
		int size = Integer.parseInt(attributes.get("ApproximateNumberOfMessages"));
		// int messagesNotVisible = Integer.parseInt(attributes
		// .get("ApproximateNumberOfMessagesNotVisible"));
		return size;

	}
	void displayMessage(Message message) {
		 logger.info("@dxc-info:"+"  Message");
         logger.info("@dxc-info:"+"    MessageId:     " + message.getMessageId());
         logger.info("@dxc-info:"+"    ReceiptHandle: " + message.getReceiptHandle());
         logger.info("@dxc-info:"+"    MD5OfBody:     " + message.getMD5OfBody());
         logger.info("@dxc-info:"+"    Body:          " + message.getBody());
         //messageBody=message.getBody();
         
         for (Entry<String, String> entry : message.getAttributes().entrySet()) {
             logger.info("@dxc-info:"+"  Attribute");
             logger.info("@dxc-info:"+"    Name:  " + entry.getKey());
             logger.info("@dxc-info:"+"    Value: " + entry.getValue());
         }
	}

}
