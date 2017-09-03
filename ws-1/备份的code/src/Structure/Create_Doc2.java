package Structure;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
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

import K_means.AllPath;
import K_means.K_Means;

public class Create_Doc2 {
	public static double percent = AllPath.percent;
	private Index index = null;
	private 	MetaIndex meta = null;
	public static ArrayList<String> alldoclsit = new ArrayList<String>();
	public static ArrayList<SingleDoc> allsinglelist = new ArrayList<SingleDoc>();
	public static void main(String args[]) throws Exception{
		
	}
	
	public Create_Doc2() {
		this.index = Index.createIndex();
		this.meta = index.getMetaIndex();
	}

/*	*//**
	 * 读取所有文档信息list
	 * @return
	 * @throws Exception
	 *//*
	public void init() throws Exception{
		if (alldoclsit == null || alldoclsit.size() == 0){
			initDocList();
		}
		ArrayList<SingleDoc> result = new ArrayList<SingleDoc>();
	//	index = Index.createIndex();
	//	meta = index.getMetaIndex();
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
			DocumentIndexEntry die = doi.getDocumentEntry(i);
			IterablePosting pi = directIndex.getPostings(die);
			while (pi.next()!=IterablePosting.EOL){
				int freq = pi.getFrequency();
				int termid = pi.getId();
				Entry<String, LexiconEntry> termEntry = lexicon.getIthLexiconEntry(termid);
				String termString = termEntry.getKey();
				hmap.put(termString, (double)freq);
				wordCount +=freq;
				System.out.print("\t("+termString+","+freq+")");
			}
			SingleDoc single = new SingleDoc(hmap, wordCount);
			result.add(single);
		}
		Create_Doc2.allsinglelist = result;
	}*/
	/**
	 * 负责对上一步已经初始化了的alldoclist进行重筛选
	 * @return
	 * @throws Exception
	 */
	public ArrayList<SingleDoc> init_single_list() throws Exception{
		System.out.println("init_single_list");
		ArrayList<SingleDoc> result = new ArrayList<SingleDoc>();
		index = Index.createIndex();
		meta = index.getMetaIndex();
		DocumentIndex doi = index.getDocumentIndex();
		PostingIndex<Pointer>directIndex = (PostingIndex<Pointer>)index.getDirectIndex();
		Lexicon<String> lexicon = index.getLexicon();
		int docNumber = doi.getNumberOfDocuments();
//		System.out.println("@Create_Doc2 - init_single_list()\t"+ docNumber);
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
			if (wordCount < 100){
				continue;
			}
			result.add(single);
			K_Means.AllDocMap.put(docid, single);
		}
/*
		for (int i = 0; i < alldoclsit.size(); i++){
			String string = alldoclsit.get(i);
			if (!K_Means.AllDocMap.containsKey(string)){
				alldoclsit.remove(string);
				System.out.println(string);
			}
		}
		System.out.println("@Create_Doc2 - init_single_list()\t alldoclsit.size(): "+ alldoclsit.size());
		System.out.println("@Create_Doc2 - init_single_list()\tK_Means.AllDocMap.size() : "+ K_Means.AllDocMap.size());*/
		ArrayList<String> aaaList = new ArrayList<String>();
		for (String string :K_Means.AllDocMap.keySet()){
			aaaList.add(string);
		}
		alldoclsit = aaaList;
		System.out.println("@Create_Doc2 - init_single_list()\t alldoclsit.size(): "+ alldoclsit.size());
		System.out.println("@Create_Doc2 - init_single_list()\tK_Means.AllDocMap.size() : "+ K_Means.AllDocMap.size());
		this.allsinglelist = result;
		return result;
	}
	private String  getdoc(int docno) throws Exception{
		String docString = meta.getItem("docno", docno);
		return docString;
	}
	private boolean isChoosen(String docnoString){
		/*if (!alldoclsit.contains(docnoString)){
			return false;
		}*/
		double rand = Math.random();
		if (rand < percent){
			return true;
		}
		return false;
	}

/*	public void initDocList() throws Exception{
		alldoclsit = readDOCLIST("F:\\terrier_data\\D1-doclist");
	//	System.out.println("@Create_Doc2 - initDocList()\t"+alldoclsit.size());
		if (alldoclsit.contains("clueweb12-0000tw-02-03763")){
			System.out.println("@Create_Doc2 - initDocList()\t:包含了这个文档");
		}
	}*/
/*	*//**
	 * 读取已经按概率选取好的文档列表
	 * @param inputPath
	 * @return
	 * @throws Exception
	 *//*
	private ArrayList<String> readDOCLIST(String inputPath) throws Exception{
		FileInputStream fis = new FileInputStream(inputPath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		ArrayList<String> result = (ArrayList<String>)ois.readObject();
		return result;
	}*/
}
