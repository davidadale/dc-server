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

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.dataformat.tika.TikaDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.jndi.JndiContext;
import org.apache.camel.impl.SimpleRegistry
import org.apache.camel.builder.RouteBuilder
import net.flow7.dc.server.routes.*
/**
 *
 * @author daviddale
 */
public class Routes {

    
    public static RouteBuilder toAmazon( String orderNumber ){
        int workers = Config.get().getInt( "workers", 1 );
        return new AmazonRoute( orderNumber, workers );
    }
    
    /*Config config;
    DataFormat tika;
    JndiContext context;
    Boolean slowPathRunning = false;
    Boolean fastPathRunning = false;
    PropertiesComponent pc = null;*/
    
    public Routes() throws IOException {

        SimpleRegistry<String,Object> reg = new SimpleRegistry<String,Object>();
        //reg.put("client", )
        
  /*      config = Config.get();
        config.printConfig();
        
        
        pc = new PropertiesComponent();
         
        //PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class );
        List<String> locations = new LinkedList<String>();

        
        
        File homeDirectory = new File(config.getHomeDir(), "dc-server.config");
        if( homeDirectory.exists() ){
System.out.println ("Home Directory found " + homeDirectory.getCanonicalPath() );            
            locations.add("file:" + homeDirectory.getCanonicalPath() );
        }
        
        File currentDirectory = new File("./dc-server.config");
        if (currentDirectory.exists()) {
System.out.println("Current Directory found " + currentDirectory.getCanonicalPath() );            
            locations.add(currentDirectory.getCanonicalPath());
        }

        pc.setLocations( locations.toArray(new String[0]) );
        
        tika = new TikaDataFormat();

        try {

            context = new JndiContext();
            context.bind("website", new IndexWriter());

        } catch (Exception e) {
            log.error(String.format(" couldn't register IndexWriter. err: %s", e.getMessage()));
            System.exit(1);
        }*/

    }
    
   

    public void configure() throws Exception {
        
        /*getContext().addComponent("properties", pc);
        int slowRate = config.getNumber("slowPathRate");
        int slowInterval = config.getNumber("slowPathInterval");*/

        /*from("{{orders.inbox}}").routeId("slow")
                .noAutoStartup()
                .unmarshal(tika)
                .wireTap("{{website.uri}}")
                .throttle(slowRate)
                .timePeriodMillis(slowInterval)
                .to("{{orders.outbox}}");*/

        /*from("{{orders.inbox}}").routeId("fast")
                .unmarshal(tika)
                .wireTap("{{website.uri}}")
                .to("{{orders.outbox}}");        */
        
        
        //from("{{order.inbox}}").to("{{order.outbox}}");
        
        
        
        //from( config.prop("copyLocation") );
        /*.process(new Processor() {
         public void process(Exchange ex) throws Exception {
         GenericFile load = (GenericFile) ex.getIn().getBody();
         File file = (File) load.getFile();
         System.out.println("((((((" + file.getPath() + "))))))))");
         String[] args = new String[]{"-silentPrint", file.getAbsolutePath() };
         PrintPDF.main( args );
         }
         });*/
        //.to("lpr://localhost/default");
        //System.out.println("Orders directory is set to " + config.prop("copyLocation"));

        //String ordersQueue = String.format("%s?%s", config.prop("copyLocation"), "recursive=true&excludedNamePostfix=_");
        //System.out.println("Order queue has been set to " + ordersQueue);
        //String amazonS3 = String.format("", "");


        // Quartz route definitions
        //String slowPathSchedule = String.format( "quartz://dcGroup/slowPathTrigger?cron=%s", config.prop("slowPathSchedule").replace( " ", "+" ) );
        //String fastPathSchedule = String.format( "quartz://dcGroup/fastPathTrigger?cron=%s", config.prop("fastPathSchedule").replace( " ", "+" )  );


        //System.out.println( " Orders queue " + ordersQueue );
        //System.out.println( " Slow schedule " + slowPathSchedule );
        //System.out.println( " Fast schedule " + fastPathSchedule );

        /*from( slowPathSchedule ).process(new Processor() {
         public void process(Exchange exch ) throws Exception {
         if( !slowPathRunning ){
         CamelContext ctx = exch.getContext();
         ctx.stopRoute("fast");
         ctx.startRoute("slow");
         slowPathRunning = true;
         fastPathRunning = false;                    
         }
         }
         });*/

        /*from( fastPathSchedule ).process(new Processor() {
         public void process(Exchange exch ) throws Exception {
         if( !fastPathRunning ){
         CamelContext ctx = exch.getContext();
         ctx.stopRoute("slow");
         ctx.startRoute("fast");
         fastPathRunning = true;
         slowPathRunning = false;
         }
         }
         });*/

        // create a custom bean that takes the Tika metadata and sends it up to the sky

        /*.unmarshal( tika  )
         .choice()
         .when(header("TikaContentType").isEqualTo( "text/plain; charset=ISO-8859-1" )).to( "http://localhost:9000/api/photo" )        
         .when(header("TikaContentType").isEqualTo("audio/mp4")).to( "http://localhost:9000/api/song" )      
         .when(header("TikaContentType").in(  ) ).to( "http://localhost:9000/api/document" )
         .setHeader(Exchange.HTTP_METHOD, constant("POST") )
         .setHeader(Exchange.HTTP_QUERY, simple("item.filename=${file:name}&item.filetype=audio&item.identifier=asdf&item.orderNumber=${file:parent}&item.size=${file:size}") )                        
         .to("http://localhost:9000/api/order/item")
         .setHeader(S3Constants.KEY, header("CamelFileNameOnly") )                        
         .to("aws-s3://flow7?accessKey=AKIAIFJQDYJQ462ABPQQ&secretKey=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&region=us-east-1");*/

    }
}
