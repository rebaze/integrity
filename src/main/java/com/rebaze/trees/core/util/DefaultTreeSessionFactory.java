package com.rebaze.trees.core.util;

import com.rebaze.trees.core.TreeSessionFactory;

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
