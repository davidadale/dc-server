package net.flow7.dc.server

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import net.flow7.dc.server.ext.Scanner
import org.apache.commons.io.FileUtils

/**
 * Created with IntelliJ IDEA.
 * User: daviddale
 * Date: 12/4/13
 * Time: 10:51 PM
 * To change this template use File | Settings | File Templates.
 */
class CopyCommand extends AbstractCommand{

    Options options


    public CopyCommand(){
        options = new Options();
        options.addOption( "l", "location", true, "Location to copy to." );
    }

    @Override
    void hint() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("copy -l /path/to/store/copied/files", options);
    }

    @Override
    boolean process(String line) throws Exception {

        String location = getCommandLine( line, options ).getOptionValue("l")

        if( !location ){ return false }

        Scanner scanner = Scanner.get()

        List<File> files = scanner.getStaged();

        files.each{ file ->
           FileUtils.copyFile( file, new File(location, scanner.getRelativePath( file ) ) )
        }

        return true

    }
}
