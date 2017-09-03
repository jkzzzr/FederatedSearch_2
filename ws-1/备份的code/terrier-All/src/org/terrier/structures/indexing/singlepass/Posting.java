/*
 * Terrier - Terabyte Retriever
 * Webpage: http://terrier.org
 * Contact: terrier{a.}dcs.gla.ac.uk
 * University of Glasgow - School of Computing Science
 * http://www.gla.ac.uk/
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is Posting.java.
 *
 * The Original Code is Copyright (C) 2004-2014 the University of Glasgow.
 * All Rights Reserved.
 *
 * Contributor(s):
 *   Roi Blanco (rblanc{at}@udc.es)
 *   Craig Macdonald (craigm{at}dcs.gla.ac.uk)
 */

package org.terrier.structures.indexing.singlepass;

import java.io.IOException;

import org.terrier.compression.bit.MemorySBOS;

/**
 * 代表内存中的一个简单的发布列表
 * 存储了信息<code>TF,idf</code>,和一串<code>[文档，tf]</code>
 * 词频，反文档频率
 * Class representing a simple posting list in memory.
 * It keeps the information for <code>TF, Nt</code>, and the sequence <code>[doc, tf]</code>
 * @author Roi Blanco
 *
 */
public class Posting {
	
	/** 词频 */
	protected int TF;
	/** 文档频率 */
	protected int Nt;
	/**压缩在内存中包含序列docid,idf的对象 
	 * The compressed in-memory object holding the sequence doc_id, idf*/	
	protected MemorySBOS docIds;
	/**插入发布中的最后一篇文档 
	 * Last document inserted in the posting */
	protected int lastInt = 0;
		
	/**
	 * 写入发布列表的第一篇文档
	 * Writes the first document in the posting list.
	 * @param docId the document identifier.
	 * @param 这个词在文档中的频率	freq the frequency of the term in the document.
	 * @return 缓冲区中被消耗的字节数量，写到了第几个字节。the number of bytes consumed in the buffer
	 * @throws IOException if an I/O error ocurrs.
	 */	
	public int writeFirstDoc(int docId, int freq) throws IOException{		
		docIds = new MemorySBOS();
		TF = freq;			
		Nt = 1;
		//System.err.println("Writing docid="+ (docId+1) + " f=" + freq);
		docIds.writeGamma(docId + 1);
		docIds.writeGamma(freq);
		lastInt = docId;
		return docIds.getSize();
	}
	
	/**
	 * 在发布列表中插入新文档，必须按照顺序插入文档
	 * (注:主要是在docIds对象中写入了gamma值,包括文档和词频)
	 * Inserts a new document in the posting list. Document insertions must be done
	 * in order.  
	 * @param doc the document identifier.
	 * @param freq 这篇文档中，这个词的词频the frequency of the term in the document.
	 * @return the 缓存中已经消耗的字节数量 number of bytes consumed in the buffer
	 * @throws IOException if and I/O error occurs.
	 */
	public int insert(int doc, int freq) throws IOException
	{		
		final int bytes = docIds.getSize();
		Nt++;
		TF += freq;
		docIds.writeGamma(doc - lastInt);
		docIds.writeGamma(freq);
		lastInt = doc;					
		return docIds.getSize() - bytes;
	}

	/**
	 * @return the term frequency of the term in the run
	 */
	public int getTF(){
		return TF;
	}
	
	/**
	 * @return the document data compressed object.
	 */
	public MemorySBOS getDocs(){
	    return docIds;
	}	
	
	/**
	 * Sets the term frequency in the run.
	 * @param tf the term frequency.
	 */
	public void setTF(int tf){
		this.TF = tf;
	}
	
	/**
	 * Sets the document data compressed object.
	 * @param docs
	 */
	public void setDocs(MemorySBOS docs){
		this.docIds = docs;
	}

	/**
	 * @return the document frequency - the number of documents this term occurs in
	 */
	public int getDocF() {
		return Nt;
	}

	/**
	 * Set the document frequency the number of documents this term occurs in.
	 * @param docF the document frequency.
	 */
	public void setDocF(int docF) {
		this.Nt = docF;
	}

	/** Returns the size of the underlying buffer representing this posting set. */
	public int getSize() {
		return docIds.getSize();
	}
}
