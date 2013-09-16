/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
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
