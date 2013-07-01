/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.flow7.dc.server.routes

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.aws.s3.*
/**
 *
 * @author daviddale
 */
class AmazonRoute extends RouteBuilder{
    String orderNumber;
    int workers;

    
    public AmazonRoute( String orderNumber ){
        super();
        this.orderNumber = orderNumber;
        this.workers = 1;
    }
    
    public AmazonRoute( String orderNumber, int workers ){
        super();            
        this.orderNumber = orderNumber;
        this.workers = workers;
    }
        
    public void configure() throws Exception{
        from("seda:${orderNumber}").routeId("${orderNumber}")  
        .noAutoStartup()
        .threads( workers )
        .to("aws-s3://${orderNumber}?amazonS3Client=#client");
    }	
}

