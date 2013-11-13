package net.flow7.dc.server

/**
 * Created with IntelliJ IDEA.
 * User: daviddale
 * Date: 11/9/13
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
class ServerException extends RuntimeException {

    public ServerException(String msg){
        super( msg )
    }

    public ServerException(String msg, Throwable e){
        super( msg, e )
    }
}
