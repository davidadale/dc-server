package net.flow7.dc.server

import org.apache.commons.cli.Options


class WriteIndexCommand extends AbstractCommand{

    Options options

    @Override
    void hint() {

    }

    @Override
    boolean process(String line) throws Exception {

        SystemRegistry registry = SystemRegistry.get()

        IndexWriter writer = registry.getCamelRegistry().lookup("index") as IndexWriter

        File index = writer.getIndexFile()
        if( !index.exists() ){
            index.createNewFile()
        }

        index.text = writer.getJson()

        return true
    }
}
