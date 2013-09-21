package dogtooth.tree.internal;

import static org.junit.Assert.*;

import org.junit.Test;

import dogtooth.tree.TreeBuilder;
import static dogtooth.tree.Selector.*;

public class InMemoryTreeImplTest {

    @Test
    public void testEmptyTreeSize() {
        assertEquals( "Trunk only", 1, new InMemoryTreeBuilderImpl( selector( "foo" ) ).seal().effectiveSize() );
    }

    @Test
    public void testAddsOnlyOnSingle() {
        assertEquals( "Trunk only", 1, new InMemoryTreeBuilderImpl( selector( "foo" ) ).add( "data".getBytes() ).seal().effectiveSize() );
    }

    @Test
    public void testSingleBranch() {
        assertEquals( 1, new InMemoryTreeBuilderImpl( selector( "foo" ) ).branch( selector( "branch" ) ).seal().effectiveSize() );
    }

    @Test
    public void testMore() {
        TreeBuilder tb = new InMemoryTreeBuilderImpl( selector( "trunk" ) );
        tb.branch( selector( "branch2" ) ).add( "data1".getBytes() );
        tb.branch( selector( "branch3" ) ).add( "data1".getBytes() );
        assertEquals( 2, tb.seal().effectiveSize() );
    }
}
