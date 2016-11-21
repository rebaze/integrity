/*
 * Copyright (c) 2012-2015 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.internal;

import java.security.MessageDigest;
import java.util.*;

import org.rebaze.integrity.tree.api.*;

/**
 * Default implementation not really suitable for very large trees but fast and simple.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class InMemoryTreeBuilderImpl implements TreeBuilder
{
    public static final String FIXED_EMPTY = "0000000000000000000000000000000000000000";

    final private MessageDigest m_digest;
    private Tree m_hash;
    private boolean noData = true;
    private boolean m_sealed = false;

    final private Map<Selector, TreeBuilder> m_subItems;

    private Selector m_selector;
    private Tag m_tag;
    private final TreeSession m_tools;

    public InMemoryTreeBuilderImpl( TreeSession tools )
    {
        m_tools = tools;
        m_digest = m_tools.createMessageDigest();
        //m_sub = new ArrayList<TreeBuilder>();
        m_subItems = new HashMap<>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.auxis.commons.tree.internal.TreeBuilder#add(byte[])
     */
    @Override
    synchronized public TreeBuilder add( byte[] bytes )
    {
        if (m_subItems.size() > 0) {
            throwMixedDataBranchException();
        }
        addUnguarded( bytes );
        return this;
    }

    private void addUnguarded( byte[] bytes )
    {
        if (bytes.length > 0) {
            noData = false;
            verifyTreeNotSealed();
            m_digest.update( bytes );
        }
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
            //defaultSelector();

            if (noData && m_subItems.size() == 0) {
                // Use fixed value empty tree:
                m_hash = m_tools.createTree(m_selector,FIXED_EMPTY,new Tree[0],m_tag);
            }else if (noData && m_subItems.size() == 1) {
                // reuse sub tree fingerprint.
                Tree underneath = m_subItems.values().iterator().next().seal();
                m_hash = m_tools.createTree(m_selector,underneath.fingerprint(),new Tree[]{underneath},m_tag);
            }else {
                List<Tree> subHashes = new ArrayList<Tree>(m_subItems.keySet().size());
                for (Selector c : m_subItems.keySet()) {
                    Tree sealed = m_subItems.get(c).seal();
                    // Empty trees are not sealed. This is empty-tree erasure.
                    if (!isEmptyTree(sealed)) {
                        subHashes.add(sealed);
                    }
                }
                Collections.sort( subHashes, this.m_tools.getComparator() );
                for (Tree c : subHashes) {
                    addUnguarded(c.fingerprint().getBytes());
                }
                m_hash = m_tools.createTree(m_selector, HashUtil.convertToHex(m_digest.digest()), subHashes.toArray(new Tree[subHashes.size()]), m_tag);
            }
            m_sealed = true;
            resetMembers();
        }
        return m_hash;
    }

    private boolean isEmptyTree(Tree tree) {
        return tree.fingerprint().equals(FIXED_EMPTY);
    }

    private void defaultSelector()
    {
        if ( m_selector == null )
        {
            // use an empty selector:
            m_selector = Selector.DEFAULT;
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
        verifyNoData();
        verifyTreeNotSealed();
        TreeBuilder c = m_subItems.get( selector );
        if ( c == null )
        {
            c = m_tools.createTreeBuilder().selector( selector );
            m_subItems.put( selector, c );
        }

        return c;
    }

    private void verifyNoData()
    {
        if (!noData) {
            throwMixedDataBranchException( );
        }
    }

    private void throwMixedDataBranchException( )
    {
        throw new TreeException( "Mixed nodes of data and branches are discouraged. It makes merges really not easy to judge by."  );
    }

    @Override
    public TreeBuilder branch( Tree subtree )
    {
        verifyNoData();
        verifyTreeNotSealed();
        TreeBuilder c = m_subItems.get( subtree.selector() );

        if ( c == null )
        {
            c = m_tools.createStaticTreeBuilder( subtree );
            m_subItems.put( subtree.selector(), c );
        }else {
            if (subtree.branches().length == 0 && c.seal().fingerprint().equals( subtree.fingerprint() )) {
                // don't merge when fingerprint matches:
                return c;

            }else
            {
                // need to merge c & subtree
                TreeBuilder unified = m_tools.createTreeBuilder();
                unified.selector( subtree.selector() );
                include( unified, c.seal() );
                include( unified, subtree );
                m_subItems.put( subtree.selector(), unified );
            }
        }

        return c;
    }

    private void include( TreeBuilder collector, Tree left )
    {
        // only when one merges in trees with competing data:

        if ( left.branches().length == 0 ) {
            // Merge competing data to a new anonymous tree.
            // Note that this produces fingerprint based on another fingerprint:
            collector.add( left.fingerprint().getBytes() ).tag( Tag.tag( "MERGED" ) );
        }else
        {
            // auto include:
            TreeBuilder subBuilder = collector.branch( left.selector() );
            for ( Tree tree : left.branches() )
            {
                include( subBuilder, tree );
            }
        }
    }

    private void verifyTreeNotSealed()
    {
        if ( m_sealed )
        {
            throw new TreeAlreadySealedException( "No modification on a sealed tree!" );
        }
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
