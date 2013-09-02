package dogtooth.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeIndex implements Hash {

	final private Hash m_tree;
	final private Map<String, TreeIndex> m_selectors;
	final private TreeIndex[] m_sub;

	
	public TreeIndex(Hash tree) {
		m_tree = tree;
		// build index on selectors
		m_selectors = new HashMap<String,TreeIndex>();
		for (Hash h : tree.getElements()) {
			TreeIndex idx = new TreeIndex(h); 
			if (h.getSelector() != null) {
				m_selectors.put(h.getSelector(), idx);
			}
		}
		List<TreeIndex> sub = new ArrayList<TreeIndex>();
		for (Hash h : tree.getElements()) {
			sub.add(new TreeIndex(h));
		}
		m_sub = sub.toArray(new TreeIndex[sub.size()]);
	}

	public TreeIndex select(String selector) {
		return m_selectors.get(selector);
	}
	
	public boolean selectable() {
		return m_tree.getSelector() != null;
	}

	@Override
	public String getHashValue() {
		return m_tree.getHashValue();
	}

	@Override
	public String getLabel() {
		return m_tree.getLabel();
	}

	@Override
	public String getSelector() {
		return m_tree.getSelector();
	}

	@Override
	public TreeIndex[] getElements() {
		return m_sub;
	}
	
	public String toString() {
		return "INDEX of " + m_tree.toString(); 
	}

}
