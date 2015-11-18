/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package com.rebaze.trees.core;

/**
 * The central element for this library. Actually you will get many instances of {@link Tree}. They
 * are all unmodifiable.
 * 
 * Trees are made of a selector which identifies it. Note: selector does not have to be globally
 * unique but unique for its parent.
 * 
 * A tree then has a fingerprint (also call it "hash" which stands for the data of all its sub
 * branches.
 * 
 * Subbranches are accessible from here to. They may get initialized lazily.
 * 
 * 
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public interface Tree
{

    /**
     * Hash value of all its sub branches. Can also be called a hash. Default implementations may
     * use a SHA-1.
     * 
     * @return the hash value of this tree.
     */
    String fingerprint();

    /**
     * Identification of this tree for the parent. Selectors are not global. Actually used to create
     * indexes.
     * 
     * @return string version of the selector.
     */
    Selector selector();

    /**
     * Sub branches of this tree. May be empty or a list of sub trees. All sub branches are supposed
     * to have unique selectors.
     *
     * @return List of sub trees.
     */
    Tree[] branches();

    /**
     * 
     * @return Tags for this tree.
     */
    Tag tags();

}
