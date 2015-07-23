package org.auxis.commons.tree.operators;

import org.junit.Test;

import com.rebaze.commons.tree.Tree;
import com.rebaze.commons.tree.TreeBuilder;
import com.rebaze.commons.tree.operators.DeltaTreeCombiner;
import com.rebaze.commons.tree.operators.DiffTreeCombiner;
import com.rebaze.commons.tree.operators.IntersectTreeCombiner;
import com.rebaze.commons.tree.operators.UnionTreeCombiner;
import com.rebaze.commons.tree.util.DefaultTreeSessionFactory;
import com.rebaze.commons.tree.util.TreeConsoleFormatter;
import com.rebaze.commons.tree.util.TreeSession;

import static com.rebaze.commons.tree.Selector.selector;
import static com.rebaze.commons.tree.annotated.Tag.tag;
import static com.rebaze.commons.tree.util.TreeSession.wrapAsIndex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by tonit on 05/03/15.
 */
public class CombinatorIntegrityTest
{
    private TreeConsoleFormatter formatter = new TreeConsoleFormatter();
    private TreeSession session =  new DefaultTreeSessionFactory().create();

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
