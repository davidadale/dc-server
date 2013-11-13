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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazonaws.auth.AWSCredentialsProvider

/**
 * A helper utility class to retrieve configuration properties.
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

    /**
     * Construct a new config object setting the home directory to the
     * users home directory and a directory called .drivecleaners. It will
     * create this directory if it doesn't exist.
     *
     * Set the configuration file to the dc.config file in the home directory.
     * Create the config file if it doesn't exist.  This is just a simple
     * Java Properties file.
     *
     */
    private Config(){        
        
        homeDir = new File( new File( System.getProperty("user.home") ), ".drivecleaners" );

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
            Class impl = Class.forName(prop("credentials.provider","com.amazonaws.auth.EnvironmentVariableCredentialsProvider"));
            obj = impl.newInstance();
            
        }catch(Exception e ){
            log.error("error trying to create the authentication object. err: " + e.getMessage() );
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
    /**
     * Load the properties from the dc.config file.
      */
    protected final void load(){
        try{
            //InputStream is = this.getClass().getClassLoader().getResourceAsStream("server.conf"); 
            props.load( new FileInputStream( configFile ) );
            
        }catch(Exception e){
            log.error("error trying to find the configuration file, server.conf", e);
        }
    }

    /**
     * Utility method for returning an Integer from the settings in dc.config
     * @param key
     * @return
     */
    public Integer getNumber( String key ){
        try{
            return Integer.valueOf( prop( key ) );
        }catch(Exception e ){
            return 0;
        }
    }

    /**
     * Utility method for returning an Integer form dc.config setting to default value if not set.
     * @param key
     * @param defaultValue
     * @return
     */
    public Integer getInt(String key, int defaultValue ){
        try{
            return Integer.valueOf( prop( key ) );
        }catch(Exception e ){
            return defaultValue;
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

    
    /**
     * Returns the environment variable if set.
     * @param key
     * @return
     */
    public String env(String key){
        return System.getenv( key );
    }


    /**
     * Set the file to use that will house the configurations
     * @param file
     */
    public static void setConfigFile(File file){
        configFile = file;
    }
    
}
