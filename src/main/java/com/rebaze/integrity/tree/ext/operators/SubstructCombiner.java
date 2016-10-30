package com.rebaze.integrity.tree.ext.operators;

import com.rebaze.integrity.tree.Tree;
import com.rebaze.integrity.tree.TreeCombiner;
import com.rebaze.integrity.tree.TreeSession;
import com.rebaze.integrity.tree.TreeBuilder;

/**
 * Rebuilds left with paths only where right is a leaf.
 *
 * Helpful for relocating subtrees within larger trees.
 *
 */
public class SubstructCombiner implements TreeCombiner
{
    private final TreeSession session;

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
