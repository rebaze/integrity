/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package com.rebaze.trees.core;

import com.rebaze.trees.core.internal.StaticTreeBuilder;

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
    public TreeBuilder add( byte[] bytes );

    /**
     * Change selector. Useful when selector is computed while adding data to the tree. Only
     * possible before {@link TreeBuilder}.seal() is called.
     *
     * @param selector
     *            to be used from here on.
     *
     * @return this
     */
    public TreeBuilder selector( Selector selector );

    /**
     * Create a new branch (Tree) with given selector. Only possible before {@link TreeBuilder}
     * .seal() is called.
     *
     * @param selector
     *            to be used on the newly created branch.
     *
     * @return A new sub tree.
     */
    public TreeBuilder branch( Selector selector );

    /**
     * Create a new branch (Tree) with given selector. Only possible before {@link TreeBuilder}
     * .seal() is called.
     *
     * @param subtree
     *            tree to be added as branch. Note that this branch of cause will be unmodifiable.
     *
     * @return An instance of {@link StaticTreeBuilder}
     */
    public TreeBuilder branch( Tree subtree );

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
    public Tree seal();

}
