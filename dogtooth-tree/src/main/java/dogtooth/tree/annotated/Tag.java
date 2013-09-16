package dogtooth.tree.annotated;

public class Tag {
    final private String[] m_tag;
    
    public static Tag tag(String...tags) {
        return new Tag(tags);
    }
    
    public Tag(String... tag) {
        m_tag = tag;
    }
    
    public boolean contains(Tag other) {
        return false;
    }
}
