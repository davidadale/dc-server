package net.flow7.dc.server

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CreateBucketRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.PutObjectResult
import org.apache.camel.Exchange
import org.apache.camel.component.aws.s3.S3Constants

/**
 * Created with IntelliJ IDEA.
 * User: daviddale
 * Date: 11/13/13
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
class Bucket {


    public void put( Exchange exchange ){

        File file = exchange.getIn().getMandatoryBody(File.class)
        if( !file ){
             throw new RuntimeException("missing file as body of message")
        }

        AmazonS3 client = (AmazonS3) SystemRegistry.get().getCamelRegistry().lookup("client");
        Map headers = exchange.getIn().getHeaders()

        CreateBucketRequest createBucketRequest = new CreateBucketRequest( (String) headers.get(S3Constants.BUCKET_NAME) )
        client.createBucket( createBucketRequest )

        PutObjectRequest req = new PutObjectRequest( (String) headers.get(S3Constants.BUCKET_NAME),
                                                     (String)headers.get( S3Constants.KEY ), file  )

        PutObjectResult putObjectResult = client.putObject( req )

    }

}



