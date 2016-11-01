package org.rebaze.integrity.tree.util;

import org.rebaze.integrity.tree.TreeSessionFactory;
import org.rebaze.integrity.tree.TreeSession;

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
