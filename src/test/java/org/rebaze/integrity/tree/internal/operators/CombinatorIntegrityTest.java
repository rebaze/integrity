package org.rebaze.integrity.tree.internal.operators;

import org.rebaze.integrity.tree.api.Selector;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.util.TreeConsoleFormatter;

import static org.rebaze.integrity.tree.api.Selector.selector;
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
        sn1.branch( Selector.selector("p1")).add("one".getBytes());
        sn1.branch( Selector.selector("p2")).add("two".getBytes());

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch( Selector.selector("p1")).add( "one".getBytes() );
        sn2.branch( Selector.selector("p3")).add( "other".getBytes() );

        formatter.prettyPrint( sn1.seal(), sn2.seal() );

        Tree intersection = new IntersectTreeCombiner(session).combine( sn1.seal(), sn2.seal() );
        Tree delta = new DeltaTreeCombiner(session).combine( sn1.seal(), sn2.seal() );
        Tree union = new UnionTreeCombiner(session).combine( sn1.seal(), sn2.seal() );

        Tree combinedDelta = new DiffTreeCombiner(session).combine( union, delta );
        formatter.prettyPrint( intersection, combinedDelta );

        assertEquals( intersection, combinedDelta );
        Assert.assertEquals( union, new UnionTreeCombiner(session).combine( delta, intersection ) );
        assertEquals( delta, new DiffTreeCombiner(session).combine( union, intersection ) );
    }
}
