package dogtooth.tree;

public interface Tree {
	
	String getHashValue();
	
	String getSelector();

	Tree[] getElements();
	
	public long getEffectiveSize();
	
}
