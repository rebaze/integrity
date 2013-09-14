package dogtooth.tree;

import static org.junit.Assert.*;

import org.junit.Test;

import dogtooth.tree.internal.InMemoryTreeBuilderImpl;

public class SimpleHashTest {
	
	@Test
	public void emptyCollector() throws Exception {
		
		TreeBuilder root = new InMemoryTreeBuilderImpl("Root");
		assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709",root.seal().getHashValue());
		
		TreeBuilder root2 = new InMemoryTreeBuilderImpl("RootOther");
		assertEquals(root.seal().getHashValue(),root2.seal().getHashValue());
		
	}

	@Test
	public void singleElements() throws Exception {	
		String[] elements = new String[] { "element1","element2"};
		TreeBuilder root = new InMemoryTreeBuilderImpl("Root");
		root.add(elements[0].getBytes());
		root.add(elements[1].getBytes());
		assertEquals("2a190d88c7a164f242ea707acf5d57bc990f0ce1",root.seal().getHashValue());
		
		String[] elementsOther = new String[] { "foo","element2"};
		TreeBuilder root2 = new InMemoryTreeBuilderImpl("Root");
		root2.add(elementsOther[0].getBytes());
		root2.add(elementsOther[1].getBytes());
		assertEquals("0dc0638a504e1b0415a37340bf57d14b75b14308",root2.seal().getHashValue());
	}
	
	@Test
	public void simpleTreeTest() throws Exception {	
		TreeBuilder root = new InMemoryTreeBuilderImpl("Root1");
		TreeBuilder sub1 = root.childCollector("child1");
		sub1.add("One".getBytes());
		sub1.add("Two".getBytes());
		
		TreeBuilder sub2 = root.childCollector("child2");
		sub2.add("Three".getBytes());
		sub2.add("Four".getBytes());
		Tree tree1 = root.seal();
		
		TreeBuilder root2 = new InMemoryTreeBuilderImpl("Root2");
		TreeBuilder sub3 = root2.childCollector("child3");
		sub3.add("Different".getBytes());
		sub3.add("Two".getBytes());
		
		TreeBuilder sub4 = root2.childCollector("child4");
		sub4.add("Three".getBytes());
		sub4.add("Four".getBytes());
		Tree tree2 = root2.seal();
		
		assertNotEquals("roots must be different",tree1.getHashValue(),tree2.getHashValue());
		assertNotEquals("first childs must be different",tree1.getElements()[0].getHashValue(),tree2.getElements()[0].getHashValue());
		assertEquals("next childs must be identical",tree1.getElements()[1].getHashValue(),tree2.getElements()[1].getHashValue());
		
	}
}
