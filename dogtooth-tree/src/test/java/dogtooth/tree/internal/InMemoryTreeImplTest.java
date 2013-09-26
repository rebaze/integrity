package dogtooth.tree.internal;

import static org.junit.Assert.*;

import org.junit.Test;

import dogtooth.tree.TreeBuilder;
import dogtooth.tree.util.TreeTools;
import static dogtooth.tree.Selector.*;

public class InMemoryTreeImplTest {
	private TreeTools tools = new TreeTools();
	
    @Test
    public void testEmptyTreeSize() {
    	
        assertEquals( "Trunk only", 1, tools.nodes( new InMemoryTreeBuilderImpl( selector( "foo" ) ).seal()) );
    }

    @Test
    public void testAddsOnlyOnSingle() {
        assertEquals( "Trunk only", 1, tools.nodes(new InMemoryTreeBuilderImpl( selector( "foo" ) ).add( "data".getBytes() ).seal() ));
    }

    @Test
    public void testSingleBranch() {
        assertEquals( 1, tools.nodes(new InMemoryTreeBuilderImpl( selector( "foo" ) ).branch( selector( "branch" ) ).seal()) );
    }

    @Test
    public void testMore() {
        TreeBuilder tb = new InMemoryTreeBuilderImpl( selector( "trunk" ) );
        tb.branch( selector( "branch2" ) ).add( "data1".getBytes() );
        tb.branch( selector( "branch3" ) ).add( "data1".getBytes() );
        assertEquals( 3,tools.nodes( tb.seal()) );
    }
}
