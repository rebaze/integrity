package org.auxis.core;
import java.net.URI;

/**
 * Meta data for a {@link DataBundle}.
 */
public interface DataBundleMetadata
{
    String version();

    URI origin();

    //DataLineage lineage();

}
