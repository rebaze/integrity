package com.rebaze.trees.core;

import com.rebaze.trees.core.TreeBuilder;
import com.rebaze.trees.core.TreeIndex;
import org.junit.Test;

import com.rebaze.trees.core.util.DefaultTreeSessionFactory;
import com.rebaze.trees.core.util.TreeSession;

import java.io.IOException;

import static com.rebaze.trees.core.Selector.selector;
import static com.rebaze.trees.core.Tag.tag;
import static com.rebaze.trees.core.util.TreeSession.wrapAsIndex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TreeIndexTest
{
    private TreeSession session =  new DefaultTreeSessionFactory().create();

    @Test
    public void compoundSelectArrayTest() throws IOException
    {
        TreeIndex sn1 = sampleTree();
        assertEquals( tag( "findme" ), sn1.select( selector( "table1", "other" ) ).tags() );
        assertNull( sn1.select( selector( "table1", "wrong" ) ) );
        assertNull( sn1.select( selector( "table2" ) ) );
    }

    @Test
    public void testSameSelectorDifferentData() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( selector( "db2" ) ).add( "dat2".getBytes() );
        c1.branch( selector( "db1" ) ).add( "data3".getBytes() );
        assertEquals( 2, wrapAsIndex( c1.seal() ).branches().length );
    }

    @Test
    public void testSameDataDifferentSelector() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( selector( "db2" ) ).add( "dat2".getBytes() );
        c1.branch( selector( "db3" ) ).add( "data3".getBytes() );
        assertEquals( 3, wrapAsIndex( c1.seal() ).branches().length );
    }


    @Test
    public void testSameDataSameSelector() throws IOException
    {
        TreeBuilder c1 = session.createTreeBuilder();
        c1.branch( selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( selector( "db1" ) ).add( "data".getBytes() );
        c1.branch( selector( "db3" ) ).add( "data".getBytes() );
        assertEquals( 2, wrapAsIndex( c1.seal() ).branches().length );
    }

    private TreeIndex sampleTree()
    {
        TreeBuilder c1 = session.createTreeBuilder().selector( selector( "db1" ) );
        c1.branch( selector( "table1" ) ).branch( selector( "other" ) ).add( "Data1".getBytes() ).tag( tag( "findme" ) );
        TreeIndex sn1 = new TreeIndex( c1.seal() );
        return sn1;
    }
}
