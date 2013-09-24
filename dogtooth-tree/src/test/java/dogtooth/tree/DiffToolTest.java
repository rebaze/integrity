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

import static dogtooth.tree.Selector.*;
import dogtooth.tree.internal.InMemoryTreeBuilderImpl;
import dogtooth.tree.util.TreeCompare;
import dogtooth.tree.util.TreeConsoleFormatter;
import dogtooth.tree.util.TreeTools;
import static dogtooth.tree.util.TreeCompare.*;

public class DiffToolTest {
	
	private static TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();
	
	@Test
	public void diffIdenticalEmpty() {
		Tree sn1 = new InMemoryTreeBuilderImpl("c1").seal();
		Tree sn2 = new InMemoryTreeBuilderImpl("c2").seal();
		Tree result = compare( sn1, sn2 );
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
		Tree result = compare( sn1, sn2 );
		assertEquals("Should no elements",0,result.branches().length);
	} 
	
	@Test
	public void diffDifferentSimple() {
	    TreeBuilder b1 = new InMemoryTreeBuilderImpl("c1");
        b1.add("Some".getBytes());
        Tree sn1 = b1.seal();
        InMemoryTreeBuilderImpl b2 = new InMemoryTreeBuilderImpl("c2");
        Tree sn2 = b2.seal();
		Tree result = compare( sn1, sn2 );
		assertEquals("Should no elements",1,result.branches().length);
		assertEquals("Select what is different",selector("c2"),result.branches()[0].selector());
		assertEquals("Select what is different",TreeCompare.MODIFIED,result.branches()[0].tags());
        
	} 
	
	@Test
	public void diff() throws IOException {
		// Setup State 1
		TreeBuilder c1 = new InMemoryTreeBuilderImpl( "db1" );
		c1.branch( selector ("table1" ) ).add("Data1".getBytes());
		c1.branch( selector( "table2" )).add("Data2".getBytes());
		c1.branch( selector ("table3")).add("Data2".getBytes());
		
		Tree sn1 = c1.seal();
		
		// Setup State 2
		TreeBuilder c2 = new InMemoryTreeBuilderImpl("db1");
		c2.branch( selector("table1")).add("Data1".getBytes());
		c2.branch( selector("table2")).add("Data2Mod".getBytes());
		c2.branch( selector("table4")).add("Data1".getBytes());
		Tree sn2 = c2.seal();
		
		// Actually compare
		TreeIndex result = new TreeIndex(compare( sn1, sn2 ));
		
		// Display both for visual reference..
		FORMAT.displayTree(0, sn1);
		FORMAT.displayTree(0, sn2);
		FORMAT.displayTree(0, result);
		
		assertEquals("Detect 3 modifications",3, result.select( selector ("db1")).branches().length);
		assertEquals("Modification in db2.table2",TreeCompare.MODIFIED, result.select( selector ("db1")).select( selector ("table2")).tags());
		assertEquals("Modification in db2.table2",TreeCompare.REMOVED,result.select( selector( "db1" )).select( selector ( "table3" )).tags());
		assertEquals("Modification in db2.table2",TreeCompare.ADDED,result.select( selector( "db1" )).select( selector("table4")).tags());
	} 
	
	@Test
	public void testStoreAndLoad() throws IOException {
		TreeBuilder c1 = new InMemoryTreeBuilderImpl("db1");
		c1.branch( selector ("table1")).add("Data1".getBytes());
		c1.branch(selector("table2")).add("Data2".getBytes());
		c1.branch(selector("table3")).add("Data2".getBytes());
		
		Tree sn1 = c1.seal();
		TreeTools tools = new TreeTools();
		File f = tools.store(sn1);
		Tree releoaded = tools.load(f);
		assertEquals(sn1,releoaded);
	}
}
