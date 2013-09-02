package dogtooth.tree.internal;

import static org.junit.Assert.*;

import org.junit.Test;

import dogtooth.tree.Hash;

public class DefaultHashTest {
	
	@Test
	public void equalityTest() {
		Hash sn1 = new Collector("c1").childCollector("d").add("Some".getBytes()).seal();
		Hash sn2 = new Collector("c1").childCollector("d").add("Some".getBytes()).seal();
		Hash sn3 = new Collector("c1").childCollector("d2").add("Other".getBytes()).seal();
		
		assertEquals("Should be identical",sn1,sn2);
		assertNotEquals("Should be identical",sn1,sn3);
		assertNotEquals("Should be identical",sn2,sn3);
		assertEquals("Should be identical",sn3,sn3);
	} 
	
	@Test
	public void equalityTestBeAwareThatHashesArePrimary() {
		Hash sn1 = new Collector("Here").childCollector("whatnow").add("Some".getBytes()).seal();
		Hash sn2 = new Collector("There").childCollector("d").add("Some".getBytes()).seal();
		assertEquals("Must be identical",sn1,sn2);
	} 
}
