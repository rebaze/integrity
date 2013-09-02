package dogtooth.tree;

public class TreeAlreadySealedException extends TreeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TreeAlreadySealedException(String msg,Exception e) {
		super(msg,e);
	}
	
	public TreeAlreadySealedException(String msg) {
		super(msg);
	}
}
