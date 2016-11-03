/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.internal.operators;

import org.rebaze.integrity.tree.api.*;

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
public class DeltaTreeCombiner implements TreeCombiner
{
    private final TreeSession session;

    public DeltaTreeCombiner( TreeSession session )
    {
        this.session = session;
    }

    @Override
    public Tree combine( Tree left, Tree right )
    {
        TreeBuilder builder = session.createTreeBuilder();
        compare( builder, TreeSession.wrapAsIndex( left ), TreeSession.wrapAsIndex( right ) );
        return builder.seal();
    }

    /**
     *
     */
    private void compare( TreeBuilder collector, TreeIndex left, TreeIndex right )
    {

    }
}
