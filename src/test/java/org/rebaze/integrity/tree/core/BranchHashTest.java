package org.rebaze.integrity.tree.core;

import org.junit.Test;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.TreeIndex;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;
import org.rebaze.integrity.tree.util.TreeConsoleFormatter;

import static org.junit.Assert.assertTrue;

/**
 * Created by tonit on 18/11/2016.
 */
public class BranchHashTest
{
    public static final TreeConsoleFormatter PRINT = new TreeConsoleFormatter();

    private TreeSession session =  new DefaultTreeSessionFactory().create();

    @Test
    public void testMerge() throws Exception
    {
        TreeBuilder root = session.createTreeBuilder();
        Tree s1 = session.createTreeBuilder().selector( "item1").add("data1" ).seal();
        Tree s2 = session.createTreeBuilder().selector( "item2").add("data2" ).seal();
        root.branch(s1);
        root.branch(s2);
        TreeIndex merged = TreeSession.wrapAsIndex( root.seal()) ;
        PRINT.prettyPrint( merged );
        assertTrue(merged.contains(s1));
        assertTrue(merged.contains(s2));


    }

}
