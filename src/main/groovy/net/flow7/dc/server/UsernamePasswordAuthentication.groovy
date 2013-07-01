/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSCredentials
/**
 *
 * @author daviddale
 */
public class UsernamePasswordAuthentication implements AWSCredentialsProvider{

    
    public AWSCredentials getCredentials(){
        return null;
    }
    
    public void refresh(){
        
    }

    
}
