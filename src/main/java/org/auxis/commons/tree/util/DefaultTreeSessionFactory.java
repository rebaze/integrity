package org.auxis.commons.tree.util;

import org.auxis.commons.tree.TreeSessionFactory;

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
