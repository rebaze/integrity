/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package dogtooth.tree;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import dogtooth.tree.internal.InMemoryTreeBuilderImpl;

public class DiffToolTest {
	
	private static TreeTools TOOLS = new TreeTools();
	
	@Test
	public void diffIdenticalEmpty() {
		Tree sn1 = new InMemoryTreeBuilderImpl("c1").seal();
		Tree sn2 = new InMemoryTreeBuilderImpl("c2").seal();
		Tree result = TOOLS.compare( sn1, sn2 );
		assertEquals("Should no elements",0,result.branches().length);
	} 
	
	@Test
	public void diffIdenticalMedium() {
		TreeBuilder b1 = new InMemoryTreeBuilderImpl("c1");
		b1.add("Some".getBytes());
        Tree sn1 = b1.seal();
		InMemoryTreeBuilderImpl b2 = new InMemoryTreeBuilderImpl("c2");
		b2.add("Some".getBytes());
        Tree sn2 = b2.seal();
		Tree result = TOOLS.compare( sn1, sn2 );
		assertEquals("Should no elements",0,result.branches().length);
	} 
	
	@Test
	public void diffDifferentSimple() {
	    TreeBuilder b1 = new InMemoryTreeBuilderImpl("c1");
        b1.add("Some".getBytes());
        Tree sn1 = b1.seal();
        InMemoryTreeBuilderImpl b2 = new InMemoryTreeBuilderImpl("c2");
        Tree sn2 = b2.seal();
		Tree result = TOOLS.compare( sn1, sn2 );
		assertEquals("Should no elements",1,result.branches().length);
		assertEquals("Select what is different","c2",result.branches()[0].selector());
		assertEquals("Select what is different",TreeTools.MODIFIED,result.branches()[0].tags());
        
	} 
	
	@Test
	public void diff() throws IOException {
		// Setup State 1
		TreeBuilder c1 = new InMemoryTreeBuilderImpl("db1");
		c1.branch("table1").add("Data1".getBytes());
		c1.branch("table2").add("Data2".getBytes());
		c1.branch("table3").add("Data2".getBytes());
		
		Tree sn1 = c1.seal();
		
		// Setup State 2
		TreeBuilder c2 = new InMemoryTreeBuilderImpl("db2");
		c2.branch("table1").add("Data1".getBytes());
		c2.branch("table2").add("Data2Mod".getBytes());
		c2.branch("table4").add("Data1".getBytes());
		Tree sn2 = c2.seal();
		
		// Actually compare
		TreeIndex result = new TreeIndex(TOOLS.compare( sn1, sn2 ));
		
		// Display both for visual reference..
		TOOLS.displayTree(0, sn1);
		TOOLS.displayTree(0, sn2);
		TOOLS.displayTree(0, result);
		
		assertEquals("Detect 3 modifications",3, result.select("db2").branches().length);
		assertEquals("Modification in db2.table2",TreeTools.MODIFIED,result.select("db2").select("table2").tags());
		assertEquals("Modification in db2.table2",TreeTools.REMOVED,result.select("db2").select("table3").tags());
		assertEquals("Modification in db2.table2",TreeTools.ADDED,result.select("db2").select("table4").tags());
	} 
	
	@Test
	public void testStoreAndLoad() throws IOException {
		TreeBuilder c1 = new InMemoryTreeBuilderImpl("db1");
		c1.branch("table1").add("Data1".getBytes());
		c1.branch("table2").add("Data2".getBytes());
		c1.branch("table3").add("Data2".getBytes());
		
		Tree sn1 = c1.seal();
		
		File f = TOOLS.store(sn1);
		Tree releoaded = TOOLS.load(f);
		assertEquals(sn1,releoaded);
	}
}
