package com.rebaze.trees.ext.operators;

import org.junit.Ignore;
import org.junit.Test;

import com.rebaze.trees.core.Tree;
import com.rebaze.trees.core.TreeBuilder;
import com.rebaze.trees.core.util.DefaultTreeSessionFactory;
import com.rebaze.trees.core.util.TreeConsoleFormatter;
import com.rebaze.trees.core.util.TreeSession;

import static com.rebaze.trees.core.Selector.selector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This is the ultimate combiner test as we combine computed parts from other combiners,
 * all to finally result in the tree we originate from.
 *
 * Might be moved to extensions project though.
 */
public class CombinatorIntegrityTest
{
    private TreeConsoleFormatter formatter = new TreeConsoleFormatter();
    private TreeSession session =  new DefaultTreeSessionFactory().create();

    @Ignore
    @Test
    public void testCombinerIntegrity() {
        TreeBuilder sn1 = session.createTreeBuilder();
        sn1.branch(selector("p1")).add("one".getBytes());
        sn1.branch(selector("p2")).add("two".getBytes());

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch(selector("p1")).add( "one".getBytes() );
        sn2.branch(selector("p3")).add( "other".getBytes() );

        formatter.prettyPrint( sn1.seal(), sn2.seal() );

        Tree intersection = new IntersectTreeCombiner(session).combine( sn1.seal(), sn2.seal() );
        Tree delta = new DeltaTreeCombiner(session).combine( sn1.seal(), sn2.seal() );
        Tree union = new UnionTreeCombiner(session).combine( sn1.seal(), sn2.seal() );

        Tree combinedDelta = new DiffTreeCombiner(session).combine( union, delta );
        formatter.prettyPrint( intersection, combinedDelta );

        assertEquals( intersection, combinedDelta );
        assertEquals( union, new UnionTreeCombiner(session).combine( delta, intersection ) );
        assertEquals( delta, new DiffTreeCombiner(session).combine( union, intersection ) );
    }
}
