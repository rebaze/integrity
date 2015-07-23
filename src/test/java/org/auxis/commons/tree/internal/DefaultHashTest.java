/*
 * Copyright (c) 2012-2014 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree.internal;

import static com.rebaze.commons.tree.Selector.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.rebaze.commons.tree.Tree;
import com.rebaze.commons.tree.TreeBuilder;
import com.rebaze.commons.tree.TreeException;
import com.rebaze.commons.tree.util.DefaultTreeSessionFactory;
import com.rebaze.commons.tree.util.TreeSession;

public class DefaultHashTest
{
    final private TreeSession session =  new DefaultTreeSessionFactory().create();

    @Test
    public void equalityTest()
    {
        Tree sn1 = session.createTreeBuilder().selector( selector( "c1" ) ).branch( selector( "d" ) ).add( "Some".getBytes() ).seal();
        Tree sn2 = session.createTreeBuilder().selector( selector( "c1" ) ).branch( selector( "d" ) ).add( "Some".getBytes() ).seal();
        Tree sn3 = session.createTreeBuilder().selector( selector( "c1" ) ).branch( selector( "d2" ) ).add( "Other".getBytes() ).seal();

        assertEquals( "Should be identical", sn1, sn2 );
        assertNotEquals( "Should not be identical", sn1, sn3 );
        assertNotEquals( "Should not be identical", sn2, sn3 );
        assertEquals( "Should be identical", sn3, sn3 );
    }

    @Test
    public void equalityTestBeAwareThatHashesArePrimary()
    {
        Tree sn1 = session.createTreeBuilder().selector( selector( "Here" ) ).branch( selector( "whatnow" ) ).add( "Some".getBytes() ).seal();
        Tree sn2 = session.createTreeBuilder().selector( selector( "There" ) ).branch( selector( "d" ) ).add( "Some".getBytes() ).seal();
        assertEquals( "Must be identical", sn1, sn2 );
    }

    @Test (expected = TreeException.class)
    public void testDoNotAllowDataWithBranch()
    {
        TreeBuilder tb = session.createTreeBuilder();
        tb.branch( selector ("foo") );
        tb.add( "Data".getBytes() );
    }

    @Test (expected = TreeException.class)
    public void testDoNotAllowBranchesWithData()
    {
        TreeBuilder tb = session.createTreeBuilder();
        tb.add( "Data".getBytes() );
        tb.branch( selector ("foo") );
    }

    @Test
    public void testAddOrderMatters()
    {
        Tree sn1 = session.createTreeBuilder().branch( selector( "a" ) ).add( "Some".getBytes() ).add("Other".getBytes() ).seal();
        Tree sn2 = session.createTreeBuilder().branch( selector( "a" ) ).add( "Some".getBytes() ).add( "Other".getBytes() ).seal();
        Tree sn3 = session.createTreeBuilder().branch( selector( "c" ) ).add( "Other".getBytes() ).add( "Some".getBytes() ).seal();

        assertEquals( "Must be identical", sn1, sn2 );
        assertNotEquals( "Must not be identical", sn1, sn3 );

        TreeBuilder tb = session.createTreeBuilder();
        tb.branch( sn1 );
        tb.branch( sn2 );
        tb.branch( sn3 );
        assertEquals( "Collabsed to 2 branches", 2, tb.seal().branches().length );
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

        assertEquals( "Must be identical", sn1.seal(), sn2.seal() );
    }

    @Test
    public void testDeepEquality()
    {
        Tree sn1 = session.createTreeBuilder().selector( selector( "Here" ) ).branch( selector( "whatnow" ) ).branch( selector( "deeper" ) ).add( "Some".getBytes() ).seal();
        Tree sn2 = session.createTreeBuilder().selector( selector( "There" ) ).branch( selector( "d" ) ).add( "Some".getBytes() ).seal();
        assertEquals( "Must not be identical", sn1, sn2 );
    }
}
