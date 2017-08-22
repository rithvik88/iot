package org.apache.olingo.iot;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

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

	public final static String URLPARAM = "https://iotmmsi309741trial.hanatrial.ondemand.com/com.sap.iotservices.mms/v1/api/http/app.svc/NEO_7KDJ8U73KGZU6QF0UFC9THAJG.T_IOT_4F918F92154E7EF2A427";
	
	public final static String URLPARAM_COUNT = "https://iotmmsi309741trial.hanatrial.ondemand.com:443/com.sap.iotservices.mms/v1/api/http/app.svc/count";
	
	public static String absoluteUrl;
	public static String USER;
	public static String PW;
	
	public static String TIMESTAMP;
	public static String SENSOR;
	public static String VALUE;
	
	public PulseMonitorHcp() {
		
		USER = "i309741";
		PW = "welcome!123";
		absoluteUrl = URLPARAM;
		
	}
	
	public static void fetchData() {
		try {
			BufferedReader in = buildConn();
			parseData(in);
		}  catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void parseData(BufferedReader in) throws Exception {
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
			getInfo(doc, xpath, "/feed/entry/content/properties/C_TIMESTAMP", TIMESTAMP);
			getInfo(doc, xpath, "/feed/entry/content/properties/C_SENSOR", SENSOR);
			getInfo(doc, xpath, "/feed/entry/content/properties/C_VALUE", VALUE);
		}

	}
	
	public static void getInfo(Document doc, XPath xpath, String path, String key) throws XPathExpressionException
	 {
		XPathExpression exp = xpath.compile(path);
		String value = (String) exp.evaluate(doc, XPathConstants.STRING);
		System.out.println(value);
	}

	public static BufferedReader buildConn() throws IOException {
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
