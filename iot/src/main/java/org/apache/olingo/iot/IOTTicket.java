package org.apache.olingo.iot;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

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
			token = execute(this.uri,"application/xml", "GET", "Basic YWRtaW5pc3RyYXRpb24wMTpXZWxjb21lMQ==");
			
			//parseData(new BufferedReader(new InputStreamReader(content)));
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
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

	
	
	public static String execute(String relativeUri, String contentType,
			String httpMethod, String httpHeaderMap) throws IOException, ODataException {
		
		//initialize the connection
		HttpURLConnection connection = initializeConnection(relativeUri, contentType, httpMethod,httpHeaderMap);
		
		//connect
		connection.connect();

		String csrf = null;
		csrf = extractResponseFromURLConnection(connection);

		//return the proper InputStream for the URI
		return csrf;
	}	
	
	public static HttpURLConnection initializeConnection(String absolutUri,
			String contentType, String httpMethod, String httpHeaderMap)
			throws MalformedURLException, IOException {
		
		URL url = new URL(absolutUri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod(httpMethod);
		connection.setRequestProperty("Authorization", httpHeaderMap);
		connection.setRequestProperty("Content-Type", contentType);
		connection.setRequestProperty("X-CSRF-Token", "Fetch");

		return connection;
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
