package org.apache.olingo.iot;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.cxf.helpers.IOUtils;

public class CloudTicket {
	
	public CloudTicket() {
		
	}
	
	public List<String> getxcsrf() throws IOException{
		
			List<String> token = null;
		    URL requestURL = new URL(ServletContext.getURL());
		    HttpsURLConnection urlConnection = (HttpsURLConnection) requestURL.openConnection();
		    //urlConnection.setSSLSocketFactory(ServletContext.sslSocketFactory);
		    urlConnection.addRequestProperty("X-CSRF-Token", "Fetch");
		    urlConnection.addRequestProperty("Authorization", "Basic XXXXXXXX");
		    
		    urlConnection.setRequestMethod("GET");

		    String xsrfToken = null;
		    try{

		    	int responseCode = urlConnection.getResponseCode();
		    	
		    	for (Entry<String, List<String>> header : urlConnection.getHeaderFields().entrySet()) {
		    		
		    		if(header.getKey() == null)
		    			System.out.print("Null");
		    		else if(header.getKey().equals("x-csrf-token"))
		    			token = header.getValue();
		    		
		    	    System.out.println(header.getKey() + "=" + header.getValue());
		    	}
		    	
		    	
		        //InputStream stream = urlConnection.getInputStream();
		        //IOUtils.toString(stream);
		        //xsrfToken = urlConnection.getHeaderFields().get("X-CSRF-Token").get(0);
		    } catch(Exception e){
		    	e.printStackTrace();
		        InputStream errorDetails = urlConnection.getErrorStream();
		        if(errorDetails != null)
		            System.out.println("Server error response is: {}"+ IOUtils.toString(errorDetails));
		        
		    } 
		    return token;
		}
	
	
	public String startInstance(List<String> csrf) throws IOException {

		   URL requestURL= new URL("https://myXXXXXXX.crm.ondemand.com/sap/byd/odata/v1/c4c.svc/TaskCollection");
		   
		   HttpsURLConnection urlConnection = (HttpsURLConnection) requestURL.openConnection();
		   //urlConnection.setSSLSocketFactory(ServletContext.sslSocketFactory);
		   urlConnection.addRequestProperty("Authorization", "Basic XXXXXXXX");
		   
		   urlConnection.addRequestProperty("X-CSRF-Token", csrf.get(0));
		   urlConnection.addRequestProperty("Content-Type", "application/atom+xml");  //application/atom+xml	//text/xml
		   urlConnection.setRequestMethod("POST");

		   String content = null;
		   try{

		      urlConnection.setDoOutput(true);
		      //String xml = xmlCreate();
		      //HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
		      
		      DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
		      wr.writeBytes("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r"
		      		+ "<atom:entry xmlns:atom=\"http://www.w3.org/2005/Atom\"\rxmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"https://myXXXXXXX.crm.ondemand.com/sap/byd/odata/v1/c4c.svc/\">\r"
		      		+ "<atom:category term=\"C4C.SVC.Ticket\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>\r"
		      		+ "<atom:content type=\"application/xml\"> \r\t"
		      		+ "<m:properties>\r\t\t"
		      		+ "<d:Priority>1</d:Priority>\r\t\t"
		      		+ "<d:Status>1</d:Status>\r\t\t"
		      		+ "<d:Subject>Pulse_Monitor_Ticket</d:Subject>\r\t\t"
		      		+ "<d:Processor>Rithvik Santosh</d:Processor>\r\t\t"
		      		+ "<d:PriorityText>High</d:PriorityText>\r\t\t"
		      		+ "<d:StatusText>Open</d:StatusText>\r\t\t"
		      		+ "<d:TypeCodeText>Activity Task</d:TypeCodeText>\r\t"
		      		+ "<d:AccountID>1000852</d:AccountID>\r\t"
		      		+ "<d:Account>Adidas</d:Account>\r\t"
		      		+ "</m:properties>\r</atom:content> \r"
		      		+ "</atom:entry>");
		     wr.flush();
		     wr.close();

		     InputStream stream = urlConnection.getInputStream();
		     content = IOUtils.toString(stream);

		  } catch(Exception e){
		    InputStream errorDetails = urlConnection.getErrorStream();
		    System.out.println("Server error response is: {}"+ IOUtils.toString(errorDetails));
		  } 
		   
		  return content;

		}


}
