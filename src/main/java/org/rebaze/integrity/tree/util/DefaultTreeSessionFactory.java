package org.rebaze.integrity.tree.util;

import org.rebaze.integrity.tree.api.TreeCombiner;
import org.rebaze.integrity.tree.api.TreeSessionFactory;
import org.rebaze.integrity.tree.api.TreeSession;
import org.rebaze.integrity.tree.internal.operators.*;

/**
 * Standard impl without any DI fw.
 */
public class DefaultTreeSessionFactory implements TreeSessionFactory
{
    private static final String DEFAULT_HASH_ALOGO = "SHA-1";

    public TreeSession create()
    {
        return new TreeSession(DEFAULT_HASH_ALOGO);
    }

    public TreeSession create(String messageDigestAlgorithm)
    {
        return new TreeSession(messageDigestAlgorithm);
    }

    public TreeCombiner createDeltaTreeCombiner( TreeSession session ) {
        return new DeltaTreeCombiner( session );
    }

    public TreeCombiner createDiffTreeCombiner( TreeSession session ) {
        return new DiffTreeCombiner( session );
    }

    public TreeCombiner createIntersectionTreeCombiner( TreeSession session ) {
        return new IntersectTreeCombiner( session );
    }

    public TreeCombiner createSubstructTreeCombiner( TreeSession session ) {
        return new SubstructCombiner( session );
    }

    public TreeCombiner createUnionTreeCombiner( TreeSession session ) {
        return new UnionTreeCombiner( session );
    }
}
