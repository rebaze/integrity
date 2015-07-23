package org.auxis.commons.tree;

import static com.rebaze.commons.tree.Selector.selector;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rebaze.commons.tree.TreeBuilder;
import com.rebaze.commons.tree.util.DefaultTreeSessionFactory;
import com.rebaze.commons.tree.util.StreamTreeBuilder;
import com.rebaze.commons.tree.util.TreeConsoleFormatter;
import com.rebaze.commons.tree.util.TreeSession;

public class FSTreeTest
{
    private final static Logger LOG = LoggerFactory.getLogger( FSTreeTest.class );

    private TreeSession session =  new DefaultTreeSessionFactory().create();
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
