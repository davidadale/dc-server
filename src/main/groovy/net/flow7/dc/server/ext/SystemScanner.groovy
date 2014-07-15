/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.flow7.dc.server.ext

import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder

import static groovy.io.FileType.FILES
import static groovy.io.FileVisitResult.*
import java.util.regex.Pattern
import net.flow7.dc.server.*

//import org.apache.commons.io.FileUtils;
/**
 *
 * @author daviddale
 */
public class SystemScanner {
    
    static instance;
    
    List<File> staged;
    
    Long totalSize = 0;
    
    String orderNumber;

    FileSystem fileSystem;
    PeriodFormatter daysHoursMinutes
    DateTime startTime
    DateTime endTime

    Integer fileTransfersComplete = 0
    Integer fileTransfersFailed = 0

    boolean scanInProgress = false
    
    private SystemScanner(){
        staged = [];
        daysHoursMinutes = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(" day", " days")
                .appendSeparator(" and ")
                .appendMinutes()
                .appendSuffix(" minute", " minutes")
                .appendSeparator(" and ")
                .appendSeconds()
                .appendSuffix(" second", " seconds")
                .toFormatter();
    }
    
    public static SystemScanner get(){
        if( instance== null ){
            instance = new SystemScanner();
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

    public String getRelativePath(File file){
        return file.getCanonicalPath() - fileSystem.startScanAt().getCanonicalPath()
    }
    
    public Long getTotalUploadSize(){
        return totalSize;
    }
    
    public String getDisplayUploadSize(){
        return FileSystem.readableFileSize( totalSize );
    }
    
    public List<File> getStaged(){
        return staged;
    }
    
    public void clear(){
        staged = []
        totalSize = 0
        orderNumber = ""
        fileTransfersFailed = 0
        fileTransfersComplete = 0
    }
    
    public void scan( String orderNumber ){

        scanInProgress = true

        clear()
        
        this.orderNumber = orderNumber
        
        startTime = new DateTime()

        File current = fileSystem.startScanAt();
        List ignore = fileSystem.getIgnoreDirs();
        Pattern namePattern = fileSystem.getNamePattern();


        current.traverse(
            type: FILES,
            preDir:{ if( ignore.contains(it.name) || it.name.startsWith(".") ){ return SKIP_SUBTREE } },
            nameFilter: namePattern ){
                stage( it )
            }

        endTime = new DateTime()

        scanInProgress = false

    }

    public String getScanDetails(){

        if( scanInProgress ){
            """
                Scan in progress....
            """
        }else{
            StringBuffer duration = new StringBuffer()
            Period diff = new Period(startTime, endTime);
            daysHoursMinutes.printTo( duration, diff )
            """
            -------------------------------------------------
                System Name:  ${fileSystem.getSystemName()}
                Order Number: ${orderNumber?:"not specified"}
            -------------------------------------------------
                Location scanned:      ${fileSystem.rootedAt }
                Scan started at:       ${startTime.toString("MM/dd/yyyy HH:mm:ss")}
                Scan ended at:         ${endTime.toString("MM/dd/yyyy HH:mm:ss")}
                Duration:              ${duration}
                Number of files found: ${staged?.size()?:0}
                Total size of files:   ${getDisplayUploadSize()}
            -------------------------------------------------
            """
        }

    }

    public String getPushResults(){
        """
        --------------------------------------
            Files Staged:                   ${staged.size()?:0}
            Files Transferred Successfully: ${fileTransfersComplete}
            Files Failed To Transfer:       ${fileTransfersFailed}
            Percent Complete:               ${ ((Integer) (fileTransfersComplete?:1) /staged.size() * 100 ) } %
        --------------------------------------
        """
    }


     
}

