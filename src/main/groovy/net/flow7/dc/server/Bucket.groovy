package net.flow7.dc.server

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CreateBucketRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.PutObjectResult
import org.apache.camel.Exchange
import org.apache.camel.component.aws.s3.S3Constants
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class Bucket {

    Logger logger = LoggerFactory.getLogger( Bucket.class )
    Long totalSize = 0  // could be a issue if we had multi threads but this model should work fine with command line

    public void put( Exchange exchange ){

        File file = exchange.getIn().getMandatoryBody(File.class)
        if( !file ){
             throw new RuntimeException("missing file as body of message")
        }

        if( Config.get().envAsBoolean("AMAZON_NOOP") ){

            logUpdate( file )

        }else{

            uploadToAmazon( exchange, file )
        }



    }

    protected void uploadToAmazon(Exchange exchange, File file){
        AmazonS3 client = (AmazonS3) SystemRegistry.get().getCamelRegistry().lookup("client");
        Map headers = exchange.getIn().getHeaders()

        CreateBucketRequest createBucketRequest = new CreateBucketRequest( (String) headers.get(S3Constants.BUCKET_NAME) )
        client.createBucket( createBucketRequest )

        PutObjectRequest req = new PutObjectRequest( (String) headers.get(S3Constants.BUCKET_NAME),
                (String)headers.get( S3Constants.KEY ), file  )

        try{
            PutObjectResult putObjectResult = client.putObject( req )
            logUpdate( file )
        }catch(Exception e){
            e.printStackTrace()
        }

    }

    protected void reset(){
        this.totalSize= 0;
    }

    protected void logUpdate( File file ){
        long fileSize = file.length()
        totalSize += fileSize
        logger.info( "File copied to server ${file.name} and file size ${fileSize}" )
        FileUtils.byteCountToDisplaySize( totalSize )
    }

}



