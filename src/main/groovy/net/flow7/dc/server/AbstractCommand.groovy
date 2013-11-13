package net.flow7.dc.server

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.Options
import org.apache.commons.cli.PosixParser

/**
 * Created with IntelliJ IDEA.
 * User: daviddale
 * Date: 11/9/13
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class AbstractCommand implements Command{

    protected CommandLine getCommandLine(String line, Options options){
        String[] args = line.split(" ");
        CommandLineParser parser = new PosixParser();
        return parser.parse(options, args);
    }

}
