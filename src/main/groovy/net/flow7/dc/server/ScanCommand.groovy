/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.flow7.dc.server;

import java.io.File;
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
public class ScanCommand implements Command {
    
    String longOpt = "scan";
    Options options;
    
    public ScanCommand(){
        options = new Options();   
        options.addOption( "o", "order", true, "Required, a valid order number." );
        options.addOption( "s", "start", true,  "Optional, start the scan at this location. Defaults current directory." );
        options.addOption( "t", "type", true, "Optional, type of operating system ( mac, windows ). Will try and guess OS." );    
    }
    
    
    @Override
    public boolean handles(String word) {
        return "scan".equals(word);
    }

    @Override
    public void hint() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("scan -o xxxxx [OPTIONS]", options);        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void perform(CommandCallback callback) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean process(String line) throws Exception {
        String[] args = line.split(" ");        
        
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        
        // require the order option
        if( !cmd.hasOption("o") ){
            return false
        }
        
        String orderNumber = cmd.getOptionValue("o")
        
        Files filter = determineSystem( cmd )        
        File current = determineStart( cmd, filter );
        
        println "System Scanning: ${filter.systemName}"
        
        Scanner.get().scan( orderNumber, current, filter.getIgnoreDirs(), filter.getNamePattern() );

        println "number of documents found ${Scanner.get().totalNumberOfFiles} for a total size: ${Scanner.get().displayUploadSize}"
        println "totalSize ${Scanner.get().totalUploadSize}"
        return true; 
    }
    
    protected boolean validateOrder( String order ){
        
    }
    
    protected Files determineSystem( CommandLine cmd ){
        String system = "windows";
        
        if( cmd.hasOption("t") ){
            system = cmd.getOptionValue("t");
        }else{
            system = Files.guess()
        }
        
        return Files.get( system );
    }
    
    protected File determineStart( CommandLine cmd, Files filter ){
        
        File current = new File( System.getProperty("user.home") )
        
        if( cmd.hasOption("s") ){
            current = new File( cmd.getOptionValue("s") );
        }//else{
           // current = new File( "." );
        //}        
        
        return current
    }
    
    
    
}

