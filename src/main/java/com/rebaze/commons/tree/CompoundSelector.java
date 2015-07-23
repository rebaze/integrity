/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package com.rebaze.commons.tree;

public class CompoundSelector extends Selector
{
    private final Selector[] m_selectors;

    public CompoundSelector( Selector... s )
    {
        super( s.toString() );
        m_selectors = s;
    }

}
