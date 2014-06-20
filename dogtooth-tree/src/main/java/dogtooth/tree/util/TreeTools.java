/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package dogtooth.tree.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dogtooth.tree.*;
import dogtooth.tree.internal.InMemoryTreeBuilderImpl;
import dogtooth.tree.internal.InMemoryTreeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import dogtooth.tree.annotated.Tag;

import static dogtooth.tree.Selector.selector;

/**
 * Set of tools for this API.
 * 
 * @author Toni Menzel <toni.menzel@rebaze.com>
 *
 */
public class TreeTools {

    private static final Logger LOG = LoggerFactory.getLogger( TreeTools.class );
    private static final String DEFAULT_HASH_ALOGO = "SHA-1";
    private String m_messageDigestAlgorithm = DEFAULT_HASH_ALOGO;

    public File store( Tree tree ) throws IOException {
        // persist tree
        File f = File.createTempFile( "tree", ".xml" );
        return store( tree, f );
    }
    
    public File store( Tree tree, File f ) throws IOException {
        // persist tree
        XStream xstream = new XStream();
        xstream.toXML( tree, new FileOutputStream( f ) );
        LOG.debug( "Stored tree to " + f.getAbsolutePath() );
        return f;
    }

    public Tree load( File f ) {
        XStream xstream = new XStream();
        return (Tree) xstream.fromXML( f );
    }

    public Tree load( String locationClasspath ) {
        XStream xstream = new XStream();
        return (Tree) xstream.fromXML( this.getClass().getResourceAsStream( locationClasspath ) );
    }  

    public long nodes(Tree tree) {
    	int total = 1;
    	for (Tree sub : tree.branches()) {
    		total += nodes(sub);
    	}
    	return total;
    }

    public TreeBuilder createTreeBuilder() {
        return new InMemoryTreeBuilderImpl(this);
    }

    public Tree createTree( Selector selector, String hashValue,Tree[] subs, Tag tag) {
        return new InMemoryTreeImpl( this, selector,hashValue,subs,tag);
    }

    public TreeTools setDigestAlgorithm(String algo) {
        m_messageDigestAlgorithm = algo;
        return this;
    }

    public MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance(m_messageDigestAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new TreeException("Problem loading digest with algorthm.");
        }
    }

    /**
     */
     public Tree compare( Tree left, Tree right ) {
        return compare( new TreeIndex( left ), new TreeIndex( right ) );
    }

    /**
     */
     public Tree compare( TreeIndex left, TreeIndex right ) {
        TreeBuilder target = createTreeBuilder().selector( selector("[" + left.selector() + " ] and [" + right.selector() + " ]" ) );
        target.tag( Tag.tag("DIFF"));
        TreeCompare.compare(target, left, right);
        return target.seal();
    }
}
