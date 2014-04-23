package net.flow7.dc.server

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import net.flow7.dc.server.ext.Scanner
import org.apache.commons.io.FileUtils

/**
 * This command will do a simple copy moving the scanned
 * files to a location some where else on the HD or device
 * that can be accessible from normal file url.
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
           FileUtils.copyFile( file, new File(  getPath( location ) , scanner.getRelativePath( file ) ) )
        }

        return true

    }

    protected String getPath(String location ){
        String path = location
        String orderNumber = Scanner.get().orderNumber
        if( orderNumber ){
            if( location.endsWith("/") ){
                path = "${location}${orderNumber}"
            }else{
                path = "${location}/${orderNumber}"
            }
        }
        return path
    }
}
