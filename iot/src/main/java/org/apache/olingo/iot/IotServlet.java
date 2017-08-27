package org.apache.olingo.iot;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IotServlet
 */
public class IotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String token = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IotServlet() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("<p><h2>Pulse Monitor Value from Sensor</h2></p>");
		
		Scheduler scheduler = new Scheduler();
		/*PulseMonitorHcp pulseMonitor = new PulseMonitorHcp();
		ArrayList<String> sensorValue = pulseMonitor.fetchData();
		
		response.getWriter().println("<p>Time : "+sensorValue.get(0)+"</p>");
		response.getWriter().println("<p>Sensor : "+sensorValue.get(1)+"</p>");
		response.getWriter().println("<p>Value : "+sensorValue.get(2)+"</p>");*/
		
		/*
		CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
		CloudTicket cloud = new CloudTicket();
		List<String> token = cloud.getxcsrf();
		String content = cloud.startInstance(token);*/
		
		/*
		PulseMonitorHcp pulseMonitor = new PulseMonitorHcp();
		pulseMonitor.ticketCreate();*/
		
		response.getWriter().println("<p>Scheduler</p>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
