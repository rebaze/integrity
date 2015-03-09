package org.auxis.commons.tree;

import org.auxis.commons.tree.util.TreeConsoleFormatter;
import org.auxis.commons.tree.util.TreeSession;
import org.junit.Test;

import static org.auxis.commons.tree.Selector.selector;
import static org.junit.Assert.assertEquals;

/**
 * Created by tonit on 05/03/15.
 */
public class TreeOperationsTest {
    private TreeConsoleFormatter formatter = new TreeConsoleFormatter();
    private TreeSession session = TreeSession.getSession();

    @Test
    public void diffIdenticalEmpty() {
        TreeBuilder sn1 = session.createTreeBuilder();
        sn1.branch(selector("p1")).add("one".getBytes());
        sn1.branch(selector("p2")).add("two".getBytes());

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch(selector("p1")).add( "one".getBytes() );
        sn2.branch(selector("p3")).add( "other".getBytes() );

        Tree intersection = session.intersection(sn1.seal(), sn2.seal());
        Tree difference = session.diff(sn1.seal(), sn2.seal());
        Tree union = session.union(sn1.seal(), sn2.seal());

        formatter.prettyPrint( sn1.seal(), sn2.seal(), intersection );

        assertEquals("Should have one intersection", 1, intersection.branches().length);
        assertEquals("Should have one difference branch", 1, difference.branches().length);
//        assertEquals("Should have three union", 3, union.branches().length);

    }
}
