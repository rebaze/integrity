package org.rebaze.integrity.view;

import lombok.EqualsAndHashCode;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeIndex;
import org.rebaze.integrity.tree.api.TreeSession;


@EqualsAndHashCode
public class TreeViewDefinition<T> implements ViewDefinition
{
    private final TreeIndex tree;

    @Override
    public boolean match( Tree p )
    {
        return tree.contains( p );
    }

    @Override public int size()
    {
        return tree.branches().length;
    }

    public TreeViewDefinition( T[] items, TreeMapper<T> mapper )
    {
        this.tree = TreeSession.wrapAsIndex( mapper.mask(items) );
    }


}
