/*
 * Copyright 2012 daviddale.
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
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

/**
 *
 * @author daviddale
 */
public class ServiceTests {

    //@Test
    public void test_picking_up_a_file() throws Exception {

        CamelContext context = new DefaultCamelContext();

        //ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        // Note we can explicit name the component
        //context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));        
        
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // get the order number from the name of the polled directory
                //MimetypesFileTypeMap
                from("file://test")
                .setHeader(Exchange.HTTP_METHOD, constant("POST") )
                .setHeader(Exchange.HTTP_QUERY, constant("item.filename=foo-bar&item.filetype=audio&item.identifier=123456&item.orderNumber=1798-asdf-fdfdsfdsf"))                        
                .to("http://localhost:9000/api/order/item")
                .setHeader(S3Constants.KEY, header("CamelFileNameOnly") )                        
                .to("aws-s3://flow7?accessKey=AKIAIFJQDYJQ462ABPQQ&secretKey=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&region=us-east-1");
                
            }
        });
        
        //ProducerTemplate template = context.createProducerTemplate();

        context.start();
        
        Thread.sleep(10000);
        
        context.stop();        

        //for (int i = 0; i < 10; i++) {
           // template.sendBody("test-jms:queue:test.queue", "Test Message: " + i);
        //}
    }
}
