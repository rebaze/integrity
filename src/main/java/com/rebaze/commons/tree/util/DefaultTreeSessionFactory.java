package com.rebaze.commons.tree.util;

import com.rebaze.commons.tree.TreeSessionFactory;

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
