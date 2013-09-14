package dogtooth.tree;

public class TreeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TreeException(String msg, Exception e) {
		super(msg, e);
	}

	public TreeException(String msg) {
		super(msg);
	}
}
