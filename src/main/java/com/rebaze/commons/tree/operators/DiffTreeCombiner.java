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
import javax.inject.Singleton;

import com.rebaze.commons.tree.*;
import com.rebaze.commons.tree.annotated.Tag;
import com.rebaze.commons.tree.util.TreeSession;

/**
 * This {@link TreeCombiner} creates a special diff tree containing
 * changes from both inputs annotated with modification tags.
 *
 * It is different to a regular diff where the resulting tree honors
 * combinator rules together with Union and Intersection.
 *
 * @author Toni Menzel (rebaze)
 * @since 0.3
 *
 */
@Singleton
public class DiffTreeCombiner implements TreeCombiner
{
    public static final Tag ADDED = new Tag( "ADDED" );
    public static final Tag REMOVED = new Tag( "REMOVED" );
    public static final Tag MODIFIED = new Tag( "MODIFIED" );

    private final TreeSession session;

    @Inject
    public DiffTreeCombiner( TreeSession session )
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
        // Unfold "empty" elements.
        if ( left == null )
        {
            for ( TreeIndex tree : right.branches() )
            {
                TreeBuilder mod = collector.branch( tree.selector() ).tag( ADDED );
                compare( mod, null, tree );
            }
            return;
        }
        if ( right == null )
        {
            for ( TreeIndex tree : left.branches() )
            {
                TreeBuilder mod = collector.branch( tree.selector() ).tag( REMOVED );
                compare( mod, tree, null );
            }
            return;
        }

        if ( !left.fingerprint().equals( right.fingerprint() ) )
        {
            if ( left.branches().length == 0 && right.branches().length == 0 )
            {
                collector.branch( right ).tag( MODIFIED );
            }
            else
            {
                TreeBuilder modification = collector.branch( right.selector() ).tag( MODIFIED );
                for ( TreeIndex tree : left.branches() )
                {
                    // first check if tree is a selectable node or not:
                    if ( tree.selectable() )
                    {
                        TreeIndex origin = right.select( tree.selector() );
                        if ( origin == null )
                        {
                            // Deleted element:

                            if ( tree.branches().length == 0 )
                            {
                                modification.branch( tree ).tag( REMOVED );
                            }
                            else
                            {
                                TreeBuilder removed = modification.branch( tree.selector() ).tag( REMOVED );
                                compare( removed, tree, null );
                            }
                        }
                        else
                        {
                            // diff content:
                            compare( modification, tree, origin );
                        }

                    }
                    else
                    {
                        throw new TreeException( "Item " + tree + " is not selectable." );
                    }
                }

                for ( TreeIndex tree : right.branches() )
                {
                    // first check if tree is a selectable node or not:
                    if ( tree.selectable() )
                    {
                        TreeIndex origin = left.select( tree.selector() );
                        if ( origin == null )
                        {
                            // New element:
                            if ( tree.branches().length == 0 )
                            {
                                modification.branch( tree ).tag( ADDED );
                            }
                            else
                            {
                                TreeBuilder added = modification.branch( tree.selector() ).tag( ADDED );
                                compare( added, null, tree );
                            }
                        }
                        else
                        {
                            // already worked on.
                        }

                    }
                    else
                    {
                        throw new TreeException( "Item " + tree + " is not selectable." );
                    }
                }
            }
        }
    }
}

