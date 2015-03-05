package org.auxis.commons.tree.operators;

import org.auxis.commons.tree.Tree;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.TreeCombiner;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

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
        return null;
    }
}
