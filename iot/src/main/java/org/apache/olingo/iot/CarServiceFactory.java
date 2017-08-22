/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package org.apache.olingo.iot;

import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;

public class CarServiceFactory extends ODataServiceFactory {

  @Override
  public ODataService createService(final ODataContext ctx) throws ODataException {

   // EdmProvider edmProvider = new CarEdmProvider();

    //ODataSingleProcessor singleProcessor = new CarODataSingleProcessor();
    
    PulseMonitorHcp pulseMonitor = new PulseMonitorHcp();
    pulseMonitor.fetchData();
    
    IOTTicket newTicket = new IOTTicket("test","https://qxl-cust220.dev.sapbydesign.com/sap/byd/odata/v1/c4c.svc/TicketCollection");
    try {
		newTicket.loadMetadata();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    return null;
    //return createODataSingleProcessorService(edmProvider, singleProcessor);
  }
}
