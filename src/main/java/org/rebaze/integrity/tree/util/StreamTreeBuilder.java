/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.rebaze.integrity.tree.api.Selector;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.TreeException;
import org.rebaze.integrity.tree.api.Tag;

public class StreamTreeBuilder implements TreeBuilder
{
    private long m_dataAmountRead = 0L;
    final private TreeBuilder m_delegate;

    public StreamTreeBuilder( final TreeBuilder delegate )
    {
        m_delegate = delegate;
    }

    public StreamTreeBuilder add( final InputStream is )
        throws IOException
    {
        byte[] bytes = new byte[1024];
        int numRead = 0;
        while ( ( numRead = is.read( bytes ) ) >= 0 )
        {
            m_delegate.add( Arrays.copyOf( bytes, numRead ) );
            m_dataAmountRead += numRead;
        }
        return this;
    }

    public StreamTreeBuilder add( final File f )
    {
        try( InputStream is = new FileInputStream( f ))
        {
            add( is );
        } catch ( IOException ioE )
        {
            throw new TreeException( "Problem reading file " + f.getAbsolutePath() + " contents.", ioE );
        }
        return this;
    }

    public long getDataRead()
    {
        return m_dataAmountRead;
    }

    public void reset()
    {
        m_dataAmountRead = 0L;
    }

    @Override
    public StreamTreeBuilder add( byte[] bytes )
    {
        m_delegate.add( bytes );
        return this;
    }

    @Override
    public StreamTreeBuilder selector( Selector selector )
    {
        m_delegate.selector( selector );
        return this;
    }

    @Override
    public StreamTreeBuilder branch( Selector selector )
    {
        // TODO: Maybe disable this by throwing an exception? Branching from StreamTreeBuilder is pretty unusually as it means you add raw data to an intermediate tree. " );
        return new StreamTreeBuilder( m_delegate.branch( selector ) );
    }

    @Override
    public StreamTreeBuilder branch( Tree subtree )
    {
        // TODO: Maybe disable this by throwing an exception? Branching from StreamTreeBuilder is pretty unusually as it means you add raw data to an intermediate tree. " );
        return new StreamTreeBuilder( m_delegate.branch( subtree ) );
    }

    @Override
    public StreamTreeBuilder tag( Tag tag )
    {
        m_delegate.tag( tag );
        return this;
    }

    @Override
    public Tree seal()
    {
        return m_delegate.seal();
    }
}
