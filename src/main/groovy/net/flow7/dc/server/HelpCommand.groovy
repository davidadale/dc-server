/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options;

/**
 *
 * @author daviddale
 */
public class HelpCommand implements Command {

    Options options;

    public HelpCommand(){
        options = new Options();
    }


    @Override
    public void hint() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("help command", options);
    }

    @Override
    public boolean process(String line) throws Exception {

        String[] values = line.split(" ");

        if( values.length > 1 ){
           String command = values[1];
           SystemRegistry.get().lookupCommand( command ).hint();
       }else{
           hint();
           for(Command cmd: SystemRegistry.get().commands){
               if( !(cmd instanceof HelpCommand) ){
                   cmd.hint()
                   println "\n"
               }
           }
       }
       
       return true;
    }
    
}
