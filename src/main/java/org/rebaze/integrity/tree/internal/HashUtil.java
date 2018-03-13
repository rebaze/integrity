package org.rebaze.integrity.tree.internal;

import java.util.Comparator;

import org.rebaze.integrity.tree.api.Tree;

/**
 * Created by tonit on 19/03/15.
 */
public class HashUtil
{
    public static String convertToHex( byte[] data )
    {
        StringBuilder buf = new StringBuilder();
        for ( int i = 0; i < data.length; i++ )
        {
            int halfbyte = ( data[i] >>> 4 ) & 0x0F;
            int two_halfs = 0;
            do
            {
                if ( ( 0 <= halfbyte ) && ( halfbyte <= 9 ) )
                {
                    buf.append( ( char ) ( '0' + halfbyte ) );
                }
                else
                {
                    buf.append( ( char ) ( 'a' + ( halfbyte - 10 ) ) );
                }
                halfbyte = data[i] & 0x0F;
            }
            while ( two_halfs++ < 1 );
        }
        return buf.toString();
    }

    public static class SelectorTreeComparator implements Comparator<Tree>
    {
        @Override public int compare( Tree left, Tree right )
        {
            return left.selector().name().compareTo( right.selector().name() );
        }
    }

    public static class FingerprintTreeComparator implements Comparator<Tree>
    {
        @Override public int compare( Tree left, Tree right )
        {
            return left.value().hash().compareTo( right.value().hash() );
        }
    }
}
