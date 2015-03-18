/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree.operators;

import org.auxis.commons.tree.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import static org.auxis.commons.tree.Selector.selector;
import static org.auxis.commons.tree.annotated.Tag.tag;
import static org.auxis.commons.tree.util.TreeSession.wrapAsIndex;

/**
 * Covers merges of similar trees so that combiners work as a system. (see test cases about combiner integrity)
 * In a sense this should be really coverable by native {@link TreeBuilder#branch(tree)} .
 *
 * @author Toni Menzel (rebaze)
 * @since 0.3
 */
@Singleton
public class UnionTreeCombiner implements TreeCombiner
{
    private final Provider<TreeBuilder> treeBuilderProvider;

    @Inject
    public UnionTreeCombiner( Provider<TreeBuilder> builder )
    {
        treeBuilderProvider = builder;
    }

    @Override public Tree combine( Tree left, Tree right )
    {
        TreeBuilder builder = treeBuilderProvider.get().tag( tag( "UNION" ) );
        include( builder, left );
        include( builder, right );
        return builder.seal();
    }

    private void include( TreeBuilder collector, Tree left )
    {

        if ( left.branches().length == 0 )
        {
            collector.branch( left );
        }
        else
        {
            TreeBuilder subBuilder = collector.branch( left ).tag( tag( "infix" ) );
            /**
             for ( Tree tree : left.branches() )
             {
             include( subBuilder, tree );
             }
             **/
        }
    }
}
