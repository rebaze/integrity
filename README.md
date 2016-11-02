[<img src="http://www.rebaze.com/assets/Rebaze_icon_colors_tbg.png" align="right" width="100">](http://rebaze.com)

[![OSGi compatible](https://img.shields.io/badge/OSGi-compatible-green.svg)](http://www.osgi.org)
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

# LICENSE
Copyright 2014-2016 rebaze GmbH.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
