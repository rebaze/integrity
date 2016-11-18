/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.api;

import org.rebaze.integrity.tree.internal.StaticTreeBuilder;

/**
 * The way for creating Trees.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public interface TreeBuilder
{

    /**
     * Add data to the current tree. Only possible before {@link TreeBuilder}.seal() is called.
     *
     * @param bytes
     *            data to be hashed.
     *
     * @return this
     */
    TreeBuilder add( byte[] bytes );

    /**
     * Change selector. Useful when selector is computed while adding data to the tree. Only
     * possible before {@link TreeBuilder}.seal() is called.
     *
     * @param selector
     *            to be used from here on.
     *
     * @return this
     */
    TreeBuilder selector( Selector selector );

    /**
     * Create a new branch (Tree) with given selector. Only possible before {@link TreeBuilder}
     * .seal() is called.
     *
     * @param selector
     *            to be used on the newly created branch.
     *
     * @return A new sub tree.
     */
    TreeBuilder branch( Selector selector );

    /**
     * Create a new branch (Tree) with given selector. Only possible before {@link TreeBuilder}
     * .seal() is called.
     *
     * @param subtree
     *            tree to be added as branch. Note that this branch of cause will be unmodifiable.
     *
     * @return An instance of {@link StaticTreeBuilder}
     */
    TreeBuilder branch( Tree subtree );

    /**
     * Tag this tree. Will overwrite previous tags.
     *
     * @param tag
     *
     * @return this
     */
    TreeBuilder tag( Tag tag );

    /**
     * Close this TreeBuilder and seal the entire subtree. This actually gives you the desired
     * inmodifiable datastructure.
     *
     * @return A new instance representing the tree you've built.
     */
    Tree seal();

    default TreeBuilder add ( String data) {
        return add(data.getBytes());
    }

    default TreeBuilder add (Long l) {
        return add(l.toString().getBytes());
    }

    default TreeBuilder add (Integer i) {
        return add(i.toString().getBytes());
    }

    default TreeBuilder add (boolean b) {
        return add( (b) ? 1 : 0 );
    }

    default TreeBuilder selector( String s ) {
        return selector (Selector.selector (s));
    }

    default TreeBuilder branch( String s ) {
        return branch (Selector.selector (s));
    }
}
