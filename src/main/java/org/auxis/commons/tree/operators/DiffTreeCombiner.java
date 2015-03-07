package org.auxis.commons.tree.operators;

import org.auxis.commons.tree.*;
import org.auxis.commons.tree.annotated.Tag;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import static org.auxis.commons.tree.Selector.selector;
import static org.auxis.commons.tree.util.TreeTools.wrapAsIndex;

@Singleton
public class DiffTreeCombiner implements TreeCombiner
{
    public static final Tag ADDED = new Tag( "ADDED" );
    public static final Tag REMOVED = new Tag( "REMOVED" );
    public static final Tag MODIFIED = new Tag( "MODIFIED" );

    private final Provider<TreeBuilder> treeBuilderProvider;

    @Inject
    public DiffTreeCombiner( Provider<TreeBuilder> builder )
    {
        treeBuilderProvider = builder;
    }

    @Override public Tree combine( Tree left, Tree right )
    {
        TreeBuilder builder = treeBuilderProvider.get();
        builder.selector( selector("DIFF") );

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
            // diff next level:
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
                        TreeBuilder removed = modification.branch( tree.selector() ).tag( REMOVED );
                        // Not so sure..
                        compare( removed, tree, origin );
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
            // find new ones:
            for ( TreeIndex tree : right.branches() )
            {
                // first check if tree is a selectable node or not:
                if ( tree.selectable() )
                {
                    TreeIndex origin = left.select( tree.selector() );
                    if ( origin == null )
                    {
                        // New element:
                        TreeBuilder added = modification.branch( tree.selector() ).tag( ADDED );
                        compare( added, origin, tree );
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