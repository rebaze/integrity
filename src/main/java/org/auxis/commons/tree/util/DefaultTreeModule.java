/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree.util;

import dagger.Module;
import dagger.Provides;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.TreeCombiner;
import org.auxis.commons.tree.operators.*;

import javax.inject.Named;
import javax.inject.Provider;

/**
 * Default Module for DI with Dagger.
 * Right now its pretty much a god mode module.
 *
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

    @Provides @Named("substruct") TreeCombiner substruct( Provider<TreeBuilder> builder )
    {
        return new SubstructCombiner( builder );
    }

    @Provides TreeBuilder treeBuilder( TreeSession tools )
    {
        return tools.createTreeBuilder();
    }

}
