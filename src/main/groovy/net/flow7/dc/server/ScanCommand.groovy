/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.flow7.dc.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import net.flow7.dc.server.ext.Scanner;

/**
 *
 * @author daviddale
 */
public class ScanCommand extends AbstractCommand {

    Options options;
    
    public ScanCommand(){
        options = new Options();   
        options.addOption( "o", "order", true, "Optional, a valid order number." );
        options.addOption( "s", "start", true,  "Optional, start the scan at this location. Defaults current directory." );
        options.addOption( "t", "type", true, "Optional, type of operating system ( mac, windows ). Will try and guess OS." );    
    }

    @Override
    public void hint() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("scan -o xxxxx [OPTIONS]", options);        
    }


    @Override
    public boolean process(String line) throws Exception {

        CommandLine cmd = getCommandLine(line, options);
        Files filter = determineSystem( cmd, determineStart( cmd ) )

        Scanner.get().setFiles( filter )
        Scanner.get().scan( cmd.getOptionValue("o") );

        println Scanner.get().getScanDetails()

        return true;
    }

    /**
     * Return the type of System Mac or Windows (the only supported systems right now)
     * @param cmd
     * @param current
     * @return
     */
    protected Files determineSystem( CommandLine cmd, File current ){
        String system;
        
        Files.setRoot( current );
        
        if( cmd.hasOption("t") ){
            system = cmd.getOptionValue("t");
        }else{
            system = Files.guess()
        }
        
        return Files.get( system );
    }

    /**
     * Create a File that points the root of the scanner.
     * @param cmd
     * @return
     */
    protected File determineStart( CommandLine cmd ){

        // Use current directory by default
        File current = new File( System.getProperty("user.dir") )

        // in included mount at a different location
        if( cmd.hasOption("s") ){
            current = new File( cmd.getOptionValue("s") );
        }     
        
        return current
    }
    
    
    
}

