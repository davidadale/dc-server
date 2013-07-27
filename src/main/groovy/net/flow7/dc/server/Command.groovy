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

    public boolean handles(String word);

    public void hint();

    public void perform(CommandCallback callback);

    // push -u david@flow7.net -p
    public boolean process(String line) throws Exception;
    
}
