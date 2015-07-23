/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package com.rebaze.commons.tree;

/**
 * Thrown when {@link TreeBuilder} has been modified (attempted) when seal() has already been
 * called.
 * 
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public class TreeAlreadySealedException extends TreeException
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public TreeAlreadySealedException( String msg, Exception e )
    {
        super( msg, e );
    }

    public TreeAlreadySealedException( String msg )
    {
        super( msg );
    }
}
