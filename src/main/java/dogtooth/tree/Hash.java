package dogtooth.tree;

public interface Hash {
	String getHashValue();

	String getLabel();
	
	String getSelector();

	Hash[] getElements();
	
	public long getEffectiveSize();
	
}
