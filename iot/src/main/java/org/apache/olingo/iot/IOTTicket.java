package org.apache.olingo.iot;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.w3c.dom.Document;

public class IOTTicket {

	EdmEntityContainer entityContainer;
	String name;
	String uri;
	
	public IOTTicket(String name, String uri) {
		this.name = name;
		this.uri = uri;
	}

	public String getName() {
		return this.name;
	}

	public String getUri() {
		return this.uri;
	}
	

	public EdmEntityContainer getEntityContainer() {
		return this.entityContainer;
	}


	public String loadMetadata() throws Exception {

		String token = null;
		Edm edm;
		try {
			token = execute(this.uri,"text/xml", "GET", "Basic XXXXXXX");
			
			//parseData(new BufferedReader(new InputStreamReader(content)));
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return token;
	}
	
	
	public static void parseData(BufferedReader in) throws Exception {
		StringBuffer response = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			response.append(line);
		}

		String xml = response.toString();
		System.out.println(xml);

	}

	
	
	@SuppressWarnings("deprecation")
	public static String execute(String relativeUri, String contentType,
			String httpMethod, String httpHeaderMap) throws Throwable {
		
		CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
		
		//initialize the connection
		HttpsURLConnection connection = initializeConnection(relativeUri, contentType, httpMethod,httpHeaderMap);
				
		
		 String xsrfToken = null;
		    try{

		        InputStream stream = connection.getInputStream();
		        IOUtils.toString(stream);
		        xsrfToken = connection.getHeaderFields().get("X-CSRF-Token").get(0);
		    } catch(Throwable t){
		        InputStream errorDetails = connection.getErrorStream();
		        if(errorDetails != null)
		            System.out.println("Server error response is: {}"+ IOUtils.toString(errorDetails));
		        throw t;
		    } 
				
		System.out.println(xsrfToken);

		//return the proper InputStream for the URI
		return xsrfToken;
	}	
	
	public static HttpsURLConnection initializeConnection(String absolutUri,
			String contentType, String httpMethod, String httpHeaderMap)
			throws MalformedURLException, IOException {
		
		URL url = new URL(absolutUri);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

		connection.setRequestMethod(httpMethod);
		connection.setRequestProperty("Authorization", httpHeaderMap);
		connection.setRequestProperty("Content-Type", contentType);
		connection.setRequestProperty("X-CSRF-Token", "Fetch");

		return connection;

		
		/*
		HttpGet get = new HttpGet(absolutUri);
		
		get.setHeader("Authorization", httpHeaderMap);
		get.setHeader("Content-Type", contentType);
		get.setHeader("X-CSRF-Token", "Fetch");
		
		return get;*/
		
	}
	
	public static String extractResponseFromURLConnection( HttpURLConnection connection ) 
			 throws IOException, ODataApplicationException {
		 
		InputStream responseContent = null;
		try {
			
			int HTTPResponseCode = connection.getResponseCode();
			if( (HTTPResponseCode >= 200) && (HTTPResponseCode < 300)){			
				responseContent = connection.getInputStream();
			}else{
				if(responseContent!=null) { 
					responseContent.close(); 
				}
				responseContent = null;  
				
				//check if Status Code 403; if so send "Forbidden" to core application
				if(HTTPResponseCode==403){
					String errorMessage= "Forbidden";
					throw new ODataApplicationException(errorMessage, Locale.ENGLISH);	
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}		 
		return connection.getHeaderField("x-csrf-token");
	
	}	
	
}
