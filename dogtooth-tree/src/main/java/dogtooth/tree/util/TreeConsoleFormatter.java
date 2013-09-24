package dogtooth.tree.util;

import java.io.PrintStream;

import dogtooth.tree.Tree;

public class TreeConsoleFormatter {
    
    public TreeConsoleFormatter() {
        this( System.out );
    }
    
    final private PrintStream m_out;

    public TreeConsoleFormatter( PrintStream ps ) {
        m_out = ps;
    }

    
    public void displayTree( int depth, Tree dbHash ) {
        if( depth == 0 )
            m_out.println( " ---- TREE ----------" );
        m_out.print( "+" );
        for( int i = 0; i < depth; i++ ) {
            m_out.print( "--" );
        }
        m_out.println( " " + dbHash.toString() );
        depth++;
        Tree[] elements = dbHash.branches();
        int count = (elements.length < 10) ? elements.length : 10;
        for( int i = 0; i < count; i++ ) {
            displayTree( depth, elements[i] );
        }
    }

    public void prettyPrint( int depth, Tree dbHash ) {
        if( depth == 0 )
            m_out.println( " ---- TREE: Total size: " + dbHash.effectiveSize());
        m_out.print( "+" );
        for( int i = 0; i < depth; i++ ) {
            m_out.print( "--" );
        }
        m_out.println( " " + dbHash.tags() + " "+ dbHash.selector() );
        depth++;
        for( Tree sub : dbHash.branches() ) {
            prettyPrint( depth, sub );
        }
    }
}
