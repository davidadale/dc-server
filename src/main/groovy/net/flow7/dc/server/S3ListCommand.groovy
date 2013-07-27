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

/**
 *
 * @author daviddale
 */
public class S3ListCommand implements Command{
    
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
        
        ListObjectsRequest list = new ListObjectsRequest()
        .withBucketName( orderNumber )
        ObjectListing objectListing;
        long count = 0l;
        long size = 0l;
        for( S3ObjectSummary obejctSummary: objectListing.getObjectSummaries() ){
            count++;
            size += objectSummary.getSize()
        }
        
        println "Bucket contains ${count} objects"
        println "Bucket has a total size of ${FileUtils.byteCountToDisplaySize( size )}"
    }	
}

