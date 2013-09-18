package dogtooth.tree;

import static dogtooth.tree.Selector.selector;

import java.io.IOException;

import org.junit.Test;

import static dogtooth.tree.annotated.Tag.*;
import static org.junit.Assert.*;
import dogtooth.tree.internal.InMemoryTreeBuilderImpl;

public class TreeIndexTest {
    
    @Test
    public void compoundSelectArrayTest() throws IOException {
        TreeIndex sn1 = sampleTree();
        assertEquals(tag("findme"),sn1.select( selector("table1","other") ).tags());  
        assertNull(sn1.select( selector("table1","wrong")));
        assertNull(sn1.select( selector("table2")));  
    }

    private TreeIndex sampleTree() {
        TreeBuilder c1 = new InMemoryTreeBuilderImpl( "db1" );
        c1.branch( selector ("table1" ) ).branch(selector ("other")).add("Data1".getBytes()).tag( tag("findme") );
        TreeIndex sn1 = new TreeIndex(c1.seal());
        return sn1;
    }
}
