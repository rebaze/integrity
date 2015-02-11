/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree.util;

import static org.auxis.commons.tree.Selector.selector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.auxis.commons.tree.Selector;
import org.auxis.commons.tree.Tree;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.TreeException;
import org.auxis.commons.tree.TreeIndex;
import org.auxis.commons.tree.annotated.Tag;
import org.auxis.commons.tree.internal.InMemoryTreeBuilderImpl;
import org.auxis.commons.tree.internal.InMemoryTreeImpl;

/**
 * Set of tools for this API.
 * 
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public class TreeTools
{
    private static final String DEFAULT_HASH_ALOGO = "SHA-1";
    private String m_messageDigestAlgorithm = DEFAULT_HASH_ALOGO;

    public long nodes( Tree tree )
    {
        int total = 1;
        for ( Tree sub : tree.branches() )
        {
            total += nodes( sub );
        }
        return total;
    }

    public TreeBuilder createTreeBuilder()
    {
        return new InMemoryTreeBuilderImpl( this );
    }

    public StreamTreeBuilder createStreamTreeBuilder()
    {
        return new StreamTreeBuilder( createTreeBuilder() );
    }

    public StreamTreeBuilder createStreamTreeBuilder( TreeBuilder delegate )
    {
        return new StreamTreeBuilder( delegate );
    }

    public Tree createTree( Selector selector, String hashValue, Tree[] subs, Tag tag )
    {
        return new InMemoryTreeImpl( this, selector, hashValue, subs, tag );
    }

    public TreeTools setDigestAlgorithm( String algo )
    {
        m_messageDigestAlgorithm = algo;
        return this;
    }

    public MessageDigest createMessageDigest()
    {
        try
        {
            return MessageDigest.getInstance( m_messageDigestAlgorithm );
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new TreeException( "Problem loading digest with algorthm." );
        }
    }

    /**
     */
    public Tree compare( Tree left, Tree right )
    {
        return compare( new TreeIndex( left ), new TreeIndex( right ) );
    }

    /**
     */
    public Tree compare( TreeIndex left, TreeIndex right )
    {
        TreeBuilder target = createTreeBuilder().selector( selector( "[" + left.selector() + " ] and [" + right.selector() + " ]" ) );
        target.tag( Tag.tag( "DIFF" ) );
        TreeCompare.compare( target, left, right );
        return target.seal();
    }
}
