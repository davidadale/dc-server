/*
 * Copyright 2013 daviddale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.flow7.dc.server;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 *
 * @author daviddale
 */
public class CamelTests extends CamelTestSupport { 
    
    private String inboxDir;
    private String outboxDir;
    
    @EndpointInject(uri="{{orders.inbox}}")
    private ProducerTemplate orders;
    
    public void setUp() throws Exception{
        super.setUp();
        inboxDir = context.resolvePropertyPlaceholders("target/inbox");
        outboxDir = context.resolvePropertyPlaceholders("target/outbox");
        deleteDirectory(inboxDir);
        deleteDirectory(outboxDir);
    }
    
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SimpleRoute();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext ctx =  super.createCamelContext();
        PropertiesComponent prop = ctx.getComponent("properties", PropertiesComponent.class );
        prop.setLocation("classpath:net/flow7/dc/server/test-config.properties");
        return ctx;
    }
    
    
    
    
    //@Test
    public void test_slow_route() throws Exception{
        
        /*MockEndpoint output = getMockEndpoint("mock:target/outbox");
        output.expectedMessageCount( 3 );
        output.setResultWaitTime(1000);
        
        for(int i=0;i<10;i++){
            orders.sendBodyAndHeader("Hello World " + i , Exchange.FILE_NAME, "hello"+i+".txt");
        }
        
        output.assertIsSatisfied();*/
    
        
        MockEndpoint output = getMockEndpoint("mock:target/outbox");
        output.expectedMinimumMessageCount( 900 );
        output.setResultWaitTime(1000); 
        
        for(int i=0;i<1000;i++){
            orders.sendBodyAndHeader("Hello World " + i , Exchange.FILE_NAME, "hello"+i+".txt");
        } 
        output.assertIsSatisfied();
        
    }
    
    
    
    
    
}
