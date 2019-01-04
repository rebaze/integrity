[<img src="http://www.rebaze.com/assets/Rebaze_icon_colors_tbg.png" align="right" width="100">](http://rebaze.com)

[![OSGi compatible](https://img.shields.io/badge/OSGi-compatible-green.svg)](http://www.osgi.org)
[![Build Status](https://travis-ci.org/rebaze/integrity.svg?branch=master)](https://travis-ci.org/rebaze/integrity)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.rebaze.integrity/org.rebaze.integrity.tree/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.rebaze.integrity/org.rebaze.integrity.tree)
[![Apache 2.0](https://img.shields.io/github/license/nebula-plugins/nebula-publishing-plugin.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# News

## 2019-01-04 Release is coming

It has been a while since the library got a release to Maven Central. Now, it will be time again. Expect a new release this January.

# What is this?

A small Java library for creating Merkle Trees (https://en.m.wikipedia.org/wiki/Merkle_tree) to be used in blockchain-like technologies.

- Composite hash- tree based java library (DAG)
- OSGi compatible
- Embeddable
- Low footprint 

## What is this good for

- Blockchain and Hyperledger Technologies
- Indexing arbitrary (nested) data based on hashes and metadata (called selectors and tags here)

## How do i use it?

The most up-to-date code that actually works can be found in this projects test folder. 
But it is usually helpful to have a first glance at the api and how it can be used.

### Initializing the TreeSession instance

This creates the main entry for anything in this library. 
It is called a "Session" because it encapsulates things you want to have consistent
when creating and using the Hash-Trees like hash algorithm used, 
kind of TreeBuilder (currently in-memory only), and potentially shared instances
such as MessageDigest.

An instance can be stored (when using the DefaultTreeSessionFactory) as private final static field in a class).

### Lets create a hash tree

````
TreeSession session =  new DefaultTreeSessionFactory().create();
````

This creates the probably smalles tree possible without any meta-data attached to it.
The final "seal()" method on TreeBuilder actually creates hashes of all sub-elements (hence tree)
and returns the composite Tree (value object, immutable).
This Tree can be shared, stored and searched (using IndexTree, see below, for example).

````
Tree myFirstTree = session.createTreeBuilder()
            .add("anydata")
            .seal();
````

That first tree basically only gives you the (by default SHA-1) hash of "anydata". Not a big leap by itself.

What happens when we add more "data"?
````
Tree treeWithoutBranches = session.createTreeBuilder()
            .add("anydata".getBytes())
            .add("moredata".getBytes())
            .seal();
````
This by itself might be useful, probably it is not. What it does is it just adds "moredata" to the single Tree node.
The resulting tree will just be the SHA-1 of "anydata" + "moredata" without any branches.

### What are branches?

Branches are sub-nodes of a tree. They are tree-branches. Branches are a way to structure (hashed) data so that 
you can read the sub-hashes after building the tree.
Note that the tree above won't have any branches when you call 
```treeWithoutBranches.branches()```.

How do you create branches?
````
TreeBuilder treeBuilder = session.createTreeBuilder();
treeBuilder.branch( selector("datapointA") ).add("anydata".getBytes());
treeBuilder.branch( selector("datapointB") ).add("otherdata".getBytes());
Tree tree = treeBuilder.seal();
````
Now your resulting tree will have the same SHA-1 hash as before but it will have the nested sub-branches
contained. 
So you now can also ask for the sub-hashes:

````
Tree subTreeOfDatapoinA = treeWithBranches.branches()[0];
Tree subTreeOfDatapoinB = treeWithBranches.branches()[1];

````
Now you might wonder if you can also use the selectors to "query" the right sub branch.
Yes you can. That's what the "TreeIndex" helper is about.
It (upon creation) traverses the given tree and creates appropriate indexes to lookup by Selectors:

````
TreeBuilder treeBuilder = session.createTreeBuilder();

treeBuilder.branch( selector("datapointA") ).add("anydata".getBytes());
treeBuilder.branch( selector("datapointB") ).add("otherdata".getBytes());
Tree tree = treeBuilder.seal();

TreeIndex treeIndex = new TreeIndex( tree );
System.out.println(treeIndex);
System.out.println(treeIndex.select( selector("datapointA" )));
System.out.println(treeIndex.select( selector("datapointB" )));

````

Selectors are "per branch" and can be nested and reused.
Note how the parent selector "index" is reused to "group" data into a common ancestor.
````
TreeBuilder treeBuilder = session.createTreeBuilder();

treeBuilder.branch( selector("index") ).branch( selector("A") ).add("anydata".getBytes());
treeBuilder.branch( selector("index") ).branch( selector("B") ).add("otherdata".getBytes());
Tree tree = treeBuilder.seal();

TreeIndex treeIndex = new TreeIndex( tree );
System.out.println(treeIndex);
System.out.println(treeIndex.select( selector("index" ),selector("A" )));

// Composite Selector is supported too:
System.out.println(treeIndex.select( selector("index","B" )));

````

### Tags
While selectors are an integral part of what branches get merged into a single node (see index selector above),
tags are pure metadata only used to later lookup sub-trees.

Selectors are unique per "parent-branch" level, which allows the grouping feature mentioned above.
Tags do not need to be unique.

IndexTree currently does not build indexes for tags, so that is a thing to do..;)

# LICENSE
Copyright 2014-2019 rebaze GmbH.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
Change
