package org.auxis.commons.tree.fs;

import org.auxis.commons.tree.Tree;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.util.StreamTreeBuilder;
import org.auxis.commons.tree.util.TreeConsoleFormatter;
import org.auxis.commons.tree.util.TreeTools;

import java.io.File;

import static org.auxis.commons.tree.Selector.selector;

/**
 *
 */
public class FSTreeRunner
{
    private final static TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();

    public static void main( String[] args )
    {
        args = new String[] { "/Users/tonit/devel/auxis", "/Users/tonit/devel/auxis" };
        FSTreeRunner runner = new FSTreeRunner();

        TreeTools treetools = TreeTools.treeTools();

        if ( args.length >= 2 )
        {
            Tree treeLeft = new FSTreeRunner().collect( treetools.createTreeBuilder(), new File( args[0] ) ).seal();
            Tree treeRight = new FSTreeRunner().collect( treetools.createTreeBuilder(), new File( args[1] ) ).seal();
            Tree diff = treetools.diff( treeLeft, treeRight );
            FORMAT.prettyPrint( 0, diff );
        }
        else
        {
            System.err.println( "Specify <Folder1> <Folder2>" );
        }
    }

    public TreeBuilder collect( TreeBuilder builder, File base )
    {
        builder.selector( selector( base.getName() ) );
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
        return builder;
    }

}

