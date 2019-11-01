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
    public static final String CHARSET_NAME = "UTF-8";
    private final String m_messageDigestAlgorithm;

    private Comparator<Tree> comparator = new HashUtil.FingerprintTreeComparator();

    public TreeSession(String messageDigestAlgorithm)
    {
        m_messageDigestAlgorithm = messageDigestAlgorithm;
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

        if ( tree.branches().length == 1 && tree.value().equals( tree.branches()[0].value() ) )
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
        return ( tree.branches().length == 1 && tree.value().equals( tree.branches()[0].value() ) );
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

    public Tree createTree( Selector selector, TreeValue value, Tree[] subs, Tag tag )
    {
        if (selector == null) {
            selector = Selector.selector( value.hash() );
        }
        return new InMemoryTreeImpl( selector, value, subs, tag );
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

    public Tree createTree( Selector selector, TreeValue value )
    {
        return createTree( selector, value, new Tree[0], Tag.tag() );
    }

    public Comparator<? super Tree> getComparator()
    {
        return comparator;
    }
}
