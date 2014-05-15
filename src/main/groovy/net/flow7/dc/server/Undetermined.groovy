package net.flow7.dc.server

/**
 * Created by daviddale on 5/12/14.
 */
class Undetermined extends Files {

    List<String> ignoreDirs


    public Undetermined(){
        ignoreDirs = new LinkedList<String>()
        ignoreDirs.addAll( new Windows().ignoreDirs )
        ignoreDirs.addAll( new Mac().ignoreDirs )
    }


    @Override
    List getIgnoreDirs() {
        return ignoreDirs
    }

    @Override
    String getStartLocation() {
        return "/"
    }

    @Override
    String getSystemName() {
        return "Undetermined"
    }
}
