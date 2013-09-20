package dogtooth.tree.internal;

import static org.junit.Assert.*;


import org.junit.Test;
import static dogtooth.tree.Selector.*;

public class InMemoryTreeImplTest {
    
    @Test
    public void testEmptyTreeSize() {
        assertEquals("Empty tree should have size 0",0, new InMemoryTreeBuilderImpl(selector("foo")).seal().effectiveSize());
    }
    
    @Test
    public void testAddsOnlyOnSingle() {
        assertEquals("Empty tree should have size 0",0, new InMemoryTreeBuilderImpl(selector("foo")).add( "data".getBytes() ).seal().effectiveSize());
    }
    
    @Test
    public void testSingleBranch() {
        assertEquals( 1, new InMemoryTreeBuilderImpl(selector("foo")).branch( selector("branch") ).seal().effectiveSize());
    }
}
