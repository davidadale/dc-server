package net.flow7.dc.server

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import com.amazonaws.services.s3.model.ListObjectsRequest
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.S3ObjectSummary
import net.flow7.dc.server.ext.SystemScanner
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by ddale on 7/12/14.
 */
class S3DeleteCommand extends AbstractCommand {

    Options options;
    AmazonS3 client;
    String orderNumber;

    Logger log = LoggerFactory.getLogger( S3DeleteCommand.class )


    public S3DeleteCommand(){
        options = new Options();
        options.addOption( "o", "order", true, "Required, a valid order number." );
    }

    @Override
    void hint() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("rm [OPTIONS]", options);
    }

    @Override
    boolean process(String line) throws Exception {

        CommandLine cmd = getCommandLine( line, options )

        if( cmd.hasOption("o") ){
            orderNumber = cmd.getOptionValue("o")
        }

        if( !orderNumber ){
            orderNumber = SystemScanner.get().getOrderNumber()
        }

        if( !orderNumber ){
            return false
        }

        client = (AmazonS3) SystemRegistry.get().lookupObject("client");

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName( orderNumber )

        try{
            // query for a list of objects
            ObjectListing objectListing = client.listObjects(listObjectsRequest);

            // remove those objects
            removeObjects( objectListing )

            // if the list isn't complete, it's truncated then
            while( objectListing.isTruncated() ){

                listObjectsRequest.setMarker( objectListing.getNextMarker() )
                objectListing = client.listObjects( listObjectsRequest )
                removeObjects( objectListing )

            }

            // don't forget to delete bucket
            client.deleteBucket( orderNumber )

        }catch(Exception e){
            log.error( "Error deleting items in bucket", e )
        }

        return true
    }

    protected void removeObjects( ObjectListing objectListing ){

        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest( orderNumber )
        List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<DeleteObjectsRequest.KeyVersion>()

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            keys.add( new DeleteObjectsRequest.KeyVersion(objectSummary.getKey() ) )
        }

        if( !keys.isEmpty() ){
            multiObjectDeleteRequest.setKeys( keys )
            client.deleteObjects( multiObjectDeleteRequest )
        }

    }
}
