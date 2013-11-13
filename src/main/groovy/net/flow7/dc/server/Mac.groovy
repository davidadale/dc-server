/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author daviddale
 */
public class Mac extends Files {

    List ignore = new LinkedList();
    
    public Mac(){
        super();      
        ignore.add(".drivecleaners")
        ignore.add("Library")
    }    
    
    @Override
    public List getIgnoreDirs() {
        return ignore;
    }

    @Override
    public String getStartLocation(){
        return System.getProperty("user.home")
    }

    @Override
    public String getSystemName(){
        return "Macintosh"
    }
    
}
