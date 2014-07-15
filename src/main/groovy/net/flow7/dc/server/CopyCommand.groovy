package net.flow7.dc.server

import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import net.flow7.dc.server.ext.SystemScanner
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

        SystemScanner scanner = SystemScanner.get()

        String orderNumber = scanner.getOrderNumber()

        if( !orderNumber ){
            return false
        }

        File locationDir = new File( getPath(orderNumber, location ) )
        locationDir.mkdirs()

        File manifest = new File( locationDir, "drive-cleaners.manifest")
        manifest.createNewFile()
        manifest.text = orderNumber

        List<File> files = scanner.getStaged();

        files.each{ file ->
           FileUtils.copyFile( file, new File(  getPath( orderNumber, location ) , scanner.getRelativePath( file ) ) )
        }

        return true

    }

    protected static String getPath(String orderNumber, String location ){
        if( location.endsWith("/") ){
            return "${location}${orderNumber}"
        }else{
            return"${location}/${orderNumber}"
        }
    }
}
