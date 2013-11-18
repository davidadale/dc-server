/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server

import org.apache.camel.ProducerTemplate
import org.apache.camel.impl.SimpleRegistry
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This is a runtime version of configuration and beans. A place
 * to register instances of objects for easy access from other parts
 * in the application.
 * @author daviddale
 */
public class SystemRegistry {

    // singleton instance
    static SystemRegistry instance;

    ProducerTemplate producerTemplate;

    //
    Map<String,Class> registered = new HashMap<String,Class>();
    CamelContext context = null;
    SimpleRegistry camelRegistry = null;

    Logger log = LoggerFactory.getLogger( SystemRegistry.class )

    
    private SystemRegistry(){
        producerTemplate = getContext().createProducerTemplate()
    }

    protected CamelContext getContext(){
        if( context == null ){
            context = new DefaultCamelContext();
            context.getExecutorServiceManager().getDefaultThreadPoolProfile().setMaxPoolSize(50)
        }
        return context;
    }

    public ProducerTemplate getProducerTemplate(){
        return producerTemplate
    }
    
    public SystemRegistry addRoutes(RouteBuilder builder){
        
        getContext().addRoutes( builder );
        return this;
        
    }
    
    public SystemRegistry bind(String key, Object object){
        
        if( camelRegistry == null ){
            camelRegistry = new SimpleRegistry();
            getContext().setRegistry( camelRegistry );
        }
        
        camelRegistry.put( key,object );
        
        return this;
    }
    
    public Object lookupObject( String key ){
        return camelRegistry.lookup( key )
    }
    
    public static SystemRegistry get(){
        
        if( instance ==  null ){
            instance = new SystemRegistry();
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
                log.error("Problem registering commands...", e )
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
