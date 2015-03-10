package org.auxis.commons.tree.util;

import dagger.Module;
import dagger.Provides;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.TreeCombiner;
import org.auxis.commons.tree.operators.DeltaTreeCombiner;
import org.auxis.commons.tree.operators.DiffTreeCombiner;
import org.auxis.commons.tree.operators.IntersectTreeCombiner;
import org.auxis.commons.tree.operators.UnionTreeCombiner;

import javax.inject.Named;
import javax.inject.Provider;

/**
 * Created by tonit on 05/03/15.
 */
@Module( library = true, injects = { TreeSession.class } )
public class DefaultTreeModule
{
    @Provides @Named("diff") TreeCombiner diffCombiner( Provider<TreeBuilder> builder )
    {
        return new DiffTreeCombiner( builder );
    }

    @Provides @Named("delta") TreeCombiner deltaCombiner( Provider<TreeBuilder> builder )
    {
        return new DeltaTreeCombiner( builder );
    }

    @Provides @Named("union") TreeCombiner unionCombiner( Provider<TreeBuilder> builder )
    {
        return new UnionTreeCombiner( builder );
    }

    @Provides @Named("intersect") TreeCombiner intersectCombiner( Provider<TreeBuilder> builder )
    {
        return new IntersectTreeCombiner( builder );
    }

    @Provides TreeBuilder treeBuilder( TreeSession tools )
    {
        return tools.createTreeBuilder();
    }

}
