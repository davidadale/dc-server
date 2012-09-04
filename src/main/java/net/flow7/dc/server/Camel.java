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

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.apache.camel.dataformat.tika.TikaDataFormat;
import org.apache.camel.spi.DataFormat;

/**
 *
 * @author daviddale
 */
public class Camel extends RouteBuilder{
    
    DataFormat tika;
    
    public Camel(){
        tika = new TikaDataFormat();
    }

    public void configure() throws Exception {
        from("file://test")
        .unmarshal( tika  )
        .choice()
        //.when(header("TikaContentType").isEqualTo( "text/plain; charset=ISO-8859-1" )).to( "http://localhost:9000/api/photo" )        
        //.when(header("TikaContentType").isEqualTo("audio/mp4")).to( "http://localhost:9000/api/song" )      
        .when(header("TikaContentType").in("image/jpeg","","") ).to( "http://localhost:9000/api/document" )
        .setHeader(Exchange.HTTP_METHOD, constant("POST") )
        .setHeader(Exchange.HTTP_QUERY, simple("item.filename=${file:name}&item.filetype=audio&item.identifier=asdf&item.orderNumber=${file:parent}&item.size=${file:size}") )                        
        .to("http://localhost:9000/api/order/item")
        .setHeader(S3Constants.KEY, header("CamelFileNameOnly") )                        
        .to("aws-s3://flow7?accessKey=AKIAIFJQDYJQ462ABPQQ&secretKey=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&region=us-east-1");
        
    }
}
