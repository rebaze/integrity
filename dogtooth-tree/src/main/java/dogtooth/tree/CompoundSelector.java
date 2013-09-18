package dogtooth.tree;

public class CompoundSelector extends Selector {

    private final Selector[] m_selectors;
    
    public CompoundSelector( Selector... s ) {
        super( s.toString() );
        m_selectors = s;
    }

}
