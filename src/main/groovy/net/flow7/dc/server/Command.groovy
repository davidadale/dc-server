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

    boolean handles(String word);

    void hint();

    void perform(CommandCallback callback);

    // push -u david@flow7.net -p
    boolean process(String line) throws Exception;
    
}
