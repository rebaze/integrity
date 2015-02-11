package org.auxis.commons.tree;

import org.auxis.commons.tree.TreeBuilder;
import org.auxis.commons.tree.TreeIndex;
import org.auxis.commons.tree.util.TreeTools;
import org.junit.Test;

import java.io.IOException;

import static org.auxis.commons.tree.Selector.selector;
import static org.auxis.commons.tree.annotated.Tag.tag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TreeIndexTest
{
    private static TreeTools TOOLS = new TreeTools();

    @Test
    public void compoundSelectArrayTest() throws IOException
    {
        TreeIndex sn1 = sampleTree();
        assertEquals( tag( "findme" ), sn1.select( selector( "table1", "other" ) ).tags() );
        assertNull( sn1.select( selector( "table1", "wrong" ) ) );
        assertNull( sn1.select( selector( "table2" ) ) );
    }

    private TreeIndex sampleTree()
    {
        TreeBuilder c1 = TOOLS.createTreeBuilder().selector( selector( "db1" ) );
        c1.branch( selector( "table1" ) ).branch( selector( "other" ) ).add( "Data1".getBytes() ).tag( tag( "findme" ) );
        TreeIndex sn1 = new TreeIndex( c1.seal() );
        return sn1;
    }
}
