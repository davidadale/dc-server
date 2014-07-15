package net.flow7.dc.server;


import org.apache.camel.builder.RouteBuilder
import net.flow7.dc.server.routes.*
/**
 *
 * @author daviddale
 */
public class Routes {

    
    public static RouteBuilder toAmazon( String orderNumber ){
        int workers = Config.get().getInt( "workers", 1 );
        return new AmazonRoute( orderNumber, workers );
    }

    public static RouteBuilder toFtp( String orderNumber, String ftpLocation ){
        return new FtpRoute( orderNumber, ftpLocation )
    }

}
