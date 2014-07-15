package net.flow7.dc.server

import net.flow7.dc.server.routes.FtpRoute
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import net.flow7.dc.server.ext.SystemScanner

/**
 * Created with IntelliJ IDEA.
 * User: daviddale
 * Date: 12/28/13
 * Time: 10:15 AM
 * To change this template use File | Settings | File Templates.
 */
class FtpCommand  extends AbstractCommand{

    Options options

    FtpCommand() {
        this.options = new Options()
        options.addOption( "l", "FTP location", true, "Location to ftp files." );
    }

    @Override
    void hint() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ftp -l admin@server.com?password=secret&binary=true", options);
    }

    @Override
    boolean process(String line) throws Exception {

        String ftpLocation = getCommandLine( line, options ).getOptionValue("l")
        String order = getCommandLine( line, options ).getOptionValue("o")

        if( !order ){
            order = SystemScanner.get().getOrderNumber();
        }

        if( !ftpLocation || !order ){ return false }

        SystemScanner scanner = SystemScanner.get()
        List<File> files = scanner.getStaged();

        if( !files ){
            return false
        }

        SystemRegistry registry = SystemRegistry.get()
        FtpRoute ftpRoute = (FtpRoute) Routes.toFtp( order, ftpLocation )

        registry.getContext().addRoutes( ftpRoute  )
        registry.getContext().startRoute( ftpRoute.getRouteName() )

        ((IndexWriter)registry.getCamelRegistry().lookup("index")).setFile( order );

        files.each{ file ->
            HashMap<String,Object> headers = new HashMap<String,Object>();
            headers.put( "DC_FILENAME", file.getName() )
            headers.put( "DC_FILEPATH", scanner.getRelativePath( file ) )
            registry.getProducerTemplate().sendBodyAndHeaders("seda:${order}", file, headers );
        }


        return true  //To change body of implemented methods use File | Settings | File Templates.
    }
}
