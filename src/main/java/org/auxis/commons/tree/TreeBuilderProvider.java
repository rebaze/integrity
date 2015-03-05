package org.auxis.commons.tree;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

public interface TreeBuilderProvider
{
    TreeBuilder create();
}
