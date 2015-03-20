/*
 * Copyright (c) 2015 rebaze GmbH
 * All rights reserved.
 *
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
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
