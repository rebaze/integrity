/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package dogtooth.tree;

public class Selector {
    
    final private String m_name;
    
    public static Selector selector(String s) {
        return new Selector( s );
    }
    
    public Selector(String s) {
        assert (s != null);
        m_name = s;
    }
    
    public String name() {
        return m_name;
    }
    
    public int hashCode() {
        return m_name.hashCode();       
    }
    
    public boolean equals(Object other) {
        if (other instanceof Selector) {
            Selector ot = (Selector)other;
            return m_name.equals( ot.name() );
        }
        return false;
    }
}
