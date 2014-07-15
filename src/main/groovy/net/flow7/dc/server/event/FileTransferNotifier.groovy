package net.flow7.dc.server.event

import net.flow7.dc.server.ext.SystemScanner

import org.apache.camel.management.event.ExchangeFailedEvent
import org.apache.camel.support.EventNotifierSupport


class FileTransferNotifier extends EventNotifierSupport {

    public FileTransferNotifier(){
        setIgnoreCamelContextEvents(true)
        setIgnoreExchangeCompletedEvent(true)
        setIgnoreExchangeCreatedEvent(true)
        setIgnoreExchangeEvents(true)
        setIgnoreExchangeFailedEvents(false)
        setIgnoreExchangeRedeliveryEvents(false)
        setIgnoreExchangeSendingEvents(true)
        setIgnoreExchangeSentEvents(true)
        setIgnoreRouteEvents(true)
        setIgnoreServiceEvents(true)

    }

    @Override
    void notify(EventObject event) throws Exception {

        if( event instanceof ExchangeFailedEvent ){
            SystemScanner.get().fileTransfersFailed++
        }
    }

    @Override
    boolean isEnabled(EventObject event) {
        return true
    }
}
