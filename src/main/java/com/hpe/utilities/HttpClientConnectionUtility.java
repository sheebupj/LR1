package com.hpe.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
//import java.rmi.registry.Registry;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.ResourceBundle;

import javax.net.ssl.SSLContext;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
//import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.hpe.data.MessageBody;
import com.hpe.lr1.ReadMessageAndUpdateOnsourceDb;

public class HttpClientConnectionUtility {
	final static Logger logger = Logger.getLogger(HttpClientConnectionUtility.class);
	public static String certUrl = "https://api.certcapture.com/v2/certificates/";
	public static String custUrl = "https://api.certcapture.com/v2/customers/";
	static String proxy;
	static String porxyPort;
	static int port;
	static String certUserName;
	static String certPassWord;
	static {
		ResourceBundle rb = utility.getResourceBundle();
		proxy = rb.getString("proxy");
		porxyPort = rb.getString("proxyPort");
		port = Integer.valueOf(porxyPort);
		certUserName=rb.getString("certCaptureUserName");
		certPassWord=rb.getString("certCapturePassWord");

	}

	public static CloseableHttpClient getHttpSSLClient() {
		InputStream inputStream = new InputStream() {

			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return 0;
			}
		};

		try {
			// new TrustSelfSignedStrategy()

			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

			// char[] pwrd=
			ks.load(inputStream, "changeit".toCharArray());

			SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(ks, "password".toCharArray())
					.loadTrustMaterial(null, new TrustAllStrategy()).build();
			SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory())
					.build();

			BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(registry);
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager)
					.setSSLSocketFactory(sslConnectionFactory)
					.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();

			return client;
		} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException
				| IOException | UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:" + "Exception happend while creating httpclient");
			e.printStackTrace();
		}

		return null;
	}

	public static CloseableHttpClient getHttpSSLClient1() {

		try {
			// new TrustSelfSignedStrategy()
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustAllStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

			return setProxyConfig(HttpClients.custom().setSSLSocketFactory(sslsf).build());
		} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:" + "Exception happend while creating httpclient");
		}

		return null;
	}

	private static CloseableHttpClient setProxyConfig(CloseableHttpClient client) {


		HttpHost proxyHost = new HttpHost(proxy, port);
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxyHost);
		client = HttpClients.custom().setRoutePlanner(routePlanner).build();
		return client;
	}

	public static CloseableHttpClient getHttpClient() {
		HttpHost proxyHost = new HttpHost(proxy, port, "http");
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxyHost);
		CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
		return httpclient;
	}

	public static String getEncodedCredentailsForCertCapture() {
		String encodedData = null;

		try {
			encodedData = DatatypeConverter
					.printBase64Binary((certUserName + ":" + certPassWord).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:" + "Exception happend while encoding credential");
		}
		return encodedData;
	}

	public static HttpGet prepareAndGetHttpGet(String url, String clientId,
			ReadMessageAndUpdateOnsourceDb readMessageAndUpdateOnsourceDb) {
		try {
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("accept", "application/json");
			httpget.addHeader("x-client-id", clientId);
			String authHeader = "Basic " + getEncodedCredentailsForCertCapture();
			httpget.setHeader(HttpHeaders.AUTHORIZATION, authHeader);// X-Forwarded-Host

			return httpget;
		} catch (Exception e) {
			readMessageAndUpdateOnsourceDb.flag = false;
			logger.error("@dxc-error:" + "enexcpeted error occured while preparing the http request" + url);
			return null;

		}
	}

	public static ObjectMapper getObjectMapperFromResponse() {
		ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	public static void closeHttpClient(CloseableHttpClient httpclient) {
		try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("@dxc-error:" + "Exception happend while closing httpclient");
		}

	}

	public static void addCertCaptureApiUrl(List<MessageBody> messageBodies) {
		for (MessageBody messageBody : messageBodies) {

			if (messageBody.getCertFlag()) {
				String certId = messageBody.getCertificate().get("id");
				messageBody.setCertUrl(certUrl + certId);
				logger.info("@dxc-info:certificate url" + messageBody.getCertUrl());
			} else {
				String custId = messageBody.getCustomer().get("id");
				messageBody.setCustUrl(custUrl + custId);
				logger.info("@dxc-info:customer url " + messageBody.getCustUrl());
			}

		}
	}

	public static void addCustCertCaptureApiUrl(List<MessageBody> messageBodies) {
		for (MessageBody messageBody : messageBodies) {

			if (messageBody.getCertFlag()) {

				messageBody.setCertCustUrl(messageBody.getCertUrl() + "/customers");
				logger.info("@dxc-info:customer url from certificate " + messageBody.getCertCustUrl());
			}

		}
	}
}
