package org.apache.olingo.iot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Scheduler {

	Scheduler() {
		ScheduledThreadPoolExecutor poolExecutor = null;  
		
		if(poolExecutor==null){
			createTimer();  
		}
		  
			}
	
	private static void createTimer() {
		ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(2);
		long delayForScheduler = calculateDelayForMonitoringUpdate();
		try {
			poolExecutor.scheduleAtFixedRate(new Runnable(){
		    	public void run(){
			    	try {
			    			/*PulseMonitorHcp pulseMonitor = new PulseMonitorHcp();
			    			ArrayList<String> sensorValue = pulseMonitor.fetchData();
			    			
			    			System.out.println(sensorValue.get(0));
			    			System.out.println(sensorValue.get(1));
			    			System.out.println(sensorValue.get(2));*/
			    		
			    			PulseMonitorHcp pulseMonitor = new PulseMonitorHcp();
			    			pulseMonitor.Count();
			    			
					} catch (Throwable e) {
						//logger.error("Tenant Update Service could not run.");
					}
		    	}
		    },delayForScheduler, 5 ,TimeUnit.SECONDS);
		} catch (Throwable e) {
			//logger.error("Tenant Update Service could not run.");
		}     
	}  
	
	private static long calculateDelayForMonitoringUpdate(){
		

		TimeZone timeZoneForCalculation = TimeZone.getTimeZone("Europe/Berlin");
		
		
		//current date
		Calendar dateNow = GregorianCalendar.getInstance();
		dateNow.setTimeZone(timeZoneForCalculation);
		
		
		//date for user input
		Calendar startDateforMonitoringUpdates =GregorianCalendar.getInstance();
		startDateforMonitoringUpdates.setTimeZone(timeZoneForCalculation);
		
		
		
		//calculate difference again, because it might have been changed in above steps
		long diff = startDateforMonitoringUpdates.getTimeInMillis()- dateNow.getTimeInMillis();
				
		//return time in minutes
		return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
	}

}
