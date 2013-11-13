/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

/**
 *
 * @author daviddale
 */
public interface Command {

    public void hint();
    public boolean process(String line) throws Exception;
    
}
