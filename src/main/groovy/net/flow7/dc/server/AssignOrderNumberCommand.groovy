package net.flow7.dc.server

import net.flow7.dc.server.ext.SystemScanner
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options

/**
 * Created by ddale on 6/26/14.
 */
class AssignOrderNumberCommand extends AbstractCommand{

    Options options


    public AssignOrderNumberCommand(){
        options = new Options();
        options.addOption( "o", "order", true, "Order number" );
    }


    @Override
    void hint() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("assign -o <order number>", options);
    }

    @Override
    boolean process(String line) throws Exception {
        String orderNumber = getCommandLine( line, options ).getOptionValue("o")
        if( !orderNumber ){
            return false
        }
        SystemScanner.get().setOrderNumber( orderNumber )
        return true
    }

}
