/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.flow7.dc.server

import com.amazonaws.services.s3.model.ListObjectsRequest
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.S3ObjectSummary
import org.apache.commons.io.FileUtils
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.PosixParser
import org.apache.commons.cli.Options;
import com.amazonaws.services.s3.AmazonS3
/**
 *
 * @author daviddale
 */
public class S3ListCommand implements Command{
    
    Options options; 
    
    public S3ListCommand(){
        options = new Options();   
        options.addOption( "o", "order", true, "Required, a valid order number." );
    }
    
    public boolean handles(String word){
        return "list".equals( word )
    }

    public void hint(){
        // do this later.
    }

    public void perform(CommandCallback callback){
        // hmmm.... not for sure.
        
        
    }

    // push -u david@flow7.net -p
    public boolean process(String line) throws Exception{
        String[] args = line.split(" ");
        
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        
        // require the order option
        if( !cmd.hasOption("o") ){
            return false
        }        
        
        String orderNumber = cmd.getOptionValue("o")
        
        ListObjectsRequest listRequest = new ListObjectsRequest()
        .withBucketName( orderNumber )
        
        
        AmazonS3 client = (AmazonS3) Registry.get().lookupObject("client");
        ObjectListing objectListing;
        long count = 0l;
        long size = 0l;
        
        objectListing = client.listObjects( listRequest );
        
        for( S3ObjectSummary objectSummary: objectListing.getObjectSummaries() ){
            count++;
            size += objectSummary.getSize()
        }
        
        listRequest.setMarker( objectListing.getNextMarker() );
        
        while( objectListing.isTruncated() ){
            objectListing = client.listObjects( listRequest );

            for( S3ObjectSummary objectSummary: objectListing.getObjectSummaries() ){
                count++;
                size += objectSummary.getSize()
            }

            listRequest.setMarker( objectListing.getNextMarker() );            
        }

        
        println "Bucket contains ${count} objects"
        println "Bucket has a total size of ${FileUtils.byteCountToDisplaySize( size )}"
    }	
    
    
}

