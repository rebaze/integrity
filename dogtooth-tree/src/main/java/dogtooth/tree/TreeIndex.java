/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package dogtooth.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dogtooth.tree.annotated.Tag;

/**
 * Index implementation that augments a given tree with some extra index accessors like find by selector and tag.
 * 
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public class TreeIndex implements Tree {

	final private Tree m_tree;
	final private Map<Selector, TreeIndex> m_selectors = new HashMap<Selector,TreeIndex>();
	final private TreeIndex[] m_sub;

	public TreeIndex(Tree tree) {
		if (tree instanceof TreeIndex) {
			throw new RuntimeException("You should not wrap a TreeIndex.. waaaayyy too expensive..");
		}
		m_tree = tree;
		// build index on selectors
		List<TreeIndex> sub = new ArrayList<TreeIndex>();
		
		for (Tree h : tree.branches()) {
			TreeIndex idx = new TreeIndex(h); 
			if (h.selector() != null) {
				m_selectors.put(h.selector(), idx);
			}
			sub.add(idx);
		}
		m_sub = sub.toArray(new TreeIndex[sub.size()]);
	}

	public TreeIndex select(Selector selector) {
        return m_selectors.get( selector );
    }
	
	public TreeIndex select(Selector... selectors) {
	    TreeIndex r = this;
	    for (Selector s : selectors) {
	        r = r.select( s );
	    }
		return r;
	}
	
	public boolean selectable() {
		return m_tree.selector() != null;
	}

	@Override
	public String fingerprint() {
		return m_tree.fingerprint();
	}

	@Override
	public Selector selector() {
		return m_tree.selector();
	}

	@Override
	public TreeIndex[] branches() {
		return m_sub;
	}
		
	public String toString() {
		return "TreeIndex of [" + m_tree.toString() + "] with selectors: " + m_selectors.size();
	}

    @Override
    public Tag tags() {
        return m_tree.tags();
    }

}
