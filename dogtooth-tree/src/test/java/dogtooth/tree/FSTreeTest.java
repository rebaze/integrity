package dogtooth.tree;

import static dogtooth.tree.Selector.selector;
import static dogtooth.tree.util.TreeCompare.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dogtooth.tree.internal.InMemoryTreeBuilderImpl;
import dogtooth.tree.util.StreamTreeBuilder;
import dogtooth.tree.util.TreeConsoleFormatter;
import dogtooth.tree.util.TreeTools;

public class FSTreeTest {
    private final static Logger LOG = LoggerFactory.getLogger(FSTreeTest.class);

    private final TreeTools TOOLS = new TreeTools();
    private final TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();
    
    @Test
    public void testLoading() throws IOException {
        TreeBuilder tb = new InMemoryTreeBuilderImpl("FS");
        File start = new File(".");
        LOG.info("Indexing " + start.getAbsolutePath() + " ..");
        collect(tb,start);
        Tree result = tb.seal();
        File f = new File("target/store.xml");
        if (f.exists()) {
            LOG.info("Comparing to existing snapshot @ " + f.getAbsolutePath() + " ..");
            Tree old = TOOLS.load( f );
            Tree diff = compare( old, result );
            FORMAT.prettyPrint( 0, diff );
        }else {
            TOOLS.store( result,f );
            LOG.info("Indexed " + start.getAbsolutePath() + " @ " + f.getAbsolutePath() + " as Tree: " + result);
        }
       
        // assertEquals(73,result.effectiveSize());
        // assertEquals(3,result.branches().length);
    }

    private void collect( TreeBuilder builder, File base ) {
        for (File f: base.listFiles() ) {
            if (f.isHidden()) continue;
            TreeBuilder sub = builder.branch( selector(f.getName()) );
            if (f.isDirectory()) collect(sub,f);else {
                new StreamTreeBuilder( sub ).add(f);
            }
       }
    }
}
