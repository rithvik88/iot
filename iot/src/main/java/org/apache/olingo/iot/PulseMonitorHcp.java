package org.apache.olingo.iot;

import java.awt.List;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;

public class PulseMonitorHcp {

	public final String URLPARAM = "https://iotmmsi309741trial.hanatrial.ondemand.com/com.sap.iotservices.mms/v1/api/http/app.svc/NEO_7KDJ8U73KGZU6QF0UFC9THAJG.T_IOT_4F918F92154E7EF2A427";
	
	public final String URLPARAM_COUNT = "https://iotmmsi309741trial.hanatrial.ondemand.com:443/com.sap.iotservices.mms/v1/api/http/app.svc/count";
	
	public String absoluteUrl;
	public String USER;
	public String PW;
	
	public String TIMESTAMP;
	public String SENSOR;
	public String VALUE;
	
	public Map<String,String> sensorValues = new HashMap<String,String>();
	ArrayList<String> sensor = new ArrayList<String>();
	
	public PulseMonitorHcp() {
		
		USER = "i309741";
		PW = "welcome!123";
		absoluteUrl = URLPARAM;
		
	}
	
	public ArrayList<String> fetchData() {
		try {
			BufferedReader in = buildConn();
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
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		if (xml.contains("entry") == false) {
			throw new Exception();
		} else {
			getInfo(doc, xpath, "/feed/entry/content/properties/C_TIMESTAMP");
			getInfo(doc, xpath, "/feed/entry/content/properties/C_SENSOR");
			getInfo(doc, xpath, "/feed/entry/content/properties/C_VALUE");
		}
		
	}
	
	public String getInfo(Document doc, XPath xpath, String path) throws XPathExpressionException
	 {
		XPathExpression exp = xpath.compile(path);
		String value = (String) exp.evaluate(doc, XPathConstants.STRING);
		System.out.println(value);
		sensor.add(value);
		
		return value;
	}

	public BufferedReader buildConn() throws IOException {
		String code = "Basic " + new String(Base64.encodeBase64((USER + ":" + PW).getBytes()));
		URL absU = new URL(absoluteUrl);
		HttpsURLConnection con = (HttpsURLConnection) absU.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", code);
		con.setRequestProperty("Accept", "application/xml");
		
		InputStream xml = con.getInputStream();
		
		return new BufferedReader(new InputStreamReader(xml));
	}
	
}
