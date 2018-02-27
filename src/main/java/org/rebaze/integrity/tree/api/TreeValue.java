package org.rebaze.integrity.tree.api;

import java.util.Objects;

public final class TreeValue
{
    private final String hash;
    private final HashAlgorithm algo;

    public TreeValue( HashAlgorithm algorithm, String hash )
    {
        this.algo = algorithm;
        this.hash = hash;
    }

    public String hash()
    {
        return hash;
    }

    public HashAlgorithm algorithm()
    {
        return algo;
    }

    @Override public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        TreeValue treeValue = ( TreeValue ) o;
        return Objects.equals( hash, treeValue.hash ) &&
            algo == treeValue.algo;
    }

    @Override public int hashCode()
    {

        return Objects.hash( hash, algo );
    }
}
