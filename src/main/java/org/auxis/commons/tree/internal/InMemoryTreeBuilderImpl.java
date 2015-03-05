/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree.internal;

import java.security.MessageDigest;
import java.util.*;

import org.auxis.commons.tree.Selector;
import org.auxis.commons.tree.StaticTreeBuilder;
import org.auxis.commons.tree.Tree;
import org.auxis.commons.tree.TreeAlreadySealedException;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.TreeException;
import org.auxis.commons.tree.annotated.Tag;
import org.auxis.commons.tree.util.TreeTools;

/**
 * Default implementation not really suitable for very large trees but fast and simple.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class InMemoryTreeBuilderImpl implements TreeBuilder
{
    final private MessageDigest m_digest;
    private Tree m_hash;
    private boolean m_sealed = false;

    //final private List<TreeBuilder> m_sub;
    final private Map<Selector, TreeBuilder> m_subItems;

    private Selector m_selector;
    private Tag m_tag;
    private final TreeTools m_tools;

    public InMemoryTreeBuilderImpl( TreeTools tools )
    {
        m_tools = tools;
        m_digest = m_tools.createMessageDigest();
        //m_sub = new ArrayList<TreeBuilder>();
        m_subItems = new HashMap<Selector, TreeBuilder>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.auxis.commons.tree.internal.TreeBuilder#add(byte[])
     */
    @Override
    synchronized public TreeBuilder add( byte[] bytes )
    {
        verifyTreeNotSealed();
        m_digest.update( bytes );
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.auxis.commons.tree.internal.TreeBuilder#seal()
     */
    @Override
    synchronized public Tree seal()
    {
        if ( !m_sealed )
        {
            defaultSelector();

            List<Tree> subHashes = new ArrayList<Tree>( m_subItems.keySet().size() );

            for ( Selector c : m_subItems.keySet() )
            {
                // strip duplicate trees. Nope.
                Tree sealed = m_subItems.get( c ).seal();
                // if (!subHashes.contains( sealed ))
                subHashes.add( sealed );
            }

            sort( subHashes );

            for ( Tree c : subHashes )
            {
                add( c.fingerprint().getBytes() );
            }
            m_hash = m_tools.createTree( m_selector, convertToHex( m_digest.digest() ), subHashes.toArray( new Tree[subHashes.size()] ), m_tag );
            m_sealed = true;
            resetMembers();
        }
        return m_hash;
    }

    private void sort( List<Tree> subHashes )
    {

        Collections.sort( subHashes, new Comparator<Tree>()
        {
            @Override public int compare( Tree left, Tree right )
            {
                return left.fingerprint().compareTo( right.fingerprint() );
            }
        } );
    }

    private void defaultSelector()
    {
        if ( m_selector == null )
        {
            // use an empty selector:
            m_selector = Selector.selector( "" );
        }
    }

    private void resetMembers()
    {
        //m_sub.clear();
        m_subItems.clear();
        m_selector = null;
        m_digest.reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.auxis.commons.tree.internal.TreeBuilder#childCollector(java.lang.String)
     */
    @Override
    synchronized public TreeBuilder branch( Selector selector )
    {
        verifyTreeNotSealed();
        TreeBuilder c = m_subItems.get( selector );
        if ( c == null )
        {
            c = m_tools.createTreeBuilder().selector( selector );
            m_subItems.put( selector, c );
        }

        return c;
    }

    @Override
    public TreeBuilder branch( Tree subtree )
    {
        verifyTreeNotSealed();
        TreeBuilder c = m_subItems.get( subtree.selector() );
        if ( c == null )
        {
            c = new StaticTreeBuilder( subtree );
            m_subItems.put( subtree.selector(), c );        }

        return c;
    }

    private void verifyTreeNotSealed()
    {
        if ( m_sealed )
        {
            throw new TreeAlreadySealedException( "No modification on a sealed tree!" );
        }
    }

    private static String convertToHex( byte[] data )
    {
        StringBuilder buf = new StringBuilder();
        for ( int i = 0; i < data.length; i++ )
        {
            int halfbyte = ( data[i] >>> 4 ) & 0x0F;
            int two_halfs = 0;
            do
            {
                if ( ( 0 <= halfbyte ) && ( halfbyte <= 9 ) )
                {
                    buf.append( ( char ) ( '0' + halfbyte ) );
                }
                else
                {
                    buf.append( ( char ) ( 'a' + ( halfbyte - 10 ) ) );
                }
                halfbyte = data[i] & 0x0F;
            }
            while ( two_halfs++ < 1 );
        }
        return buf.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.auxis.commons.tree.internal.TreeBuilder#setSelector(java.lang.String)
     */

    @Override
    synchronized public TreeBuilder selector( Selector selector )
    {
        verifyTreeNotSealed();
        m_selector = selector;
        return this;
    }

    @Override
    public TreeBuilder tag( Tag tag )
    {
        m_tag = tag;
        return this;
    }

}
