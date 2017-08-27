package org.apache.olingo.iot;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;


public class ServletContext implements ServletContextListener{

	static Logger logger = LoggerFactory.getLogger(ServletContext.class);
	private static String URL;
	public static SSLSocketFactory sslSocketFactory;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		// Setup SSL
		// Read destination configuration and use given certificates to avoid
		// sun.security.validator.ValidatorException see
		// https://jtrack.wdf.sap.corp/browse/NGPBUG-24008
		// Implementation according to
		// https://help.hana.ondemand.com/help/frameset.htm?4da3b13c88ce4220bbd56a4361799668.html
		Context ctx;
		ConnectivityConfiguration configuration = null;
		try {
			ctx = new InitialContext();
			configuration = (ConnectivityConfiguration) ctx
					.lookup("java:comp/env/connectivityConfiguration");
		} catch (NamingException e) {
			logger.error("Error while reading destination configuration");
		}
		
		if(configuration == null){
			logger.error("Connectivity Configuration is null");
			return;
		}

		DestinationConfiguration iotWsDestinationConfig = configuration
				.getConfiguration("IOTOutbound");
		KeyStore truststore = iotWsDestinationConfig.getTrustStore();
		setURL(iotWsDestinationConfig.getProperty("URL"));
		TrustManagerFactory trustManagerFactory = null;
		try {
			trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(truststore);
		} catch (NoSuchAlgorithmException e){}
		catch(KeyStoreException e) {
			logger.error("Error during ssl setup (trustManager)");
			return;
		}

		SSLContext sslcontext = null;
		//SSLSocketFactory sslSocketFactory = null;
		try {
			sslcontext = SSLContext.getInstance("TLSv1.2");
			sslcontext.init(null, trustManagerFactory.getTrustManagers(), null);
			sslSocketFactory = sslcontext.getSocketFactory();
		} catch (NoSuchAlgorithmException e){}
		catch(KeyManagementException e) {
			logger.error("Error during ssl setup");
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
		setSslSocketFactory(sslSocketFactory);
	}
	
	public static String getURL() {
		return URL;
	}
	
	public static void setURL(String uRL) {
		URL = uRL;
	}
	
	public static SSLSocketFactory getSslSocketFactory() {
		return sslSocketFactory;
	}

	public static void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
		ServletContext.sslSocketFactory = sslSocketFactory;
	}
	

}
