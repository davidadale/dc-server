/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.flow7.dc.server;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 * @author daviddale
 */
public class SimpleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //from("{{orders.inbox}}").throttle(3).to("{{orders.outbox}}");
        from("{{orders.inbox}}").threads().to("{{orders.outbox}}");
        //from("{{orders}}").to("{{destination}}");
    }
    
}

