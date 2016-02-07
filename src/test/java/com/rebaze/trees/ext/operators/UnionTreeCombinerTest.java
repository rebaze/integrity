package com.rebaze.trees.ext.operators;

import org.junit.Ignore;
import org.junit.Test;

import com.rebaze.trees.core.TreeBuilder;
import com.rebaze.trees.core.TreeException;
import com.rebaze.trees.core.TreeIndex;
import com.rebaze.trees.core.util.DefaultTreeSessionFactory;
import com.rebaze.trees.core.util.TreeConsoleFormatter;
import com.rebaze.trees.core.TreeSession;

import static com.rebaze.trees.core.Selector.selector;
import static com.rebaze.trees.core.Tag.tag;
import static com.rebaze.trees.core.TreeSession.wrapAsIndex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by tonit on 10/03/15.
 */
@Ignore
public class UnionTreeCombinerTest
{
    private TreeConsoleFormatter formatter = new TreeConsoleFormatter();
    private TreeSession session =  new DefaultTreeSessionFactory().create();

    @Test
    public void testUnionWIthMerge() {
        TreeBuilder sn1 = session.createTreeBuilder();
        sn1.branch(selector("p1")).add("onedqwd".getBytes());
        sn1.branch(selector("p2")).add( "two".getBytes() );

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch(selector("p1")).add( "one".getBytes() );
        sn2.branch(selector("p3")).add( "other".getBytes() );

        TreeIndex union = wrapAsIndex( new UnionTreeCombiner(session).combine( sn1.seal(), sn2.seal() ) );
        formatter.prettyPrint( sn1.seal(), sn2.seal(), union );

        assertEquals( 3, union.branches().length );
        assertEquals( "ad782e", union.select( selector( "p2" ) ).fingerprint().substring( 0, 6 ) );
        assertEquals( "d0941e", union.select( selector(  "p3" ) ).fingerprint().substring( 0,6)  );

        // Merged node, must be a new checksum:
        assertNotEquals( "7c4656", union.select( selector( "p1" ) ).fingerprint().substring( 0,6) ) ;
        assertNotEquals( "fe05bc", union.select( selector( "p1" ) ).fingerprint().substring( 0,6 ) );
        assertEquals( tag("MERGED"), union.select( selector( "p1" ) ).tags() );

    }

    @Test
    public void testDiscreteUnion() {
        TreeBuilder sn1 = session.createTreeBuilder();
        sn1.branch(selector("p1")).add("one".getBytes());
        sn1.branch(selector("p2")).add( "two".getBytes() );

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch(selector("p1")).add( "one".getBytes() );
        sn2.branch(selector("p3")).add( "other".getBytes() );

        TreeIndex union = wrapAsIndex( new UnionTreeCombiner(session).combine( sn1.seal(), sn2.seal() ) );
        formatter.prettyPrint( sn1.seal(), sn2.seal(), union );

        assertEquals( 3, union.branches().length );
        assertEquals( "fe05bc", union.select( selector( "p1" ) ).fingerprint().substring( 0, 6 ) ) ;
        assertEquals( "ad782e", union.select( selector( "p2" ) ).fingerprint().substring( 0, 6 ) );
        assertEquals( "d0941e", union.select( selector( "p3" ) ).fingerprint().substring( 0, 6 ) );
    }

    @Test (expected = TreeException.class )
    public void testDeepUnionWithIncompatibleBranchData() {
        TreeBuilder sn1 = session.createTreeBuilder();
        sn1.branch(selector("p1")).branch( selector( "deep" ) ).add( "one".getBytes() );
        sn1.branch(selector("p2")).add( "two".getBytes() );

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch(selector("p1")).add( "one".getBytes() );
        sn2.branch(selector("p3")).add( "other".getBytes() );

        TreeIndex union = wrapAsIndex( new UnionTreeCombiner(session).combine( sn1.seal(), sn2.seal() ) );
        formatter.prettyPrint( sn1.seal(), sn2.seal(), union );

        assertEquals( 3, union.branches().length );
        assertEquals( "fe05bc", union.select( selector( "p1" ) ).fingerprint().substring( 0, 6 ) ) ;
        assertEquals( "ad782e", union.select( selector( "p2" ) ).fingerprint().substring( 0, 6 ) );
        assertEquals( "d0941e", union.select( selector( "p3" ) ).fingerprint().substring( 0, 6 ) );
    }

    @Test
    public void testDeepUnion() {
        TreeBuilder sn1 = session.createTreeBuilder();
        sn1.branch(selector("p1")).branch( selector( "deep" ) ).add( "one".getBytes() );
        sn1.branch(selector("p2")).add( "two".getBytes() );

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch(selector("p1")).branch( selector( "deep" ) ).add( "other".getBytes() );
        sn2.branch(selector("p3")).add( "other".getBytes() );

        TreeIndex union = wrapAsIndex( new UnionTreeCombiner(session).combine( sn1.seal(), sn2.seal() ) );
        formatter.prettyPrint( sn1.seal(), sn2.seal(), union );

        assertEquals( 3, union.branches().length );
        assertEquals( "fe05bc", union.select( selector( "p1" ) ).fingerprint().substring( 0, 6 ) ) ;
        assertEquals( "ad782e", union.select( selector( "p2" ) ).fingerprint().substring( 0, 6 ) );
        assertEquals( "d0941e", union.select( selector( "p3" ) ).fingerprint().substring( 0, 6 ) );
    }

}
