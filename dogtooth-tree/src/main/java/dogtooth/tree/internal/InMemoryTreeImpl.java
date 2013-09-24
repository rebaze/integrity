/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package dogtooth.tree.internal;

import dogtooth.tree.Selector;
import dogtooth.tree.Tree;
import dogtooth.tree.annotated.Tag;
import dogtooth.tree.util.TreeTools;
import static dogtooth.tree.util.TreeCompare.*;

/**
 * Default implementation not really suitable for very large trees but fast and simple.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public class InMemoryTreeImpl implements Tree {

	final private String m_hashValue;
	final private Tree[] m_subs;
	final private Selector m_selector;
	final private long m_size;
    final private Tag m_tag;

	public InMemoryTreeImpl( Selector selector, String hashValue,Tree[] subs, Tag tag) {
		m_selector = selector;
		m_hashValue = hashValue;
		m_subs = subs;
		m_tag = tag;
		long total = (subs.length == 0) ? total =1 : 0;
		for (Tree h : subs) {
			total += h.effectiveSize();
		}
		m_size = total;
	}
	
	@Override
	public String fingerprint() {
		return m_hashValue;
	}
	
	@Override
	public Selector selector() {
		return m_selector;
	}

	@Override
	public Tree[] branches() {
		return m_subs;
	}
	
	public String toString() {
		return m_hashValue.substring(0,6) + " /Selector: " + m_selector + " /Children: " + m_subs.length + " /Total: " + m_size; 
	}
	
	public int hashCode() {
		return m_hashValue.hashCode();
	}
	
	public boolean equals(Object other) {
		if (other instanceof Tree ) {
			Tree sn2 = (Tree)other;
			Tree compare = compare( this, sn2 );
            return (compare.branches().length == 0);
		}else {
			throw new RuntimeException("Should not come here..");
		}
	}

	@Override
	public long effectiveSize() {
		return m_size;
	}

    @Override
    public Tag tags() {
        return m_tag;
    }
}
