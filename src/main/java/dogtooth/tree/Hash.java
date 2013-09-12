package dogtooth.tree;

public interface Hash {
	String getHashValue();
	
	String getSelector();

	Hash[] getElements();
	
	public long getEffectiveSize();
	
}
