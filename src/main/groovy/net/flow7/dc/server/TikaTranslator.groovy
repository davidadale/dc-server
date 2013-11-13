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
package net.flow7.dc.server

import org.apache.camel.Exchange
import org.apache.camel.component.aws.s3.S3Constants;
import org.apache.camel.spi.DataFormat;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

/**
 *
 * @author daviddale
 */
public class TikaTranslator {

    public TikaTranslator(){

    }

    public void process(Exchange exchange){
        Map headers = exchange.getIn().getHeaders()
        String type = (String) headers.get("TikaContentType")
        headers.put( S3Constants.CONTENT_TYPE, type )
    }
    
    
}
