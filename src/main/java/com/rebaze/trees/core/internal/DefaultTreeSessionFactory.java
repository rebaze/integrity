package com.rebaze.trees.core.internal;

import com.rebaze.trees.core.TreeSessionFactory;
import com.rebaze.trees.core.TreeSession;

/**
 * Standard impl without any DI fw.
 */
public class DefaultTreeSessionFactory implements TreeSessionFactory
{
    public TreeSession create()
    {
        return new TreeSession();
    }
}
