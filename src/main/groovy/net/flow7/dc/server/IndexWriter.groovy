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
package net.flow7.dc.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange
import org.apache.commons.lang.StringUtils;


public class IndexWriter {

    Gson gson

    String routeId = null
    List<Map> items = new LinkedList<Map>()

    public IndexWriter(){
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void write( Exchange exchange ){
        if( !routeId ){
            routeId = exchange.fromRouteId
        }

        Map messageHeaders = exchange.getIn().getHeaders()

        items.add( formatKeys( messageHeaders ) )
    }

    protected Map formatKeys( messageHeaders ){
        messageHeaders.collectEntries{ k, v ->
            String key = k.replace(" ", "")
            key = key.replace("_", "")
            [StringUtils.uncapitalize( key ), v ]
        }
    }

    public File getIndexFile(){
        return new File( System.getProperty("user.dir"), "${routeId}.json")
    }

    public String getJson(){
        return gson.toJson( items )
    }

}
