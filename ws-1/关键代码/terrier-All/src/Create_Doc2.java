
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import org.terrier.structures.seralization.WriteableFactory;

public class Create_Doc2 {
	public Index index = null;
	public 	MetaIndex meta = null;
	public static ArrayList<String> alldoclsit = new ArrayList<String>();
	public static void main(String args[]) throws Exception{
		System.out.println("初始化文档列表");
		initDocList();
		System.out.println("初始化单个文档");
		ArrayList<SingleDoc> al = new Create_Doc2().init();
		System.out.println("END");
		writeSingleDoc_ALL(al, "F:\\terrier_data\\d1-single-list");
		
	}
	public ArrayList<SingleDoc> init() throws Exception{
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
		if (alldoclsit.contains(docnoString)){
			return true;
		}
		return false;
	}
	
	/**
	 * 初始化文档列表
	 */
	public static void initDocList(){
		try {
			alldoclsit = readDOCLIST("F:\\terrier_data\\D1-doclist");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static ArrayList<String> readDOCLIST(String inputPath) throws Exception{
		FileInputStream fis = new FileInputStream(inputPath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		ArrayList<String> result = (ArrayList<String>)ois.readObject();
		return result;
	}
	private static void writeSingleDoc_ALL(ArrayList<SingleDoc> al, String outputPath) throws Exception{
		FileOutputStream fos = new FileOutputStream(outputPath);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(al);
		return;
	}
	private static ArrayList<SingleDoc> readSingleDoc_ALL(String inputPath) throws Exception{
		FileInputStream fis = new FileInputStream(inputPath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		ArrayList<SingleDoc> result = (ArrayList<SingleDoc>)ois.readObject();
		return result;
	}
}
