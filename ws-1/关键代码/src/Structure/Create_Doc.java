package Structure;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.terrier.structures.BitIndexPointer;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.IterablePosting;

/**
 * 根据索引，将所有的文档信息添加到内存中。
 * @author Administrator
 *
 */
public class Create_Doc {
	public Index index = null;
	public 	MetaIndex meta = null;
	private ArrayList<String> docList = new ArrayList<String>();
	public static void main(String args[]) throws Exception{
		HashMap<String, SingleDoc> alldocList = new Create_Doc().init();;
	}
	public HashMap<String, SingleDoc> init() throws Exception{
		HashMap<String, SingleDoc> result = new HashMap<String, SingleDoc>();
		index = Index.createIndex();
		meta = index.getMetaIndex();
		DocumentIndex doi = index.getDocumentIndex();
		PostingIndex<Pointer>directIndex = (PostingIndex<Pointer>)index.getDirectIndex();
		Lexicon<String> lexicon = index.getLexicon();
		int docNumber = doi.getNumberOfDocuments();
		for (int i = 0; i < docNumber; i++){
			String docid = getdoc(i);
			if (!isChoosen(docid)){
				continue;
			}
			HashMap<String, Double>hmap = new HashMap<String, Double>();
			int wordCount = 0;
			IterablePosting pi = directIndex.getPostings((BitIndexPointer)doi.getDocumentEntry(i));
			while (pi.next()!=IterablePosting.EOL){
				Map.Entry<String,LexiconEntry> leeEntry = lexicon.getLexiconEntry(pi.getId());
				int freq = pi.getFrequency();
				String termString = leeEntry.getKey();
				hmap.put(termString, (double)freq);
				wordCount +=freq;
			}
			SingleDoc single = new SingleDoc(hmap, wordCount, docid);
			result.put(docid, single);
		}
		return result;
	}
	public String  getdoc(int docno) throws Exception{
		String docString = meta.getItem("docno", docno);
		return docString;
	}
	public boolean isChoosen(String docnoString){
		if (docList.contains(docnoString)){
			return true;
		}
		return false;
	}
}
