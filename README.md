# Rebaze Commons Tree

[![Build Status](https://travis-ci.org/rebaze/rebaze-commons-tree.svg?branch=master)](https://travis-ci.org/rebaze/rebaze-commons-tree)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.rebaze.commons.tree/rebaze-commons-tree/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.rebaze.commons.tree/rebaze-commons-tree)

Small Java library implementing Merkle Trees (https://en.m.wikipedia.org/wiki/Merkle_tree).

## What is this good for

Well we use it to create content based indexes organized in directed graph like structure.
Once you have obtained a Tree instance derived from your (potentially large) content tree you can use it to (for example) quickly find the needle in the haystack when compared to an older snapshot Tree. (Usecase: Find changes)

License: ASL 2.0
