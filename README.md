[<img src="http://www.rebaze.com/assets/Rebaze_icon_colors_tbg.png" align="right" width="100">](http://rebaze.com)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.rebaze.commons.tree/rebaze-commons-tree/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.rebaze.commons.tree/rebaze-commons-tree)

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
