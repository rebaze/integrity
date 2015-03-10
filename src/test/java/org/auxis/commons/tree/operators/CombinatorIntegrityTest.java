package org.auxis.commons.tree.operators;

import org.auxis.commons.tree.Tree;
import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.util.TreeConsoleFormatter;
import org.auxis.commons.tree.util.TreeSession;
import org.junit.Test;

import static org.auxis.commons.tree.Selector.selector;
import static org.auxis.commons.tree.annotated.Tag.tag;
import static org.auxis.commons.tree.util.TreeSession.wrapAsIndex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by tonit on 05/03/15.
 */
public class CombinatorIntegrityTest
{
    private TreeConsoleFormatter formatter = new TreeConsoleFormatter();
    private TreeSession session = TreeSession.getSession();

    @Test
    public void testCombinerIntegrity() {
        TreeBuilder sn1 = session.createTreeBuilder();
        sn1.branch(selector("p1")).add("one".getBytes());
        sn1.branch(selector("p2")).add("two".getBytes());

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch(selector("p1")).add( "one".getBytes() );
        sn2.branch(selector("p3")).add( "other".getBytes() );

        formatter.prettyPrint( sn1.seal(), sn2.seal() );

        Tree intersection = session.intersection(sn1.seal(), sn2.seal());
        Tree delta = session.delta( sn1.seal(), sn2.seal() );
        Tree union = session.union( sn1.seal(), sn2.seal() );

        Tree combinedDelta = session.diff( union, delta );
        formatter.prettyPrint( intersection, combinedDelta );

        assertEquals( intersection, combinedDelta );
        assertEquals( union, session.union( delta, intersection ) );
        assertEquals( delta, session.diff( union, intersection ) );
    }
}
