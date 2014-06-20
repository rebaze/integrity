package dogtooth.tree;

import dogtooth.tree.util.StreamTreeBuilder;
import dogtooth.tree.util.TreeConsoleFormatter;
import dogtooth.tree.util.TreeTools;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static dogtooth.tree.Selector.selector;

public class FSTreeTest {
    private final static Logger LOG = LoggerFactory.getLogger(FSTreeTest.class);

    private final TreeTools TOOLS = new TreeTools();
    private final TreeConsoleFormatter FORMAT = new TreeConsoleFormatter();


    @Test
    public void testLoading() throws IOException {
        TreeBuilder tb = TOOLS.createTreeBuilder().selector(selector("FS"));
        File indexFolder = new File(".");
        LOG.info("Indexing " + indexFolder.getAbsolutePath() + " ..");
        long start = System.currentTimeMillis();

        collect(tb,indexFolder);
        LOG.info("Collected. Sealing..");

        Tree result = tb.seal();
        LOG.info("Sealed: " + result.fingerprint());

        long stop = System.currentTimeMillis();
        LOG.info("Indexed "+TOOLS.nodes(result)+" nodes in " + (stop-start) + "ms");
        File f = new File("target/store.xml");

        if (f.exists()) {
            LOG.info("Comparing to existing snapshot @ " + f.getAbsolutePath() + " ..");
            Tree old = TOOLS.load( f );
            Tree diff = TOOLS.compare(old, result);
            FORMAT.prettyPrint( 0, diff );
        }else {
            f.getParentFile().mkdirs();
            TOOLS.store( result,f );
            LOG.info("Indexed " + indexFolder.getAbsolutePath() + " @ " + f.getAbsolutePath() + " as Tree: " + result);
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
