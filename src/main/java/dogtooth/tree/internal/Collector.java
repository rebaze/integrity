package dogtooth.tree.internal;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dogtooth.tree.Hash;
import dogtooth.tree.TreeAlreadySealedException;
import dogtooth.tree.TreeException;

public class Collector {
	private final static Logger LOG = LoggerFactory.getLogger(Collector.class);
	private final MessageDigest m_digest;
	private Hash m_hash;
	private boolean m_sealed = false;
	final private List<Collector> m_sub;
	final private String m_label;
	private String m_selector;
	
	public Collector(String label) {
		try {
		m_digest = MessageDigest.getInstance("SHA-1");
		m_sub = new ArrayList<Collector>();
		m_label = label;
		m_selector = null;
		}catch (Exception e) {
			throw new TreeException("Problem creating collector for " + label,e);
		}
	}
	
	synchronized public Collector add(byte[] bytes) {
		if (m_sealed) throw new TreeAlreadySealedException("No modification on a sealed tree!");
		m_digest.update(bytes);
		return this;
	}

	synchronized public Hash seal() {
		if (!m_sealed) {
			// collect all sub elements..
			autosetSelector();
			List<Hash> subHashes = new ArrayList<Hash>(m_sub.size());
			for (Collector c : m_sub) {
				Hash subHash = c.seal();
				subHashes.add(subHash);
				add(subHash.getHashValue().getBytes());
			}
			m_hash = new DefaultHash(m_selector, m_label,convertToHex(m_digest.digest()),subHashes.toArray(new Hash[subHashes.size()]));
			m_sealed = true;
		}
		return m_hash;
	}
	
	private void autosetSelector() {
		if (m_selector == null) {
			//LOG.warn("Autosetting selector to " + m_label);
			m_selector = m_label;
		}
	}

	synchronized public Collector childCollector(String label) {
		if (m_sealed) throw new TreeAlreadySealedException("No modification on a sealed tree!");
		Collector c = new Collector(label);
		m_sub.add(c);
		return c;
	}
	
	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public Collector setSelector(String selector) {
		m_selector = selector;
		return this;
	}
}
