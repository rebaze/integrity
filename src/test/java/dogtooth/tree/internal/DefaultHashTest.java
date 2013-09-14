package dogtooth.tree.internal;

import static org.junit.Assert.*;

import org.junit.Test;

import dogtooth.tree.Tree;

public class DefaultHashTest {
	
	@Test
	public void equalityTest() {
		Tree sn1 = new InMemoryTreeBuilderImpl("c1").childCollector("d").add("Some".getBytes()).seal();
		Tree sn2 = new InMemoryTreeBuilderImpl("c1").childCollector("d").add("Some".getBytes()).seal();
		Tree sn3 = new InMemoryTreeBuilderImpl("c1").childCollector("d2").add("Other".getBytes()).seal();
		
		assertEquals("Should be identical",sn1,sn2);
		assertNotEquals("Should be identical",sn1,sn3);
		assertNotEquals("Should be identical",sn2,sn3);
		assertEquals("Should be identical",sn3,sn3);
	} 
	
	@Test
	public void equalityTestBeAwareThatHashesArePrimary() {
		Tree sn1 = new InMemoryTreeBuilderImpl("Here").childCollector("whatnow").add("Some".getBytes()).seal();
		Tree sn2 = new InMemoryTreeBuilderImpl("There").childCollector("d").add("Some".getBytes()).seal();
		assertEquals("Must be identical",sn1,sn2);
	} 
}
