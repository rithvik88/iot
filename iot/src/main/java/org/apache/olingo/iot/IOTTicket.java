package org.apache.olingo.iot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;

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


	public boolean loadMetadata() throws ODataException {

		InputStream content = null;
		Edm edm;
		try {
			content = execute(this.uri + "/"+"$metadata","application/xml", "GET", "Basic YWRtaW5pc3RyYXRpb24wMTpXZWxjb21lMQ==");
			
			edm = EntityProvider.readMetadata(content, false);
			this.entityContainer = edm.getDefaultEntityContainer();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static InputStream execute(String relativeUri, String contentType,
			String httpMethod, String httpHeaderMap) throws IOException, ODataException {
		
		//initialize the connection
		HttpURLConnection connection = initializeConnection(relativeUri, contentType, httpMethod,httpHeaderMap);
		
		//connect
		connection.connect();

		InputStream content = null;
		content = extractResponseFromURLConnection(connection);

		//return the proper InputStream for the URI
		return content;
	}	
	
	public static HttpURLConnection initializeConnection(String absolutUri,
			String contentType, String httpMethod, String httpHeaderMap)
			throws MalformedURLException, IOException {
		
		URL url = new URL(absolutUri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod(httpMethod);
		connection.setRequestProperty("Authorization", httpHeaderMap);
		connection.setRequestProperty("Content-Type", contentType);

		return connection;
	}
	
	public static InputStream extractResponseFromURLConnection( HttpURLConnection connection ) 
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
		return responseContent;
	
	}	
	

}
