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
import org.apache.camel.component.properties.PropertiesComponent
import org.apache.camel.dataformat.tika.TikaDataFormat
import org.apache.camel.impl.JndiRegistry
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry
import org.apache.camel.spi.Registry;
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

    IndexWriter index

    public void setUp() throws Exception{
        super.setUp();
        inboxDir = context.resolvePropertyPlaceholders("target/inbox");
        outboxDir = context.resolvePropertyPlaceholders("target/outbox");
        deleteDirectory(inboxDir);
        deleteDirectory(outboxDir);

    }
    
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            void configure() throws Exception {
                from("{{orders.inbox}}")
                .unmarshal( new TikaDataFormat() )
                .to("bean:index")
                .to("{{orders.outbox}}")
                //.bean( index, "write" )
                //from("direct:fileDetails")
                //.to("bean:#index?method=write")
            }
        }
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {

        CamelContext ctx =  super.createCamelContext();

        PropertiesComponent prop = ctx.getComponent("properties", PropertiesComponent.class );
        prop.setLocation("classpath:net/flow7/dc/server/test-config.properties");

        Registry reg = ctx.getRegistry()

        if( reg instanceof PropertyPlaceholderDelegateRegistry ){

            Registry graph = ((PropertyPlaceholderDelegateRegistry)ctx.getRegistry()).getRegistry()
            if( graph instanceof JndiRegistry ){
                index = new IndexWriter( new StringWriter() )
                ((JndiRegistry)graph).bind("index", index )
            }

        }

        return ctx;
    }

    @Test
    public void test_contents_of_yaml_index(){

        File buildDir = new File( System.getProperty("user.dir") )
        MockEndpoint output = getMockEndpoint("mock:target/outbox")
        output.expectedMessageCount( 1 )

        File image = new File( buildDir, "/src/test/data/av150.jpg" )

        assert image.exists()


        HashMap headers = new HashMap()
        headers.put("realFileName",image.name)
        headers.put("realFilePath", image.path )

        orders.sendBodyAndHeaders( image, headers )
        output.assertIsSatisfied()

        //index.flush()
        List objects = index.read()

        assert objects.first().realFileName == "av150.jpg"
        assert objects.first().realFilePath == "/Users/daviddale/projects/dc-server/src/test/data/av150.jpg"

    }

/*
    - {CamelAwsS3ContentLength: 1078, CamelAwsS3Key: 9bed2539-686e-403b-aa0a-2c24474c73b6,
  TikaContentEncoding: UTF-8, breadcrumbId: ID-David-Dales-MacBook-Air-local-50876-1378812253495-0-1,
  DC_FILEPATH: /Users/daviddale/Desktop/damage/release_reflection_qa_structure.txt,
  TikaContentType: text/plain; charset=UTF-8, DC_FILENAME: release_reflection_qa_structure.txt}
     */

    @Test
    public void test_index_format(){

        StringWriter writer = new StringWriter()

        IndexWriter index = new IndexWriter(writer)
        writer.write("""- {CamelAwsS3ContentLength: 1078, CamelAwsS3Key: 9bed2539-686e-403b-aa0a-2c24474c73b6,
                            TikaContentEncoding: UTF-8, breadcrumbId: ID-David-Dales-MacBook-Air-local-50876-1378812253495-0-1,
                            DC_FILEPATH: /Users/daviddale/Desktop/damage/release_reflection_qa_structure.txt,
                            TikaContentType: text/plain; charset=UTF-8, DC_FILENAME: release_reflection_qa_structure.txt}""")

        def objs = index.read()

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
