/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree.util;

import dagger.ObjectGraph;
import org.auxis.commons.tree.*;
import org.auxis.commons.tree.annotated.Tag;
import org.auxis.commons.tree.internal.InMemoryTreeBuilderImpl;
import org.auxis.commons.tree.internal.InMemoryTreeImpl;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Set of tools for this API.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class TreeSession
{
    private static final String DEFAULT_HASH_ALOGO = "SHA-1";
    private String m_messageDigestAlgorithm = DEFAULT_HASH_ALOGO;

    private static ObjectGraph INSTANCE;

    @Inject @Named( "delta" ) TreeCombiner deltaCombiner;

    @Inject @Named( "diff" ) TreeCombiner diffCombiner;

    @Inject @Named( "intersect" ) TreeCombiner intersectCombiner;

    @Inject @Named( "union" ) TreeCombiner unionCombiner;

    @Inject @Named( "substruct" ) TreeCombiner substructCombiner;

    public static TreeSession getSession()
    {
        if ( INSTANCE == null )
        {
            INSTANCE = ObjectGraph.create( new DefaultTreeModule() );
        }
        return INSTANCE.get( TreeSession.class );
    }

    public static long nodes( Tree tree )
    {
        int total = 1;
        for ( Tree sub : tree.branches() )
        {
            total += nodes( sub );
        }
        return total;
    }

    public static boolean isRaw( Tree tree )
    {
        if (tree.branches().length == 0) return true;

        if (tree.branches().length == 1 && tree.fingerprint().equals(tree.branches()[0].fingerprint() ))
        {
            return isRaw( tree.branches()[0] );
        }else
        {
            return false;
        }
    }

    public static boolean isWrapper(Tree tree) {
        return (tree.branches().length == 1 && tree.fingerprint().equals(tree.branches()[0].fingerprint() ));
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
            throw new TreeException( "Problem loading digest with algorthm." );
        }
    }

    public Tree diff( Tree left, Tree right )
    {
        return diffCombiner.combine( left, right );
    }

    public Tree delta( Tree left, Tree right )
    {
        return deltaCombiner.combine( left, right );
    }

    public Tree union( Tree left, Tree right )
    {
        return unionCombiner.combine( left, right );
    }

    public Tree substract( Tree left, Tree right )
    {
        return substructCombiner.combine( left, right );
    }

    public Tree intersection( Tree left, Tree right )
    {
        return intersectCombiner.combine( left, right );
    }

    public TreeBuilder createStaticTreeBuilder(Tree tree) {
        return new StaticTreeBuilder(tree,this);
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

    public static byte[] asByteArray( String s) {
        try
        {
            return s.getBytes( "UTF-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new IllegalArgumentException( "Is not UTF-8 ??" );
        }
    }

    TreeSession() { }

    public Tree find( Tree base, Tree subtree )
    {
        TreeBuilder builder = createTreeBuilder();
        // basically copy the whole input tree but erase all leafs
        // that do not mach the subtree.

        return intersection( base, subtree );

    }
}
