/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

/**
 *
 * @author daviddale
 */
public class HelpCommand implements Command {

    
    
    
    @Override
    public boolean handles(String word) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return false;
    }

    @Override
    public void hint() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void perform(CommandCallback callback) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean process(String line) throws Exception {
       String[] values = line.split(" ");
       if( values.length > 1 ){
           
           String command = values[1];           
           Registry.get().lookupCommand( command ).hint();
       
       }else{
           
           for( Command command: Registry.get().getCommands() ){
               command.hint();
           }
           
       }
       
       return true;
    }
    
}
