/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

import net.flow7.dc.server.ext.*;
import org.apache.camel.component.aws.s3.*

/**
 *
 * @author daviddale
 */
public class SimplePushCommand implements Command {

    @Override
    public boolean handles(String word) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hint() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void perform(CommandCallback callback) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean process(String line) throws Exception {
        
        String order = Scanner.get().getOrderNumber();
        String bucket = "dc-${order}"
        
        if( !order ){
            throw new RuntimeException("Missing order number. Try rescanning the data.");
        }
        
        Registry.get().getContext().addRoutes( Routes.toAmazon("${bucket}") ) 
        Registry.get().getContext().startRoute( bucket )
        def producerTemplate = Registry.get().getContext().createProducerTemplate();
        
        List<File> files = Scanner.get().getStaged();
        files.each{ file ->
            //producerTemplate shove file onto queue
            println "File to transfer: ${file.path}"//.setHeader(S3Constants.KEY, header("CamelFileNameOnly") )
            
            HashMap<String,Object> headers = new HashMap<String,Object>();
            headers.put( S3Constants.KEY,file.name );
            headers.put( S3Constants.CONTENT_LENGTH, new Long( file.length() ) );
            
            producerTemplate.sendBodyAndHeaders("seda:${bucket}", file, headers );

        }
        
        Registry.get().getContext().stopRoute( bucket )
        return true
    }
    
}
