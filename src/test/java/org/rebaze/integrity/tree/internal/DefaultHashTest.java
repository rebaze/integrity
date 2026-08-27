/*
 * Copyright (c) 2012-2014 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.internal;

import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.TreeException;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.util.DefaultTreeSessionFactory;
import org.junit.jupiter.api.Test;

import static org.rebaze.integrity.tree.api.Selector.selector;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultHashTest
{
    final private TreeSession session =  new DefaultTreeSessionFactory().create();

    @Test
    public void equalityTest()
    {
        Tree sn1 = session.createTreeBuilder().selector( selector( "c1" ) ).branch( selector( "d" ) ).add( "Some".getBytes() ).seal();
        Tree sn2 = session.createTreeBuilder().selector( selector( "c1" ) ).branch( selector( "d" ) ).add( "Some".getBytes() ).seal();
        Tree sn3 = session.createTreeBuilder().selector( selector( "c1" ) ).branch( selector( "d2" ) ).add( "Other".getBytes() ).seal();

        assertEquals( sn1, sn2, "Should be identical" );
        assertNotEquals( sn1, sn3, "Should not be identical" );
        assertNotEquals( sn2, sn3, "Should not be identical" );
        assertEquals( sn3, sn3, "Should be identical" );
    }

    @Test
    public void equalityTestBeAwareThatHashesArePrimary()
    {
        Tree sn1 = session.createTreeBuilder().selector( selector( "Here" ) ).branch( selector( "whatnow" ) ).add( "Some".getBytes() ).seal();
        Tree sn2 = session.createTreeBuilder().selector( selector( "There" ) ).branch( selector( "d" ) ).add( "Some".getBytes() ).seal();
        assertEquals( sn1, sn2, "Must be identical" );
    }

    @Test
    public void testDoNotAllowDataWithBranch()
    {
        TreeBuilder tb = session.createTreeBuilder();
        tb.branch( selector ("foo") );
        assertThrows( TreeException.class, () -> tb.add( "Data".getBytes() ) );
    }

    @Test
    public void testDoNotAllowBranchesWithData()
    {
        TreeBuilder tb = session.createTreeBuilder();
        tb.add( "Data".getBytes() );
        assertThrows( TreeException.class, () -> tb.branch( selector ("foo") ) );
    }

    @Test
    public void testAddOrderMatters()
    {
        Tree sn1 = session.createTreeBuilder().branch( selector( "a" ) ).add( "Some".getBytes() ).add("Other".getBytes() ).seal();
        Tree sn2 = session.createTreeBuilder().branch( selector( "a" ) ).add( "Some".getBytes() ).add( "Other".getBytes() ).seal();
        Tree sn3 = session.createTreeBuilder().branch( selector( "c" ) ).add( "Other".getBytes() ).add( "Some".getBytes() ).seal();

        assertEquals( sn1, sn2, "Must be identical" );
        assertNotEquals( sn1, sn3, "Must not be identical" );

        TreeBuilder tb = session.createTreeBuilder();
        tb.branch( sn1 );
        tb.branch( sn2 );
        tb.branch( sn3 );
        assertEquals( 2, tb.seal().branches().length, "Collabsed to 2 branches" );
    }

    @Test
    public void testSubTreeOrderDoesNotMatter()
    {
        TreeBuilder sn1 = session.createTreeBuilder();
        sn1.branch( selector( "a" ) ).add( "Some".getBytes() );
        sn1.branch( selector( "b" ) ).add( "Other".getBytes() );

        TreeBuilder sn2 = session.createTreeBuilder();
        sn2.branch( selector( "a" ) ).add( "Other".getBytes() );
        sn2.branch( selector( "b" ) ).add( "Some".getBytes() );

        assertEquals( sn1.seal(), sn2.seal(), "Must be identical" );
    }

    @Test
    public void testDeepEquality()
    {
        Tree sn1 = session.createTreeBuilder().selector( selector( "Here" ) ).branch( selector( "whatnow" ) ).branch( selector( "deeper" ) ).add( "Some".getBytes() ).seal();
        Tree sn2 = session.createTreeBuilder().selector( selector( "There" ) ).branch( selector( "d" ) ).add( "Some".getBytes() ).seal();
        assertEquals( sn1, sn2, "Must not be identical" );
    }
}
