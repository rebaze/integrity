package dogtooth.tree;

public interface TreeBuilder {

	public TreeBuilder add(byte[] bytes);

	//public TreeBuilder childCollector();

	public TreeBuilder childCollector(String selector);

	public TreeBuilder setSelector(String selector);
	
	public Tree seal();

}