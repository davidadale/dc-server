/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

/**
 *
 * @author daviddale
 */
public class SimpleAuthentication implements AmazonCredentials {

    @Override
    public String getAccessKeyId() {
        return Config.get().prop("accessKeyId");
    }

    @Override
    public String getSecretAccessKey() {
        return Config.get().prop("secretAccessKey");       
    }
    
}
