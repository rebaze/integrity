package dogtooth.tree.internal;

import dogtooth.tree.Hash;
import dogtooth.tree.TreeTools;

public class DefaultHash implements Hash {

	//final private String m_label;
	final private String m_hashValue;
	final private Hash[] m_subs;
	final private String m_selector;
	final private long m_size;

	public DefaultHash(  String selector, String label, String hashValue,Hash[] subs) {
		//m_label = label;
		m_selector = selector;
		m_hashValue = hashValue;
		m_subs = subs;
		long total = 1; // self
		for (Hash h : subs) {
			total += h.getEffectiveSize();
		}
		m_size = total;
	}
	
	@Override
	public String getHashValue() {
		return m_hashValue;
	}

	@Override
	public String getLabel() {
		return "NOT SET";//m_label;
	}
	
	@Override
	public String getSelector() {
		return m_selector;
	}

	@Override
	public Hash[] getElements() {
		return m_subs;
	}
	
	public String toString() {
		return m_hashValue.substring(0,6) + " /Label: " + getLabel() + " /Selector: " + m_selector + " /Children: " + m_subs.length + " /Total: " + m_size; 
	}
	
	public int hashCode() {
		return m_hashValue.hashCode();
	}
	
	// implement compares using our own tools
	public boolean equals(Object other) {
		if (other instanceof Hash ) {
			Hash sn2 = (Hash)other;
			System.out.println("Comparing: " + this + " and " + sn2);
			return (new TreeTools().compare(this, sn2).getElements().length == 0);
		}else {
			throw new RuntimeException("Should not come here..");
		}
	}

	@Override
	public long getEffectiveSize() {
		return m_size;
	}
	
	

}
