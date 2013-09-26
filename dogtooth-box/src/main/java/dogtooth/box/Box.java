package dogtooth.box;

import dogtooth.tree.Tree;
import dogtooth.tree.TreeBuilder;
import static dogtooth.tree.Selector.*;
import dogtooth.tree.internal.InMemoryTreeBuilderImpl;

/**
 * "Intelligent Box" is here to solve the so called assembly problem.
 * 
 * Assemblies are used to be solid forks built at some point in time.
 * There is no pointer to its origin, no integral way of syncing fat assemblies when there are
 * only small modifications.
 * 
 * At its simpliest way a Box is a list of "things".
 * It can be created from a variety of sources.
 * 
 * Boxes can be send over the network in an efficient way since boxes are always made out of smaller pieces (hence: Tree).
 * 
 * You can ask Box to sync with another Box in an efficient way (partial update).
 * You also can ask Box to 
 * 
 * @author tonit
 *
 */
public class Box {
    private static final String ROOT = "Box";
    private Tree m_content;
    
    public Box(Walker walker) {
        TreeBuilder tb = new InMemoryTreeBuilderImpl( selector ( ROOT ) );
        walker.visit(tb);
        m_content = tb.seal();
    }
    
    public Box() {
        // TODO Auto-generated constructor stub
    }

    public boolean hasTree(Tree tree) {
        return false;
    }
}
