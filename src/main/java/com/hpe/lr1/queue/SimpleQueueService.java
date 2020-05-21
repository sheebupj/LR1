package com.hpe.lr1.queue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.http.client.utils.HttpClientUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.hpe.data.MessageBody;
import com.hpe.utilities.HttpClientConnectionUtility;
import com.hpe.utilities.utility;

/**
 * This sample demonstrates how to make basic requests to Amazon SQS using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web
 * Services developer account, and be signed up to use Amazon SQS. For more
 * information on Amazon SQS, see http://aws.amazon.com/sqs.
 * <p>
 * Fill in your AWS access credentials in the provided credentials file
 * template, and be sure to move the file to the default location
 * (~/.aws/credentials) where the sample code will load the credentials from.
 * <p>
 * <b>WARNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 */
public class SimpleQueueService {

    public static void main(String[] args) throws Exception {

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        ResourceBundle rb= utility.getResourceBundle();
        
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProxyHost(rb.getString("proxy"));
        clientConfiguration.setProxyPort(Integer.valueOf(rb.getString("proxyPort"))); 

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                               .withCredentials(credentialsProvider)
                               .withRegion(Regions.US_EAST_1)
                               .withClientConfiguration(clientConfiguration).build();

        System.out.println("===========================================");
        System.out.println("Started to read from certcapture aws queue");
        System.out.println("===========================================\n");

        try {
            // Create a queue
           // System.out.println("Creating a new SQS queue called MyQueue.\n");
            GetQueueUrlResult getQueueUrlResult = sqs.getQueueUrl("sqs-company-prod-HPE");
            String myQueueUrl = getQueueUrlResult.getQueueUrl();
            
            // List queues
          //  System.out.println("Listing all queues in your account.\n");
//            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
//                System.out.println("  QueueUrl: " + queueUrl);
//            }
           // System.out.println();

            // Send a message
           // System.out.println("Sending a message to MyQueue.\n");
           // sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is my message text."));

            // Receive messages
            System.out.println("Receiving messages from MyQueue.\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            System.out.println("Received "+ messages.size()+" Messages");
            String messageBody=null;
            for (Message message : messages) {
                System.out.println("  Message");
                System.out.println("    MessageId:     " + message.getMessageId());
                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("    Body:          " + message.getBody());
                messageBody=message.getBody();
                
                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                    System.out.println("  Attribute");
                    System.out.println("    Name:  " + entry.getKey());
                    System.out.println("    Value: " + entry.getValue());
                }
            }
            
            ObjectMapper mapper=HttpClientConnectionUtility.getObjectMapperFromResponse();
           // MessageBody 
            MessageBody MessageBody= mapper.readValue(messageBody, MessageBody.class);
            
           // 
            
            System.out.println(MessageBody);

            // Delete a message
           // System.out.println("Deleting a message.\n");
         //  String messageReceiptHandle = messages.get(0).getReceiptHandle();
           // sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));
            
            // Delete a queue
           // System.out.println("Deleting the test queue.\n");
            //sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
