package org.auxis.commons.tree;

import org.auxis.commons.tree.util.TreeConsoleFormatter;
import org.auxis.commons.tree.util.TreeTools;
import org.junit.Test;

import static org.auxis.commons.tree.Selector.selector;
import static org.junit.Assert.assertEquals;

/**
 * Created by tonit on 05/03/15.
 */
public class TreeOperationsTest {
    private static TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();
    private static TreeTools TOOLS = TreeTools.treeTools();

    @Test
    public void diffIdenticalEmpty() {
        TreeBuilder sn1 = TOOLS.createTreeBuilder();
        sn1.branch(selector("p1")).add("one".getBytes());
        sn1.branch(selector("p2")).add("two".getBytes());

        TreeBuilder sn2 = TOOLS.createTreeBuilder();
        sn2.branch(selector("p1")).add("one".getBytes());
        sn2.branch(selector("p3")).add("other".getBytes());

        Tree intersection = TOOLS.intersection(sn1.seal(), sn2.seal());
        Tree difference = TOOLS.diff(sn1.seal(), sn2.seal());
        Tree union = TOOLS.union(sn1.seal(), sn2.seal());

        FORMAT.prettyPrint( sn1.seal(),sn2.seal(),intersection );

        assertEquals("Should have one intersection", 1, intersection.branches().length);
        assertEquals("Should have one difference branch", 1, difference.branches().length);
        assertEquals("Should have three union", 3, union.branches().length);

    }
}
