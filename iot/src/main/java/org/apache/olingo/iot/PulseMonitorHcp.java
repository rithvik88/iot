package org.apache.olingo.iot;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class PulseMonitorHcp {

	public final String URLPARAM = "https://iotmmsXXXXXXXtrial.hanatrial.ondemand.com/com.sap.iotservices.mms/v1/api/http/app.svc/NEO_7KDJ8U73KGZU6QF0UFC9THAJG.T_IOT_4F918F92154E7EF2A427";
	
	public final String URLPARAM_COUNT = "https://iotmmsXXXXXXXtrial.hanatrial.ondemand.com:443/com.sap.iotservices.mms/v1/api/http/app.svc/count";
	
	public String absoluteUrl;
	public String countUrl;
	public String USER;
	public String PW;
	
	public String TIMESTAMP;
	public String SENSOR;
	public String VALUE;
	
	public String psmCount;
	
	public String token = null;
	
	public Map<String,String> sensorValues = new HashMap<String,String>();
	ArrayList<String> sensor = new ArrayList<String>();
	
	public PulseMonitorHcp() {
		
		USER = "XXXXXXX";
		PW = "XXXXXXX";
		absoluteUrl = URLPARAM;
		countUrl = URLPARAM_COUNT;
		
	}
	
	public void Count(){
		
		
		try {
				BufferedReader in = buildConn(countUrl);
				psmCount = parseValue(in);
				
				int index = readToFile(psmCount);
				
				if(index < Integer.parseInt(psmCount))
					writeToFile(psmCount);
				
				
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	}
	
	
	public String parseValue(BufferedReader in) throws Exception {
		
		String count;
		StringBuffer response = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			response.append(line);
		}

		String xml = response.toString();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		
		if (xml.contains("entry") == false) {
			throw new Exception();
		} else {		
			 
			NodeList nList = doc.getElementsByTagName("entry");

	        Node nNode1 = nList.item(1);
	        Element eElement1 = (Element) nNode1;
	        
	        NodeList iotNode = eElement1.getLastChild().getLastChild().getChildNodes();
	        
	        count = iotNode.item(2).getTextContent();
	        
	        System.out.println(iotNode.item(2).getTextContent());
	        
		} 
		
		return count;
	}

	public ArrayList<String> fetchData() {
		try {
			BufferedReader in = buildConn(absoluteUrl);
			parseData(in);
			return sensor;
		}  catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void parseData(BufferedReader in) throws Exception {
		StringBuffer response = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null) {
			response.append(line);
		}

		String xml = response.toString();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		//XPathFactory xpathFactory = XPathFactory.newInstance();
		//XPath xpath = xpathFactory.newXPath();
		if (xml.contains("entry") == false) {
			throw new Exception();
		} else {		
			 
			NodeList nList = doc.getElementsByTagName("entry");
			int l1 = nList.getLength()-1;

	        Node nNode1 = nList.item(l1);
	        Element eElement1 = (Element) nNode1;
	        
	        NodeList iotNode = eElement1.getLastChild().getLastChild().getChildNodes();
	        
	        for(int index=2; index<iotNode.getLength();index++)
	        {
	        	sensor.add(iotNode.item(index).getTextContent());
	        }
			
		} 		           
		
	}
	
	public BufferedReader buildConn(String Url) throws IOException {
		String code = "Basic " + new String(Base64.encodeBase64((USER + ":" + PW).getBytes()));
		URL absU = new URL(Url);
		HttpsURLConnection con = (HttpsURLConnection) absU.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", code);
		con.setRequestProperty("Accept", "application/xml");
		
		InputStream xml = con.getInputStream();
		
		return new BufferedReader(new InputStreamReader(xml));
	}
	
	
	public void writeToFile(String count){
		
		Properties prop = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream("config.properties");

			// set the properties value
			prop.setProperty("counter", count);

			// save properties to project root folder
			prop.store(output, null);
			
			ticketCreate();
			
			System.out.println("Properties File Write ->");

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int readToFile(String psmCount)
	{
		Properties prop = new Properties();
		InputStream input = null;
		
		int counter = 0;

		try {

			input = new FileInputStream("config.properties");
			System.out.println(input);
			// load a properties file
			prop.load(input);

			if(prop.getProperty("counter")==null)
				writeToFile(psmCount);
			else
				counter = Integer.parseInt(prop.getProperty("counter"));
			
			// get the property value and print it out
			System.out.println("Properties File Read ->"+prop.getProperty("counter"));
			
			

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return counter;
	}
	
	
	public void ticketCreate() throws IOException{
		
		CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
		CloudTicket cloud = new CloudTicket();
		List<String> token = cloud.getxcsrf();
		String content = cloud.startInstance(token);
		
		System.out.println("Ticket created"+content);
	}
	
}
