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

import org.apache.camel.main.Main;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author daviddale
 */
public class Server {
   
   
    
    public static void main(String[] args)throws Exception{
        Options options = new Options();
        options.addOption("t", false, "display current time");        
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse( options, args);        
        
        if( cmd.hasOption("t")){
            //server.start();
            Main main = new Main();
            main.enableHangupSupport();
            main.addRouteBuilder( new Camel() );
            System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
            main.run();            
        }
        
        
        
    }
    
}
