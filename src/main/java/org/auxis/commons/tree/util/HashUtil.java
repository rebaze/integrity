package org.auxis.commons.tree.util;

import org.auxis.commons.tree.Tree;

import java.util.Comparator;

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

    public static class TreeComparator implements Comparator<Tree>
    {
        @Override public int compare( Tree left, Tree right )
        {
            return left.fingerprint().compareTo( right.fingerprint() );
        }
    }
}
