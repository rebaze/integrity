/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.api;

import org.rebaze.integrity.tree.internal.HashUtil;
import org.rebaze.integrity.tree.internal.StaticTreeBuilder;
import org.rebaze.integrity.tree.util.StreamTreeBuilder;
import org.rebaze.integrity.tree.internal.InMemoryTreeBuilderImpl;
import org.rebaze.integrity.tree.internal.InMemoryTreeImpl;
import org.rebaze.integrity.tree.internal.operators.IntersectTreeCombiner;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

/**
 * Set of tools for this API.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class TreeSession
{
    private static final String DEFAULT_HASH_ALOGO = "SHA-1";
    public static final String CHARSET_NAME = "UTF-8";
    private String m_messageDigestAlgorithm = DEFAULT_HASH_ALOGO;

    private Comparator<Tree> comparator = new HashUtil.FingerprintTreeComparator();

    public TreeSession()
    {
    }

    /**
     * counts the total number of nodes of this tree.
     *
     * @param tree input
     * @return number of sub trees.
     */
    public static long nodes( Tree tree )
    {
        int total = 1;
        for ( Tree sub : tree.branches() )
        {
            total += nodes( sub );
        }
        return total;
    }

    /**
     * counts the total number of leafs of this tree.
     *
     * @param tree input
     * @return number of leafs.
     */
    public static long leafs( Tree tree )
    {
        int total = 0;
        for ( Tree sub : tree.branches() )
        {
            if ( sub.branches().length == 0 )
            {
                total++;
            }
            else
            {
                total += leafs( sub );
            }
        }
        return total;
    }

    public static boolean isRaw( Tree tree )
    {
        if ( tree.branches().length == 0 )
            return true;

        if ( tree.branches().length == 1 && tree.fingerprint().equals( tree.branches()[0].fingerprint() ) )
        {
            return isRaw( tree.branches()[0] );
        }
        else
        {
            return false;
        }
    }

    public static boolean isWrapper( Tree tree )
    {
        return ( tree.branches().length == 1 && tree.fingerprint().equals( tree.branches()[0].fingerprint() ) );
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
        return new InMemoryTreeImpl( selector, hashValue, subs, tag );
    }

    public TreeSession setDigestAlgorithm( String algo )
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
            throw new TreeException( "Problem loading digest with algorthm " + m_messageDigestAlgorithm,e );
        }
    }

    public TreeBuilder createStaticTreeBuilder( Tree tree )
    {
        return new StaticTreeBuilder( tree, this );
    }

    public static TreeIndex wrapAsIndex( Tree tree )
    {
        if ( tree instanceof TreeIndex )
        {
            return ( TreeIndex ) tree;
        }
        else
        {
            return new TreeIndex( tree );
        }
    }

    public static byte[] asByteArray( String s )
    {
        try
        {
            return s.getBytes( CHARSET_NAME );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new IllegalArgumentException( "Is not " + CHARSET_NAME + " ??" );
        }
    }

    public Tree find( Tree base, Tree subtree )
    {
        //TreeBuilder builder = createTreeBuilder();
        // basically copy the whole input tree but erase all leafs
        // that do not mach the subtree.

        return new IntersectTreeCombiner( this ).combine( base, subtree );
    }

    public Tree createTree( Selector selector, String hashValue )
    {
        return createTree( selector, hashValue, new Tree[0], Tag.tag() );
    }

    public Comparator<? super Tree> getComparator()
    {
        return comparator;
    }
}
