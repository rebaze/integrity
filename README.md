[<img src="http://www.rebaze.com/assets/Rebaze_icon_colors_tbg.png" align="right" width="100">](http://rebaze.com)

[![Build Status](https://travis-ci.org/rebaze/integrity.svg?branch=master)](https://travis-ci.org/rebaze/integrity)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.rebaze.integrity/org.rebaze.integrity.tree/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.rebaze.integrity/org.rebaze.integrity.tree)
[![Apache 2.0](https://img.shields.io/github/license/nebula-plugins/nebula-publishing-plugin.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# Rebaze Integrity

- Composite hash- tree based java library
- OSGi compatible
- Embeddable
- Low footprint 

Small Java library implementing Merkle Trees (https://en.m.wikipedia.org/wiki/Merkle_tree).

## What is this good for

Well we use it to create content based indexes organized in directed graph like structure.

### Usecase: Find changes
Once you have obtained a Tree instance derived from your (potentially large) content tree you can use it to quickly find the needle in the haystack when compared to an older snapshot Tree.

## License
Apache Software License 2.0
