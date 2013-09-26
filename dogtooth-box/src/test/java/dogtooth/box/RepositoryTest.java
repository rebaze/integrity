package dogtooth.box;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class RepositoryTest {
    @Test
    public void createBoxFromLocalMavenRepo() {
        FSWalker walker = new FSWalker(new File("/Users/tonit/.m2/repository"));
  
        Box box1 = new Box(walker);
        Box box2 = new Box(); // empty one.
        
        assertNotEquals("box2 has not same content as box1",box1, box2);

        BoxSyncAgent agent = new BoxSyncAgent();
        agent.sync(box1,box2);
        
        assertEquals("box2 has same content as box1",box1, box2);
    }
}
