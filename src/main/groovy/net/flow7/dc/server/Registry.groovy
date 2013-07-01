/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.camel.impl.SimpleRegistry
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext

/**
 *
 * @author daviddale
 */
public class Registry {
    
    static Registry instance;
    
    Map<String,Class> registered = new HashMap<String,Class>();
    CamelContext context = null;
    SimpleRegistry camelRegistry = null;
    
    private Registry(){
        
    }

    protected CamelContext getContext(){
        if( context == null ){
            context = new DefaultCamelContext();
            context.getExecutorServiceStrategy().getDefaultThreadPoolProfile().setMaxPoolSize(50)
        }
        return context;
    }
    
    public Registry addRoutes(RouteBuilder builder){
        
        getContext().addRoutes( builder );
        return this;
        
    }
    
    public Registry bind(String key, Object object){
        
        if( camelRegistry == null ){
            camelRegistry = new SimpleRegistry();
            getContext().setRegistry( camelRegistry );
        }
        
        camelRegistry.put( key,object );
        
        return this;
    }
    
    public static Registry get(){
        
        if( instance ==  null ){
            instance = new Registry();
        }
        return instance;
        
    }
    
    public void register( Map<String,String> commands ) throws ClassNotFoundException{
        for( String key: commands.keySet() ){
            registered.put( key, Class.forName( commands.get(key) )  );
        }        
    }
    
    public List<Command> getCommands(){
        List<Command> commands = new LinkedList<Command>();
        for( String key: registered.keySet() ){

            try{
                
                commands.add( lookupCommand( key ) );
                
            }catch(Exception e){
                
            }
            
        }
        
        
        return commands;
    }
    
    public Command lookupCommand(String key) throws Exception{
        Class<Command> impl = registered.get( key );
        if( impl!=null ){
            return impl.newInstance();
        }
        
        return null;        
    }
    
    public Command parse(String line ) throws Exception{
        
        return lookupCommand( firstArg(line) );
    
    }
    
    protected String firstArg(String line ){
        String[] args = line.split(" ");
        return args[0];
    }
    
    
}
