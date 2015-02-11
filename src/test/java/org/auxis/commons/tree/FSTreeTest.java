package org.auxis.commons.tree;

import static org.auxis.commons.tree.Selector.selector;

import java.io.File;

import org.auxis.commons.tree.util.StreamTreeBuilder;
import org.auxis.commons.tree.util.TreeConsoleFormatter;
import org.auxis.commons.tree.util.TreeTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FSTreeTest
{
    private final static Logger LOG = LoggerFactory.getLogger( FSTreeTest.class );

    private final TreeTools TOOLS = new TreeTools();
    private final TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();

    private void collect( TreeBuilder builder, File base )
    {
        for ( File f : base.listFiles() )
        {
            if ( f.isHidden() )
                continue;
            TreeBuilder sub = builder.branch( selector( f.getName() ) );
            if ( f.isDirectory() )
                collect( sub, f );
            else
            {
                new StreamTreeBuilder( sub ).add( f );
            }
        }
    }
}
