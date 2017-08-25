package org.apache.olingo.iot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;

public class IOTC4CTicket {
	
	EdmEntityContainer entityContainer;
	String name;
	String uri;
	
	public IOTC4CTicket(String name, String uri) {
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


	public boolean ticketCreation(String token) throws Exception {

		InputStream content = null;
		Edm edm;
		try {
			content = execute(this.uri,"application/atom+xml", "POST", "Basic YWRtaW5pc3RyYXRpb24wMTpXZWxjb21lMQ==", token);
			
			//parseData(new BufferedReader(new InputStreamReader(content)));
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	public static void parseData(BufferedReader in) throws Exception {
		StringBuffer response = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			response.append(line);
		}
		in.close();

		String xml = response.toString();
		System.out.println(xml);

	}

	
	
	public static InputStream execute(String relativeUri, String contentType,
			String httpMethod, String httpHeaderMap, String token) throws IOException, ODataException {
		
		//initialize the connection
		HttpClient client = new DefaultHttpClient();
		HttpPost post = initializeConnection(relativeUri, contentType, httpMethod,httpHeaderMap, token);
		
		//connect
		HttpResponse response = client.execute(post);

		String result = EntityUtils.toString(response.getEntity());
		
		System.out.print(result);
		
		InputStream content = null;
		

		//return the proper InputStream for the URI
		return content;
	}	
	
	public static HttpPost initializeConnection(String absolutUri,
			String contentType, String httpMethod, String httpHeaderMap, String token)
			throws MalformedURLException, IOException {
		
		HttpPost post = new HttpPost(absolutUri);
		
		post.setHeader("Authorization", httpHeaderMap);
		post.setHeader("Content-Type", contentType);
		post.setHeader("X-CSRF-Token", token);
		
		String xml = xmlCreate();
		HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
        post.setEntity(entity);
        
		return post;
	}
	
	public static String xmlCreate(){
		
		String ticket_create = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
				+ "<atom:entry xmlns:atom=\"http://www.w3.org/2005/Atom\"\rxmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"https://qxl-cust220.dev.sapbydesign.com/sap/byd/odata/v1/c4c.svc/\">\r"
				+ "<atom:category term=\"C4C.SVC.Ticket\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>\r"
				+ "<atom:content type=\"application/xml\"> \r\t"
				+ "<m:properties>\r\t\t"
				+ "<d:Description>demo_create_ticket_1</d:Description>\r\t"
				+ "</m:properties>\r"
				+ "</atom:content> \r"
				+ "</atom:entry>";
		
		return ticket_create;
	}
		
	
}
