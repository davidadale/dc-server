/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server
/**
 *
 * @author daviddale
 */
public class Windows extends FileSystem{

    List ignore = new LinkedList();
	
    public Windows() {
        super();
        ignore.add("Program Files")
        ignore.add("WINDOWS")
        ignore.add("Temporary Internet")
        ignore.add("System Volume Information")
        ignore.add("Application Data")
        ignore.add(".drivecleaners")
    }

    public List getIgnoreDirs() {
        return ignore;
    }
    
    public String getStartLocation(){
        return "/"
    }
    
    public String getSystemName(){
        return "Microsoft Windows"
    }
    
}
