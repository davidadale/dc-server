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

import jline.console.ConsoleReader
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.PosixParser
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.amazonaws.ClientConfiguration

/**
 * This is the main class and will start up the client.
 * @author daviddale
 */
public class Server {
    
    // options for the server
    static Options options;

    // singleton instance of server
    Server server;

    // a registry of object instances
    SystemRegistry registry;

    static Logger logger = LoggerFactory.getLogger( Server.class );
    
    
    public Server() {

        logger.info( "Server starting to scan directories." )

        Map commands = new HashMap();
        
        commands.put( "scan", Config.get().prop("command.scan","net.flow7.dc.server.ScanCommand") )
        commands.put( "copy", Config.get().prop("command.scan","net.flow7.dc.server.CopyCommand"))
        commands.put( "push", Config.get().prop("command.push","net.flow7.dc.server.SimplePushCommand") )
        commands.put( "list", Config.get().prop("command.list","net.flow7.dc.server.S3ListCommand") )
        commands.put( "ftp",  Config.get().prop("command.ftp", "net.flow7.dc.server.FtpCommand" )  )

        commands.put( "help", Config.get().prop("command.help","net.flow7.dc.server.HelpCommand" ) )

        // get the socket time out for the network calls when pushing to Amazon (defaults to 10 minutes)
        Integer socketTimeout = Config.get().getInt( "socketTimeout", 600000 );

        // Amazon client configuration with a default time out of 10 minutes
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setSocketTimeout( socketTimeout ); // wait 10 by default.

        logger.debug( "Socket timeout is set to: %s", socketTimeout  )

        // create an amazon client 
        AmazonS3 client = new AmazonS3Client( Config.get().getCredentialsProvider(), clientConfig );

        // register a few objects for use later on
        SystemRegistry.get().bind( "client", client )
        SystemRegistry.get().bind( "index", new IndexWriter() )
        SystemRegistry.get().bind( "translator", new TikaTranslator() )
        SystemRegistry.get().bind( "bucket", new Bucket() )

        // start the camel context
        SystemRegistry.get().getContext().start()

        try{
            // this registers all the available commands for the
            // command line
            SystemRegistry.get().register( commands );
            
        }catch( ClassNotFoundException e ){
            logger.error( "Missing class for command.", e )
        }
        
    }

    public static void main(String[] args) throws Exception {

        // options to configure the local server
        options = new Options();
        options.addOption("f", "file", true, "External configuration file.");

        // command line parser
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
        reader.setBellEnabled( false );
        reader.setHistoryEnabled( true );
        
        String line;

        // Server Loop to query in the commands from user.
        while ((line = reader.readLine("prompt> ")) != null) {
            
            Command cmd = server.findCommand( line );

            if( cmd!=null ){               
               
                try{
                    if( !cmd.process( line ) ){
                        cmd.hint()
                    }                    
                }catch(Exception e ){

                    logger.error("Error executing command. err: " + e.getMessage(), e )

                }

            }
            
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {

                SystemRegistry.get().getContext().stop()
                break;

            }            
        }

    }
    
    public Command findCommand(String line ){
        try{
            
          return SystemRegistry.get().parse( line );
        
        }catch(Exception e){
            // handle exception
            logger.error( "Error finding command. err: " + e.getMessage() )

        }
        
        return null;
    }

}
