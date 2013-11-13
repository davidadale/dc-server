package net.flow7.dc.server

import org.apache.camel.Exchange
import org.apache.camel.Message;
import org.junit.Test;
import static org.mockito.Mockito.*
/**
 * Created with IntelliJ IDEA.
 * User: daviddale
 * Date: 8/25/13
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexWriterTests {

    @Test
    public void test_read_and_write_of_index_file(){

        IndexWriter index = new IndexWriter("test_file")

        for(int i=0;i<3;i++){

            Map<String,Object> headers = new HashMap<String,Object>();
            headers.put("fileName", (String) "Filename${i}")
            headers.put("filePath", (String) "/some/path/${i}")

            Message msg = mock(Message)
            when(msg.getHeaders()).thenReturn( headers )

            Exchange ex = mock(Exchange.class)
            when( ex.getIn() ).thenReturn( msg )
            index.write( ex )
        }
        index.setWriter( new  StringWriter() )
        index.flush()
        List objects = index.read()

        assert objects.size() == 3
        assert objects.first().fileName == "Filename0"

    }






}