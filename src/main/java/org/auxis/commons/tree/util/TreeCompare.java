package org.auxis.commons.tree.util;

import org.auxis.commons.tree.*;
import org.auxis.commons.tree.annotated.Tag;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class TreeCompare implements TreeCombiner
{
    public static final Tag ADDED = new Tag( "ADDED" );
    public static final Tag REMOVED = new Tag( "REMOVED" );
    public static final Tag MODIFIED = new Tag( "MODIFIED" );

    @Inject private TreeTools context;

    @Override public Tree combine( Tree left, Tree right )
    {
        TreeBuilder builder = context.createTreeBuilder();
        compare(builder,new TreeIndex(left),new TreeIndex(right));
        return builder.seal();
    }

    /**
     *
     */
    static void compare( TreeBuilder collector, TreeIndex left, TreeIndex right )
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
            // compare next level:
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
                        // compare content:
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

    /**
     * Structure-aware identity.
     *
     * @param collector
     * @param left
     * @param right
     */
    static void identBySelector( TreeBuilder collector, TreeIndex left, TreeIndex right )
    {

    }

    /**
     * Hash based identity.
     * Copies all trees from left that have an equivalent in right.
     * <p/>
     * By doing this from both sides, one could construct a nice "move-" tree.
     *
     * @param collector
     * @param left
     * @param right
     */
    static void identLeftHash( TreeBuilder collector, TreeIndex left, TreeIndex right )
    {
        identLeftHash( collector, left, right, buildDeep( right ) );
    }

    static void identLeftHash( TreeBuilder collector, TreeIndex left, TreeIndex right, Map<String, Tree> index )
    {

        //if (index.containsKey(left.))
        if ( !left.fingerprint().equals( right.fingerprint() ) )
        {
            // dig deeper

        }
        else
        {
            // fast forward, copy whole branch.
            collector.branch( left );
        }
    }

    private static Map<String, Tree> buildDeep( Tree tree )
    {
        Map<String, Tree> index = new HashMap<String, Tree>();
        buildDeep( tree, index );
        return index;
    }

    private static void buildDeep( Tree tree, Map<String, Tree> index )
    {
        index.put( tree.fingerprint(), tree );
        for ( Tree sub : tree.branches() )
        {
            buildDeep( sub, index );
        }
    }
}
