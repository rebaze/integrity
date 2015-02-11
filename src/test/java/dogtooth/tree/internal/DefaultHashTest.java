/*
 * Copyright (c) 2012-2014 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package dogtooth.tree.internal;

import static org.junit.Assert.*;

import dogtooth.tree.util.TreeTools;
import org.junit.Test;

import dogtooth.tree.Tree;
import static dogtooth.tree.Selector.*;


public class DefaultHashTest {
    private final TreeTools TOOLS = new TreeTools();

    @Test
	public void equalityTest() {
		Tree sn1 = TOOLS.createTreeBuilder().selector(selector("c1")).branch( selector ("d") ).add("Some".getBytes()).seal();
		Tree sn2 = TOOLS.createTreeBuilder().selector(selector("c1")).branch( selector ("d" )).add("Some".getBytes()).seal();
		Tree sn3 = TOOLS.createTreeBuilder().selector(selector("c1")).branch( selector ("d2" )).add("Other".getBytes()).seal();
		
		assertEquals("Should be identical",sn1,sn2);
		assertNotEquals("Should be identical",sn1,sn3);
		assertNotEquals("Should be identical",sn2,sn3);
		assertEquals("Should be identical",sn3,sn3);
	} 
	
	@Test
	public void equalityTestBeAwareThatHashesArePrimary() {
		Tree sn1 = TOOLS.createTreeBuilder().selector(selector("Here")).branch(selector("whatnow")).add("Some".getBytes()).seal();
		Tree sn2 = TOOLS.createTreeBuilder().selector(selector("There")).branch(selector("d")).add("Some".getBytes()).seal();
		assertEquals("Must be identical",sn1,sn2);
	} 
}
