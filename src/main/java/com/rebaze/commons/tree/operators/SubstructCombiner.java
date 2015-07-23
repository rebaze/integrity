package com.rebaze.commons.tree.operators;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.rebaze.commons.tree.Tree;
import com.rebaze.commons.tree.TreeBuilder;
import com.rebaze.commons.tree.TreeCombiner;
import com.rebaze.commons.tree.util.TreeSession;

/**
 * Rebuilds left with paths only where right is a leaf.
 *
 * Helpful for relocating subtrees within larger trees.
 *
 */
@Singleton
public class SubstructCombiner implements TreeCombiner
{
    private final TreeSession session;

    @Inject
    public SubstructCombiner( TreeSession session )
    {
        this.session = session;
    }

    @Override public Tree combine( Tree left, Tree right )
    {
        TreeBuilder tb = session.createTreeBuilder();
        walk(tb,left,right);
        return tb.seal();
    }

    private void walk( TreeBuilder tb, Tree base, Tree target )
    {
        boolean relevant = false;
        //depth first:
        for (Tree t : base.branches()) {
            if (t.equals( target )) {
                relevant = true;
            }else {

            }
            walk(tb,t,target);
        }
        if (relevant) {
            tb.branch( base );
        }
    }
}
