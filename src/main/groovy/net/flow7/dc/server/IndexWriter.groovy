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

import org.apache.camel.Exchange;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author daviddale
 */
public class IndexWriter {
    
    Yaml yaml = null
    String routeId = null
    Writer writer = null
    List stuff = []
    File file = null


    public IndexWriter(){
        yaml = new Yaml()
    }

    public IndexWriter( String routeId ){
        this()
    }

    public IndexWriter( Writer writer ){
        this()
        this.writer = writer
    }

    public void setFile( String order ){
        File dir = new File( System.getProperty("user.home"), "Desktop" )
        this.file = new File( dir, "${order}-index.yml")

    }

    protected File createFile(){
        // TODO: Fix this file path so that it will be configured outside of index
        File dir = new File( System.getProperty("user.home"), "Desktop" )
        File indexFile = new File( dir, "${routeId}.yml")
        return indexFile
    }

    public void setWriter(Writer writer ){
        this.writer = writer
    }

    
    public void write( Exchange exchange ){

        if( routeId == null ){
            routeId = exchange.getFromRouteId()
        }

        Map headers = exchange.getIn().getHeaders()

        if( writer ){
            appendToWriter( headers )
        }else{
            appendToFile( headers )
        }

    }

    protected void appendToWriter( Map headers ){
        writer.append( "- ${yaml.dump(headers)}\n" )
    }

    protected void appendToFile( Map headers ){
        if( file == null ){
            file = createFile()
        }
        file.append("- ${yaml.dump(headers)}\n")
    }

    public void flush(){
        if( writer==null ){
            writer = new FileWriter( createFile() )
        }
        yaml.dump( stuff, writer )
    }


    public List read(){

        if( writer instanceof StringWriter ){
            return yaml.load( writer.toString() )
        }

        return yaml.load( new FileInputStream( createFile() ) )

    }

    
}
