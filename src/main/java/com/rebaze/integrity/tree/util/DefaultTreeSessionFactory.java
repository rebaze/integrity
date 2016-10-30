package com.rebaze.integrity.tree.util;

import com.rebaze.integrity.tree.TreeSessionFactory;
import com.rebaze.integrity.tree.TreeSession;

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
