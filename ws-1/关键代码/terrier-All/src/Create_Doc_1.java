
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.terrier.structures.BitIndexPointer;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.DocumentIndexEntry;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.IterablePosting;

public class Create_Doc_1 {
	public Index index = null;
	public 	MetaIndex meta = null;
	public static void main(String args[]) throws Exception{
		new Create_Doc_1().init_singledoc_list();;
	}
	
	/**
	 * 初始化ArrayList<SingleDoc>
	 * @return
	 * @throws Exception
	 */
	public ArrayList<SingleDoc> init_singledoc_list() throws Exception{
		ArrayList<SingleDoc> result = new ArrayList<SingleDoc>();
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
			SingleDoc single = new SingleDoc(hmap, wordCount);
			result.add(single);
		}
		return result;
	}
	public String  getdoc(int docno) throws Exception{
		String docString = meta.getItem("docno", docno);
		return docString;
	}
	public boolean isChoosen(String docnoString){
		return true;
	}
}
