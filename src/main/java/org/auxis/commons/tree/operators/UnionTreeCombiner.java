package org.auxis.commons.tree.operators;

import org.auxis.commons.tree.Tree;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.TreeCombiner;
import org.auxis.commons.tree.TreeIndex;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import static org.auxis.commons.tree.Selector.selector;
import static org.auxis.commons.tree.annotated.Tag.tag;
import static org.auxis.commons.tree.util.TreeSession.wrapAsIndex;

/**
 * Created by tonit on 05/03/15.
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
        TreeBuilder builder = treeBuilderProvider.get().tag( tag ( "UNION" ));
        include( builder, left );
        include( builder, right );
        return builder.seal();
    }

    private void include( TreeBuilder collector, Tree left )
    {

        if ( left.branches().length == 0 ) {
            collector.branch( left );
        }else
        {
            //TreeBuilder subBuilder = collector.branch( left.selector() ).tag( tag ("infix" ) );

            for ( Tree tree : left.branches() )
            {
                include( collector, tree );
            }
        }
    }
}
