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

import java.io.File
import java.io.PrintWriter
import java.util.HashMap
import java.util.Map
import jline.console.ConsoleReader
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.PosixParser
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.amazonaws.ClientConfiguration

/**
 *
 * @author daviddale
 */
public class Server {
    
    
    static Options options;
    Server server;
    
    Registry registry;
    static Logger logger = LoggerFactory.getLogger( Server.class );
    
    
    public Server() {
        
        log("Server starting to scan directories.");        
        
        Map commands = new HashMap();
        
        commands.put( "scan", Config.get().prop("command.scan","net.flow7.dc.server.ScanCommand") );
        commands.put( "copy", Config.get().prop("command.copy","net.flow7.dc.server.CopyCommand") );
        commands.put( "push", Config.get().prop("command.push","net.flow7.dc.server.SimplePushCommand") );
        commands.put( "list", Config.get().prop("command.list","net.flow7.dc.server.S3ListCommand"));
        commands.put( "help", "net.flow7.dc.server.HelpCommand" );
        
        
        
        Integer socketTimeout = Config.get().getInt( "socketTimeout", 600000 );
        
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setSocketTimeout( socketTimeout ); // wait 10 by default.
        
        log( "Socket timeout is set to: %s", socketTimeout );
        
        // create an amazon client 
        AmazonS3 client = new AmazonS3Client( Config.get().getCredentialsProvider(), clientConfig );        
        Registry.get().bind( "client", client )
        Registry.get().getContext().start()
        
        try{
            
            Registry.get().register( commands );
            
        }catch(Exception e){
            // handle
        }
        
    }

    private void log(String msg, Object... args) {

        if (args != null) {
            System.out.println(String.format(msg, (Object[]) args));
        } else {
            System.out.println(msg);
        }

    }

    public static void main(String[] args) throws Exception {
        
        options = new Options();
        options.addOption("f", "file", true, "External configuration file.");

        CommandLineParser parser = new PosixParser();
        CommandLine cmdLine = parser.parse(options, args);

        if ( cmdLine.hasOption("f") ) {
            
            String path = cmdLine.getOptionValue("f");
            logger.info("The server is using a config file: " + path)

            File file  = new File( path );
            if( !file.exists() ){
                throw new RuntimeException("The file specified doesn't exist.");
            }            
            
            Config.setConfigFile( file );
            
        }        
        
        Server server = new Server();
        
        ConsoleReader reader = new ConsoleReader();
        reader.setBellEnabled(false);
        reader.setHistoryEnabled( true );
        
        String line;
        PrintWriter out = new PrintWriter( System.out );        
        
        while ((line = reader.readLine("prompt> ")) != null) {
            
            Command cmd = server.findCommand( line );
            
            
            if( cmd!=null ){               
               
                try{
                    if( !cmd.process( line ) ){
                        cmd.hint()
                    }                    
                }catch(Exception e ){
                    System.out.println( "Error: ${e.message}" )
                    e.printStackTrace()
                }

            }
            
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                Registry.get().getContext().stop()
                break;
            }            
        }

    }
    
    public Command findCommand(String line ){
        try{
            
          return Registry.get().parse( line );  
        
        }catch(Exception e){
            // handle exception
            e.printStackTrace();
        }
        
        return null;
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("cytoscape.{sh|bat} [OPTIONS]", options);
    }
}
