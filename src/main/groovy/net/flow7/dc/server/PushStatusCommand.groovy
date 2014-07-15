package net.flow7.dc.server

import net.flow7.dc.server.ext.SystemScanner
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options

/**
 * Created by ddale on 7/14/14.
 */
class PushStatusCommand extends AbstractCommand {

    Options options


    public PushStatusCommand(){
        options = new Options()
    }

    @Override
    void hint() {

    }

    @Override
    boolean process(String line) throws Exception {
        CommandLine cmd = getCommandLine( line, options )
        println SystemScanner.get().pushResults
        return true
    }
}
