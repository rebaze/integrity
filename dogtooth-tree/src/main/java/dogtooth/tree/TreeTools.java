/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package dogtooth.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import dogtooth.tree.internal.InMemoryTreeBuilderImpl;

public class TreeTools {

    private static final Logger LOG = LoggerFactory.getLogger( TreeTools.class );
    final private PrintStream m_out;
    private long m_dataAmountRead = 0;

    public TreeTools() {
        this( System.out );
    }

    public TreeTools( PrintStream ps ) {
        m_out = ps;
    }

    public File store( Tree tree ) throws IOException {
        // persist tree
        File f = File.createTempFile( "tree", ".xml" );
        return store( tree, f );
    }

    public File store( Tree tree, File f ) throws IOException {
        // persist tree
        XStream xstream = new XStream();
        xstream.toXML( tree, new FileOutputStream( f ) );
        LOG.debug( "Stored tree to " + f.getAbsolutePath() );
        return f;
    }

    public Tree load( File f ) {
        XStream xstream = new XStream();
        return (Tree) xstream.fromXML( f );
    }

    public Tree load( String locationClasspath ) {
        XStream xstream = new XStream();
        return (Tree) xstream.fromXML( this.getClass().getResourceAsStream( locationClasspath ) );
    }

    /**
	 */
    public Tree compare( Tree left, Tree right ) {
        return compare( new TreeIndex( left ), new TreeIndex( right ) );
    }

    /**
	 */
    public Tree compare( TreeIndex left, TreeIndex right ) {
        TreeBuilder target = new InMemoryTreeBuilderImpl( "Difference [" + left.selector() + " ] and [" + right.selector() + " ]-" );
        compare( target, left, right );
        return target.seal();
    }


    /**
	 * 
	 */
    private void compare( TreeBuilder collector, TreeIndex left, TreeIndex right ) {
        // Unfold "empty" elements.
        if( left == null ) {
            for( TreeIndex tree : right.branches() ) {
                TreeBuilder mod = collector.branch( "[ADDED] " + tree.selector() );
                compare( mod, null, tree );
            }
            return;
        }
        if( right == null ) {
            for( TreeIndex tree : left.branches() ) {
                TreeBuilder mod = collector.branch( "[REMOVED] " + tree.selector() );
                compare( mod, tree, null );
            }
            return;
        }

        if( !left.fingerprint().equals( right.fingerprint() ) ) {
            // compare next level:
            TreeBuilder modification = collector.branch( "[MOD] " + right.selector() );
            for( TreeIndex tree : left.branches() ) {
                // first check if tree is a selectable node or not:
                if( tree.selectable() ) {
                    TreeIndex origin = right.select( tree.selector() );
                    if( origin == null ) {
                        // Deleted element:
                        TreeBuilder removed = modification.branch( "[REMOVED] " + tree.selector() );
                        // Not so sure..
                        compare( removed, tree, origin );
                    } else {
                        // compare content:
                        compare( modification, tree, origin );
                    }

                } else {
                    throw new TreeException( "Item " + tree + " is not selectable." );
                }
            }
            // find new ones:
            for( TreeIndex tree : right.branches() ) {
                // first check if tree is a selectable node or not:
                if( tree.selectable() ) {
                    TreeIndex origin = left.select( tree.selector() );
                    if( origin == null ) {
                        // New element:
                        TreeBuilder added = modification.branch( "[ADDED] " + tree.selector() );
                        compare( added, origin, tree );
                    } else {
                        // already worked on.
                    }

                } else {
                    throw new TreeException( "Item " + tree + " is not selectable." );
                }
            }
        }

    }

    public void displayTree( int depth, Tree dbHash ) {
        if( depth == 0 )
            m_out.println( " ---- TREE ----------" );
        m_out.print( "+" );
        for( int i = 0; i < depth; i++ ) {
            m_out.print( "--" );
        }
        m_out.println( " " + dbHash.toString() );
        depth++;
        Tree[] elements = dbHash.branches();
        int count = (elements.length < 10) ? elements.length : 10;
        for( int i = 0; i < count; i++ ) {
            displayTree( depth, elements[i] );
        }
    }

    public void prettyPrint( int depth, Tree dbHash ) {
        if( depth == 0 )
            m_out.println( " ---- TREE: Total size: " + dbHash.effectiveSize() + " /Data read in session: " + prettyDataSize());
        m_out.print( "+" );
        for( int i = 0; i < depth; i++ ) {
            m_out.print( "--" );
        }
        m_out.println( " " + dbHash.selector() );
        depth++;
        for( Tree sub : dbHash.branches() ) {
            prettyPrint( depth, sub );
        }
    }

    public String prettyDataSize() {
        if ( m_dataAmountRead < (1024 * 1024)) {
            return "" + (m_dataAmountRead / 1024 ) + "k";
        }else if (m_dataAmountRead < (1024 * 1024 * 1024)) {
            return (m_dataAmountRead / 1024 / 1024) + "M";
        }else {
            return (float)(m_dataAmountRead / 1024f / 1024f / 1024f) + "G";
        }
        
    }
 

    public void addStreams( final TreeBuilder builder, final InputStream is )
        throws IOException {
        byte[] bytes = new byte[1024];
        int numRead = 0;
        while( (numRead = is.read( bytes )) >= 0 ) {
            builder.add( Arrays.copyOf( bytes, numRead ) );
            m_dataAmountRead += numRead;
        }
    }

    public void addStreams( final TreeBuilder builder, final File f ) {
        try {
            InputStream is = new FileInputStream( f );
            try {
                addStreams( builder, is );
            }
            finally {
                if( is != null )
                    try {
                        is.close();
                    }
                    catch( IOException e ) {
                        LOG.warn("Problem closing file " + f.getAbsolutePath(),e);
                    }
            }
        }
        catch( IOException ioE ) {
            throw new TreeException( "Problem reading file " + f.getAbsolutePath() + " contents.", ioE );
        }
    }
}
