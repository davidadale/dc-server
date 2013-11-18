/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

import net.flow7.dc.server.ext.*;
import org.apache.camel.component.aws.s3.*
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.io.IOUtils
import com.google.common.io.ByteStreams

import java.security.MessageDigest

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
            order = Scanner.get().getOrderNumber();
        }

        if( !order ){
           return false
        }

        Scanner scanner = Scanner.get()

        List<File> files = scanner.getStaged();

        if( !files ){
            return false
        }

        SystemRegistry registry = SystemRegistry.get()


        registry.getContext().addRoutes( Routes.toAmazon( order )  )
        registry.getContext().startRoute( order )

        ((IndexWriter)registry.getCamelRegistry().lookup("index")).setFile( order );
        ((Bucket)registry.getCamelRegistry().lookup("bucket")).reset()

        try{

            files.each{ file ->

                HashMap<String,Object> headers = new HashMap<String,Object>();
                headers.put( S3Constants.BUCKET_NAME, order )
                headers.put( S3Constants.KEY, UUID.randomUUID().toString()  );
                headers.put( "DC_FILENAME", file.getName() )
                headers.put( "DC_FILEPATH", scanner.getRelativePath( file ) )
                registry.getProducerTemplate().sendBodyAndHeaders("seda:${order}", file, headers );
            }

        }finally{

            //producerTemplate.stop();

        }

        return true
    }
    
}
