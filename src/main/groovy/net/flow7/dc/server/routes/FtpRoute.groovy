package net.flow7.dc.server.routes

import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder

/**
 * Created with IntelliJ IDEA.
 * User: daviddale
 * Date: 12/28/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
class FtpRoute extends RouteBuilder{

    String orderNumber
    String location

    public FtpRoute(String orderNumber, String location){
        this.orderNumber = orderNumber
        this.location = location
    }

    public void configure() throws Exception{
        errorHandler( deadLetterChannel( "log:errorLog?level=ERROR" ).useOriginalMessage()
                .maximumRedeliveries(3).maximumRedeliveryDelay(5000)
                .logStackTrace(true).retryAttemptedLogLevel( LoggingLevel.INFO )  )


        from(  "seda:${orderNumber}"  ).routeId(  "${orderNumber}-ftp"   )
                .noAutoStartup()
                .to("${location}&binary=true")
    }

    public String getRouteName(){
        return "${orderNumber}-ftp"
    }
}
