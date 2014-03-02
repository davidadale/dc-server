package net.flow7.dc.server;

import com.google.common.base.Joiner

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.File;

public abstract class Files {

    Map<String, String> audio = new HashMap<String, String>();
    Map<String, String> documents = new HashMap<String, String>();
    Map<String, String> images = new HashMap<String, String>();
    Map<String, String> developer = new HashMap<String, String>();
    Map<String, String> defaultFilters = new HashMap<String,String>();
    
    static File rootedAt = null;
 
    static Files instance = null
    
    public static void setRoot( File rootedAt){
        this.rootedAt = rootedAt;        
    }

    public static Files get(String system) {

        if( "mac".equals(system) ){
            instance = getMacFilter();
        }else{
            instance = getWindowsFilter();
        }
       
        return instance;
    }

    public File startScanAt(){

        String fullPath

        if( !rootedAt ){
            rootedAt = new File("/")
            fullPath = rootedAt.getCanonicalPath() + getStartLocation()
        }else{
            fullPath = rootedAt.getCanonicalPath()
        }

        return new File( fullPath.endsWith("/")?fullPath.substring( 0, fullPath.length() - 1 ):fullPath );
    }
    
    
    protected static Object getMacFilter(){
        try{
            String clzz = Config.get().prop("macFilter","net.flow7.dc.server.Mac")
            return Class.forName( clzz ).newInstance()
        }catch(Exception e){
            e.printStackTrace()
            return null
        }
        
    }
    protected static Object getWindowsFilter(){
        try{
            String clzz = Config.get().prop("windowsFilter","net.flow7.dc.server.Windows")
            return Class.forName( clzz ).newInstance()
        }catch(Exception e){
            e.printStackTrace()
            return null            
        }        
    }
    
    
    
    public static String guess(){
        
        if( new File(rootedAt, "Windows").exists() ){
            return "windows"
        }
        
        if( new File(rootedAt, "Applications").exists() ){
            return "mac"
        }
        
        return null;
        
    }
    

    public boolean ignore(String value) {
        return getIgnoreDirs().contains(value);
    }

    public Pattern getNamePattern() {
        List exts = new LinkedList();
        
        for(String key: audio.keySet() ){
            exts.add( ".*" + key.replace(".", "\\.")+"\$" );
        }
        
        for( String key: documents.keySet() ){
            exts.add( ".*" + key.replace(".", "\\.")+"\$" );
        }

        for( String key: images.keySet() ){
            exts.add( ".*" + key.replace( ".","\\." ) +"\$" );
        }
        
        for( String key: developer.keySet() ){
            exts.add( ".*" + key.replace( ".","\\." ) +"\$" );
        }

        return Pattern.compile( Joiner.on("|").join( exts) );

    }

    protected Files() {
        audio.put(".aac", "Apple audio format");
        audio.put(".aif", "Audio Interchange File Format");
        audio.put(".iff", "Interchange File Format");
        audio.put(".m3u", "Media Playlist File");
        audio.put(".m4a", "MPEG-4 Audio File");
        audio.put(".mid", "MIDI File");
        audio.put(".mp3", "MP3 Audio File");
        audio.put(".mp4", "MP3 Audio File");
        audio.put(".m4v", "Apple Video format");
        audio.put(".mov", "Movie format");
        audio.put(".mpa", "MPEG-2 Audio File");
        audio.put(".ra", "Real Audio File");
        audio.put(".wav", "WAVE Audio File");
        audio.put(".wma", "Windows Media Audio File");        
        documents.put(".doc", "Microsoft Word Document");
        documents.put(".docx", "Microsoft Word Open XML Document");
        documents.put(".odt", "OpenDocument Text Document");
        documents.put(".pages", "Pages Document");
        documents.put(".rtf", "Rich Text Format File");
        documents.put(".tex", "LaTeX Source Document");
        documents.put(".txt", "Plain Text File");
        documents.put(".wpd", "WordPerfect Document");
        documents.put(".wps", "Microsoft Works Word Processor Document");
        documents.put(".csv", "Comma Separated Values File");
        documents.put(".dat", "Data File");
        documents.put(".efx", "eFax Document");
        documents.put(".epub", "Open eBook File");
        documents.put(".ibooks", "Multi-Touch iBook");
        documents.put(".key", "Keynote Presentation");
        documents.put(".pps", "PowerPoint Slide Show");
        documents.put(".ppt", "PowerPoint Presentation");
        documents.put(".pptx", "PowerPoint Open XML Presentation");
        documents.put(".tar", "Consolidated Unix File Archive");
        documents.put(".tax2010", "TurboTax 2010 Tax Return");
        documents.put(".tax2011", "TurboTax 2011 Tax Return");
        documents.put(".vcf", "vCard File");
        documents.put(".xml", "XML File");
        documents.put(".indd", "Adobe InDesign Document");
        documents.put(".pct", "Picture File");
        documents.put(".pdf", "Portable Document Format File");
        documents.put(".xlr", "Works Spreadsheet");
        documents.put(".xls", "Excel Spreadsheet");
        documents.put(".xlsx", "Microsoft Excel Open XML Spreadsheet");
        documents.put(".band", "Garage band file");
        images.put(".bmp", "Bitmap Image File");
        images.put(".dds", "DirectDraw Surface");
        images.put(".dng", "Digital Negative Image File");
        images.put(".gif", "Graphical Interchange Format File");
        images.put(".jpg", "JPEG Image");
        images.put(".png", "Portable Network Graphic");
        images.put(".psd", "Adobe Photoshop Document");
        images.put(".pspimage", "PaintShop Pro Image");
        images.put(".tga", "Targa Graphic");
        images.put(".thm", "Thumbnail Image File");
        images.put(".tif", "Tagged Image File");
        images.put(".yuv", "YUV Encoded Image File");
        images.put(".ai", "Adobe Illustrator File");
        images.put(".eps", "Encapsulated PostScript File");
        images.put(".ps", "PostScript File");
        images.put(".svg", "Scalable Vector Graphics File");
        developer.put(".java","Java Source file");
        developer.put(".jar","Java Archive file");
        developer.put(".groovy","Groovy source code");
        

    }

    public static String readableFileSize( long size ){
        if(size<=0){ return "0" }
        final String[] units = ["B","KB","MB","GB","TB"].toArray( new String[0] )
        int digitGroups = (int) ( Math.log10(size)/Math.log10(1024) )
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024,digitGroups) ) + " " + units[digitGroups]

    }

    public abstract List getIgnoreDirs();
    public abstract String getStartLocation();
    public abstract String getSystemName();
}