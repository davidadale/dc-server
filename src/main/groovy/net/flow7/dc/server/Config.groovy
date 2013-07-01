/*
 * Copyright 2012 daviddale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.flow7.dc.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider 
/**
 *
 * @author daviddale
 */
public class Config {
    
    Logger log = LoggerFactory.getLogger(Config.class);
    Properties props;
    File homeDir;
    static File configFile;
    
    static Config instance = null;
    
    
    public File getHomeDir(){
        return homeDir;
    }
    
    public static Config get(){
        if( instance == null ){
            instance = new Config();
        }
        return instance;
    }
    
    private Config(){        
        
        homeDir = new File( new File(System.getProperty("user.home")), ".drivecleaners" );        
        if( !homeDir.exists() ){
            homeDir.mkdir();
        }
        
        if( configFile == null ){
            configFile = new File( homeDir, "dc.config");
            if( !configFile.exists() ){
                createConfigFile();
            }            
        }

        
        props = new Properties();
        load();
    }
    
    public AWSCredentialsProvider getCredentialsProvider(){
        Object obj = null;
        try{
            Class impl = Class.forName(prop("credentials.provider","com.amazonaws.auth.SystemPropertiesCredentialsProvider")); 
            obj = impl.newInstance();
            
        }catch(Exception e ){
            log.error("errored trying to create the authentication object. err: " + e.getMessage() );
            return null;
        }
        return (AWSCredentialsProvider)obj;
    }
    
    private void createConfigFile(){
        try{
            configFile.createNewFile();
        }catch(Exception e){
            
        }
    }
    
    protected final void load(){
        try{
            //InputStream is = this.getClass().getClassLoader().getResourceAsStream("server.conf"); 
            props.load( new FileInputStream( configFile ) );
            
        }catch(Exception e){
            log.error("error trying to find the configuration file, server.conf", e);
        }
    }
    
    public Integer getNumber( String key ){
        try{
            return Integer.valueOf( prop( key ) );
        }catch(Exception e ){
            return 0;
        }
    }
    
    
    
    /**
     * Returns the property from configuration file.
     * @param key
     * @return
     */
    public String prop(String key){
        return props.getProperty( key );
    }
    
    /**
     * Allow the user to specify a reasonable default.
     * @param key
     * @param defaultValue
     * @return
     */
    public String prop(String key, String defaultValue ){
        String result = prop( key );
        if( result==null || result.length()==0 ){
            return defaultValue;
        }
        
        return result;
    }
    
    
    
    public String[] values(String key){
        return props.getProperty(key).split(",");
    }
    
    
    /**
     * Returns the environment variable if set.
     * @param key
     * @return
     */
    public String env(String key){
        return System.getenv( key );
    }

    public void printConfig(){
       /* Set<String> props = props.string
        for(String prop: props ){
            System.out.println("Property is " + prop );
        }*/
    }
    
    public static void setConfigFile(File file){
        configFile = file;
    }
    
}
