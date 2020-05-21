package com.hpe.utilities;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class QueueReadUtilty {
	final static Logger logger = Logger.getLogger(QueueReadUtilty.class);
	public static ProfileCredentialsProvider getProfileCredentialsProviderr() {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at (~/.aws/credentials).
		 */
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {

			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}
		return credentialsProvider;
	}

	public static AWSCredentials getProfileCredentialsProvider() {
		String awsAccessKeyId = " ";
		String awsSecretAccessKey = " ";

		final AWSCredentials reqCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
		return reqCredentials;

	}

	public static AmazonSQS getAmazonSQS(ProfileCredentialsProvider credentialsProvider) {
		
		ResourceBundle rb = utility.getResourceBundle();
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setProxyHost(rb.getString("proxy"));
		clientConfiguration.setProxyPort(Integer.valueOf(rb.getString("proxyPort")));

		AmazonSQS sqs = AmazonSQSClientBuilder.standard().withCredentials(credentialsProvider).withRegion(rb.getString("region"))
				.withClientConfiguration(clientConfiguration).build();

		return sqs;
	}


}
