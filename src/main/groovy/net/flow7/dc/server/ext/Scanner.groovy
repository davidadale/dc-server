/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.flow7.dc.server.ext

import static groovy.io.FileType.FILES
import static groovy.io.FileVisitResult.*
import org.apache.commons.io.FileUtils
import java.util.regex.Pattern

//import org.apache.commons.io.FileUtils;
/**
 *
 * @author daviddale
 */
public class Scanner {
    
    static instance;
    
    List<File> staged;
    
    Long totalSize = 0;
    
    String orderNumber;
    
    private Scanner(){
        staged = [];
    }
    
    public static Scanner get(){
        if( instance== null ){
            instance = new Scanner();
        }
        return instance;
    }
    
    public String getOrderNumber(){
        return this.orderNumber;
    }

    public void stage( File file ){
        totalSize += file.length();
        staged.add( file );
    }
    
    public void unstage( File file ){
        totalSize -= file.length();
        staged.remove( file );
    }
    
    public Integer getTotalNumberOfFiles(){
        return staged?.size()?:0;
    }
    
    public Long getTotalUploadSize(){
        return totalSize;
    }
    
    public String getDisplayUploadSize(){
        return FileUtils.byteCountToDisplaySize( totalSize );
    }
    
    public List<File> getStaged(){
        return staged;
    }
    
    public void clear(){
        staged = []
        totalSize = 0
        orderNumber = ""
    }
    
    public void scan(String orderNumber, File current, List ignore, Pattern namePattern){
        
        clear()
        
        this.orderNumber = orderNumber
        
        def start = new Date();
        
        //new File("dc-files.txt").withWriter { out ->
            current.traverse(
                type: FILES,
                preDir:{ if( ignore.contains(it.name) || it.name.startsWith(".") ){ return SKIP_SUBTREE } },
                nameFilter: namePattern ){
                    //out << "${it}\n"
                    //println "${it.absolutePath - current.absolutePath}"
                    stage( it )
                }
        //}
        println "Scan started at ${start}"    
        println "Scan completed at ${ new Date() }"    
    }
     
}

