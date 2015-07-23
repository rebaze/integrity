/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package com.rebaze.commons.tree.operators;

import static com.rebaze.commons.tree.util.TreeSession.wrapAsIndex;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.rebaze.commons.tree.*;
import com.rebaze.commons.tree.annotated.Tag;
import com.rebaze.commons.tree.util.TreeSession;

/**
 * This {@link TreeCombiner} creates a special diff tree containing
 * changes from both inputs annotated with modification tags.
 * <p/>
 * It is different to a regular diff where the resulting tree honors
 * combinator rules together with Union and Intersection.
 *
 * @author Toni Menzel (rebaze)
 * @since 0.3
 */
@Singleton
public class DeltaTreeCombiner implements TreeCombiner
{
    private final TreeSession session;

    @Inject
    public DeltaTreeCombiner( TreeSession session )
    {
        this.session = session;
    }

    @Override
    public Tree combine( Tree left, Tree right )
    {
        TreeBuilder builder = session.createTreeBuilder();
        compare( builder, wrapAsIndex( left ), wrapAsIndex( right ) );
        return builder.seal();
    }

    /**
     *
     */
    private void compare( TreeBuilder collector, TreeIndex left, TreeIndex right )
    {

    }
}
