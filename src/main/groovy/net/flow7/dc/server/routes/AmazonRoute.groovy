/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.flow7.dc.server.routes

import net.flow7.dc.server.ext.SystemScanner
import org.apache.camel.Exchange
import org.apache.camel.LoggingLevel
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.dataformat.tika.TikaDataFormat
import org.apache.camel.spi.DataFormat

/**
 *
 * @author daviddale
 */
class AmazonRoute extends RouteBuilder{

    DataFormat tika;

    String orderNumber;
    int workers = 5;

    public AmazonRoute( String orderNumber, int workers ){

        super();            
        this.orderNumber = orderNumber;
        this.workers = workers;
        tika = new TikaDataFormat();

    }
        
    public void configure() throws Exception{
        
        errorHandler( deadLetterChannel( "log:errorLog?level=ERROR" ).useOriginalMessage()
                .maximumRedeliveries(3).maximumRedeliveryDelay(5000)
                .logStackTrace(true).retryAttemptedLogLevel( LoggingLevel.INFO )  )

        from(  "seda:${orderNumber}"  ).routeId( orderNumber )
        .noAutoStartup()
        .threads( workers )
        .to( "bean:bucket" ).process(new Processor(){
            @Override
            void process(Exchange exchange) throws Exception {
                SystemScanner.get().fileTransfersComplete++
            }
        })
        .to( "seda:${orderNumber}-out" )

        from(  "seda:${orderNumber}-out"  ).routeId( "${orderNumber}-index" )
        .unmarshal( tika )
        .to( "bean:index" )

    }	
}

