package dogtooth.tree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import dogtooth.tree.internal.InMemoryTreeBuilderImpl;

public class TreeTools {
			
	private static final Logger LOG = LoggerFactory.getLogger(TreeTools.class);
	final private PrintStream m_out;

	public TreeTools() {
		this(System.out);
	}
	
	public TreeTools(PrintStream ps) {
		m_out = ps;
	}
	
	public File store(Tree tree) throws IOException {
		// persist tree
		File f = File.createTempFile("tree", ".xml");
		return store(tree,f);
	}
	
	public File store(Tree tree, File f) throws IOException {
		// persist tree
		XStream xstream = new XStream();
	    xstream.toXML(tree,new FileOutputStream(f));
	    LOG.debug("Stored tree to " + f.getAbsolutePath());
		return f;
	}

	public Tree load(File f) {
		XStream xstream = new XStream();
		return (Tree)xstream.fromXML(f);
	}
	
	public Tree load(String locationClasspath) {
		XStream xstream = new XStream();
		return (Tree)xstream.fromXML(this.getClass().getResourceAsStream(locationClasspath));
	}

	/**
	 */
	public Tree compare(Tree left, Tree right) {
		return compare(new TreeIndex(left),new TreeIndex(right));
	}
	
	/**
	 */
	public Tree compare(TreeIndex left, TreeIndex right) {
		TreeBuilder target = new InMemoryTreeBuilderImpl("Difference [" + left.getSelector() + " ] and [" + right.getSelector() + " ]-");
		compare(target, left, right);
		return target.seal();
	}
	

	/**
	 * 
	 */
	private void compare(TreeBuilder collector, TreeIndex left, TreeIndex right) {
		// Unfold "empty" elements.
		if (left == null) {
			for (TreeIndex tree : right.getElements()) {
				TreeBuilder mod = collector.childCollector("[ADDED] " + tree.getSelector());
				compare(mod,null,tree);
			}
			return;
		}
		if (right == null) {
			for (TreeIndex tree : left.getElements()) {
				TreeBuilder mod = collector.childCollector("[REMOVED] " + tree.getSelector());
				compare(mod,tree,null);
			}
			return;
		}
		
		if (!left.getHashValue().equals(right.getHashValue())) {
			// compare next level:
			TreeBuilder modification = collector.childCollector("[MOD] " + right.getSelector());
			for (TreeIndex tree : left.getElements()) {
				// first check if tree is a selectable node or not:
				if (tree.selectable()) {
					TreeIndex origin = right.select(tree.getSelector());
					if (origin == null) {
						// Deleted element:
						TreeBuilder removed = modification.childCollector("[REMOVED] " + tree.getSelector());
						// Not so sure..
						compare(removed, tree, origin);
					} else {
						// compare content:
						compare(modification, tree, origin);
					}

				} else {
					throw new TreeException("Item " + tree + " is not selectable.");
				}
			}
			// find new ones:
			for (TreeIndex tree : right.getElements()) {
				// first check if tree is a selectable node or not:
				if (tree.selectable()) {
					TreeIndex origin = left.select(tree.getSelector());
					if (origin == null) {
						// New element:
						TreeBuilder added = modification.childCollector("[ADDED] " + tree.getSelector());
						compare(added, origin, tree);
					} else {
						// already worked on.
					}

				} else {
					throw new TreeException("Item " + tree + " is not selectable.");
				}
			}
		}

	}

	public void displayTree(int depth, Tree dbHash) {
		if (depth == 0)
			m_out.println(" ---- TREE ----------");
		m_out.print("+");
		for (int i = 0; i < depth; i++) {
			m_out.print("--");
		}
		m_out.println(" " + dbHash.toString());
		depth++;
		Tree[] elements = dbHash.getElements();
		int count = (elements.length < 10) ? elements.length : 10;
		for (int i = 0;i<count;i++) {
			displayTree(depth, elements[i]);
		}
	}

	public void prettyPrint(int depth, Tree dbHash) {
		if (depth == 0)
			m_out.println(" ---- TREE ----------");
		m_out.print("+");
		for (int i = 0; i < depth; i++) {
			m_out.print("--");
		}
		m_out.println(" " + dbHash.getSelector());
		depth++;
		for (Tree sub : dbHash.getElements()) {
			prettyPrint(depth, sub);
		}

	}
}
