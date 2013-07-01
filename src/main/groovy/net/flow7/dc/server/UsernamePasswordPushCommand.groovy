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
public class UsernamePasswordPushCommand implements Command {
    
    String longOpt = "push";
    Options options;
    
    public UsernamePasswordPushCommand(){
        options = new Options();
        options.addOption("u", "user", true, "Amazon S3 account username.");
        options.addOption("p", "password", true, "Amazon S3 account password");
    }
    
    public boolean handles( String word ){
        return "push".equals(word);
    }
    
    @Override
    public void hint(){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("push [OPTIONS]", options);
    }
    
    // push -u david@flow7.net -p
    public boolean process( String line ) throws Exception{
        
        String[] args = line.split(" ");        
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);        
        
        String username = null;// get username
        String password = null;// get password

        //Object amazonCredentials = getAmazonCredentials(username, password);
        
        for( File file: Scanner.get().getStaged() ){
            //Amazon.push( amazonCredentials, file );
        }
        
        // this may do nothing other than call perform
        // in username password
        return false;
    }
    
    public void perform( CommandCallback callback ){
        
        
        
        
    }
    
}
