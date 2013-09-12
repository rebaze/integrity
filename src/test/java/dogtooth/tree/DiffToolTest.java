package dogtooth.tree;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import dogtooth.tree.internal.Collector;

public class DiffToolTest {
	
	private static TreeTools TOOLS = new TreeTools();
	
	@Test
	public void diffIdenticalEmpty() {
		Hash sn1 = new Collector("c1").seal();
		Hash sn2 = new Collector("c2").seal();
		Hash result = TOOLS.compare( sn1, sn2 );
		assertEquals("Should no elements",0,result.getElements().length);
	} 
	
	@Test
	public void diffIdenticalMedium() {
		Hash sn1 = new Collector("c1").add("Some".getBytes()).seal();
		Hash sn2 = new Collector("c2").add("Some".getBytes()).seal();
		Hash result = TOOLS.compare( sn1, sn2 );
		assertEquals("Should no elements",0,result.getElements().length);
	} 
	
	@Test
	public void diffDifferentSimple() {
		Hash sn1 = new Collector("c1").add("Some".getBytes()).seal();
		Hash sn2 = new Collector("c2").seal();
		Hash result = TOOLS.compare( sn1, sn2 );
		assertEquals("Should no elements",1,result.getElements().length);
		assertEquals("Select what is different","[MOD] c2",result.getElements()[0].getSelector());
	} 
	
	@Test
	public void diff() throws IOException {
		// Setup State 1
		Collector c1 = new Collector("db1");
		c1.childCollector("table1").add("Data1".getBytes());
		c1.childCollector("table2").add("Data2".getBytes());
		c1.childCollector("table3").add("Data2".getBytes());
		
		Hash sn1 = c1.seal();
		
		// Setup State 2
		Collector c2 = new Collector("db2");
		c2.childCollector("table1").add("Data1".getBytes());
		c2.childCollector("table2").add("Data2Mod".getBytes());
		c2.childCollector("table4").add("Data1".getBytes());
		Hash sn2 = c2.seal();
		
		// Actually compare
		TreeIndex result = new TreeIndex(TOOLS.compare( sn1, sn2 ));
		
		// Display both for visual reference..
		TOOLS.displayTree(0, sn1);
		TOOLS.displayTree(0, sn2);
		TOOLS.displayTree(0, result);
		
		assertEquals("Detect 3 modifications",3, result.select("[MOD] db2").getElements().length);
		assertNotNull("Modification in db2.table2",result.select("[MOD] db2").select("[MOD] table2"));
		assertNotNull("Modification in db2.table2",result.select("[MOD] db2").select("[REMOVED] table3"));
		assertNotNull("Modification in db2.table2",result.select("[MOD] db2").select("[ADDED] table4"));
		
		//assertEquals("Should have one item",1,result.getElements().length);
		//assertEquals("Select what is different","c2",result.getElements()[0].getSelector());
	} 
	
	@Test
	public void testStoreAndLoad() throws IOException {
		Collector c1 = new Collector("db1");
		c1.childCollector("table1").add("Data1".getBytes());
		c1.childCollector("table2").add("Data2".getBytes());
		c1.childCollector("table3").add("Data2".getBytes());
		
		Hash sn1 = c1.seal();
		
		File f = TOOLS.store(sn1);
		Hash releoaded = TOOLS.load(f);
		assertEquals(sn1,releoaded);
	}
}
