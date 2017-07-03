package K_means;

import java.io.IOException;
import java.util.Map;

import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.IterablePosting;


public class Collection_initial {

	public void init(int docid) throws IOException{
		Index index = Index.createIndex();
		PostingIndex<Pointer> di = (PostingIndex<Pointer>) index.getDirectIndex();
		DocumentIndex doi = index.getDocumentIndex();
		Lexicon<String> lex = index.getLexicon();
		IterablePosting postings = di.getPostings(doi.getDocumentEntry(docid));
		while (postings.next() != IterablePosting.EOL) {
			Map.Entry<String,LexiconEntry> lee = lex.getLexiconEntry(postings.getId());
			System.out.print(lee.getKey() + " with frequency " + postings.getFrequency());
		}
	}
}
