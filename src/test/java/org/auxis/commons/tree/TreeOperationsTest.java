package org.auxis.commons.tree;

import org.auxis.commons.tree.util.TreeConsoleFormatter;
import org.auxis.commons.tree.util.TreeTools;
import org.junit.Test;

import static org.auxis.commons.tree.Selector.selector;
import static org.junit.Assert.assertEquals;

/**
 * Created by tonit on 05/03/15.
 */
public class TreeOperationsTest
{
    private static TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();
    private static TreeTools TOOLS = TreeTools.treeTools();

    @Test
    public void diffIdenticalEmpty()
    {
        Tree sn1 = TOOLS.createTreeBuilder()
            .branch( selector( "p1" ) ).add( "one".getBytes() )
            .branch( selector( "p1" ) ).add( "two".getBytes() )
            .seal();
        Tree sn2 = TOOLS.createTreeBuilder().selector( selector( "c2" ) ).seal();

        Tree result = TOOLS.intersection( sn1, sn2 );
        assertEquals( "Should no elements", 0, result.branches().length );
    }
}
