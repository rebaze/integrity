/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree;

/**
 * General Unchecked exception for the Tree API.
 * 
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public class TreeException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public TreeException( String msg, Exception e )
    {
        super( msg, e );
    }

    public TreeException( String msg )
    {
        super( msg );
    }
}
