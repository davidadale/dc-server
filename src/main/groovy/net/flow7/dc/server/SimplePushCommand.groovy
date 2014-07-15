
package net.flow7.dc.server;

import net.flow7.dc.server.ext.*;
import org.apache.camel.component.aws.s3.*
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options

/**
 *
 * @author daviddale
 */
public class SimplePushCommand extends AbstractCommand {

    Options options;

    public SimplePushCommand(){
        options = new Options();
        options.addOption( "o", "order", true, "Optional, a valid order number. Maybe be set at time of scanning." );
    }

    @Override
    public void hint() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("push [OPTIONS]", options);
    }

    @Override
    public boolean process(String line) throws Exception {

        String order = getCommandLine( line, options ).getOptionValue("o")

        if( !order ){
            order = SystemScanner.get().getOrderNumber();
        }

        if( !order ){
           return false
        }

        SystemScanner scanner = SystemScanner.get()

        List<File> files = scanner.getStaged();

        if( !files ){
            return false
        }

        SystemRegistry registry = SystemRegistry.get()

        //((IndexWriter)registry.getCamelRegistry().lookup("index")).setFile( order );
        ((Bucket)registry.getCamelRegistry().lookup("bucket")).reset()

        registry.getContext().addRoutes( Routes.toAmazon( order )  )
        registry.getContext().startRoute( order )

        try{

            files.each{ file ->

                HashMap<String,Object> headers = new HashMap<String,Object>();
                headers.put( S3Constants.BUCKET_NAME, order )
                headers.put( S3Constants.KEY, UUID.randomUUID().toString()  );
                headers.put( "dcFileName", file.getName() )
                headers.put( "dcFilePath", scanner.getRelativePath( file ) )
                registry.getProducerTemplate().sendBodyAndHeaders( "seda:${order}", file, headers );
            }

        }finally{

            //producerTemplate.stop();

        }

        return true
    }
    
}
