package org.rebaze.integrity.view.api;

import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.TreeSession;

/**
 * Created by tonit on 21/11/2016.
 */
public interface TreeMapper<T>
{
    TreeSession getSession();

    // This is creating the sub criteria based tree from a person.
    default Tree mask( T[] list )
    {
        TreeBuilder tb = getSession().createTreeBuilder();
        for (T p : list)
        {
            tb.branch( mask(p) );
        }
        return tb.seal();
    }

    Tree mask( T item );

}
