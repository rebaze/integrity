/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package dogtooth.tree.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import dogtooth.tree.Tree;
import dogtooth.tree.TreeBuilder;
import dogtooth.tree.TreeException;

/**
 * Set of tools for this API.
 * 
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public class TreeTools {

    private static final Logger LOG = LoggerFactory.getLogger( TreeTools.class );
    private long m_dataAmountRead = 0L;
       
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
