package dogtooth.tree.util;

import static dogtooth.tree.Selector.selector;
import dogtooth.tree.Tree;
import dogtooth.tree.TreeBuilder;
import dogtooth.tree.TreeException;
import dogtooth.tree.TreeIndex;
import dogtooth.tree.annotated.Tag;
import dogtooth.tree.internal.InMemoryTreeBuilderImpl;

public class TreeCompare {
    
    public static final Tag ADDED = new Tag("ADDED");
    public static final Tag REMOVED = new Tag("REMOVED");
    public static final Tag MODIFIED = new Tag("MODIFIED");
    
    /**
     */
    static public Tree compare( Tree left, Tree right ) {
        return compare( new TreeIndex( left ), new TreeIndex( right ) );
    }
    
    /**
     */
    static public Tree compare( TreeIndex left, TreeIndex right ) {
        TreeBuilder target = new InMemoryTreeBuilderImpl( selector("[" + left.selector() + " ] and [" + right.selector() + " ]" ) );
        target.tag( Tag.tag("DIFF"));
        compare( target, left, right );
        return target.seal();
    }


    /**
     * 
     */
    static private void compare( TreeBuilder collector, TreeIndex left, TreeIndex right ) {
        // Unfold "empty" elements.
        if( left == null ) {
            for( TreeIndex tree : right.branches() ) {
                TreeBuilder mod = collector.branch( tree.selector() ).tag( ADDED );
                compare( mod, null, tree );
            }
            return;
        }
        if( right == null ) {
            for( TreeIndex tree : left.branches() ) {
                TreeBuilder mod = collector.branch( tree.selector() ).tag( REMOVED );
                compare( mod, tree, null );
            }
            return;
        }

        if( !left.fingerprint().equals( right.fingerprint() ) ) {
            // compare next level:
            TreeBuilder modification = collector.branch( right.selector() ).tag(MODIFIED);
            for( TreeIndex tree : left.branches() ) {
                // first check if tree is a selectable node or not:
                if( tree.selectable() ) {
                    TreeIndex origin = right.select( tree.selector() );
                    if( origin == null ) {
                        // Deleted element:
                        TreeBuilder removed = modification.branch( tree.selector() ).tag(REMOVED);
                        // Not so sure..
                        compare( removed, tree, origin );
                    } else {
                        // compare content:
                        compare( modification, tree, origin );
                    }

                } else {
                    throw new TreeException( "Item " + tree + " is not selectable." );
                }
            }
            // find new ones:
            for( TreeIndex tree : right.branches() ) {
                // first check if tree is a selectable node or not:
                if( tree.selectable() ) {
                    TreeIndex origin = left.select( tree.selector() );
                    if( origin == null ) {
                        // New element:
                        TreeBuilder added = modification.branch( tree.selector() ).tag( ADDED );
                        compare( added, origin, tree );
                    } else {
                        // already worked on.
                    }

                } else {
                    throw new TreeException( "Item " + tree + " is not selectable." );
                }
            }
        }

    }
}
