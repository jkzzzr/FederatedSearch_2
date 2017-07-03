package structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.omg.CORBA.Environment;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.postings.IterablePosting;

public class Create {
	public static double percent = 0.0;
	public static int abc = 1;
	
	private static Index index = null;
	private static MetaIndex meta = null;
	private static DocumentIndex doi = null;
	private static PostingIndex<Pointer> directIndex = null;
	private static Lexicon<String> lexicon = null;
	public int alldocnumber = 0;
	
	
	public static void main(String args[]) throws Exception{
		
	}
	
	public Create() {
		if (index ==null){
			index = Index.createIndex();
		}
		if (meta ==null){
			meta = index.getMetaIndex();
		}
		if (doi ==null){
			doi = index.getDocumentIndex();
		}
		if (directIndex ==null){
			directIndex = (PostingIndex<Pointer>)index.getDirectIndex();
		}
		if (lexicon ==null){
			lexicon = index.getLexicon();
		}
	}

	/**
	 * 负责对上一步已经初始化了的alldoclist进行重筛选
	 * @return
	 * @throws Exception
	 */
	public ArrayList<SingleDoc> init() throws Exception{
		System.out.println("init_single_list");
		ArrayList<SingleDoc> result = new ArrayList<SingleDoc>();
		int docNumber = doi.getNumberOfDocuments();
		for (int i = 0; i < docNumber; i++){
			if (!isChoosen()){
				continue;
			}
			SingleDoc singleDoc = getDocItemList(i);
			result.add(singleDoc);
		}
		this.alldocnumber = result.size();
		System.out.println("@Create - init()"
				+ "\n\t 总文档数量: "+ docNumber
				+ "\n\t 初始化文档数量: "+ result.size());
		return result;
	}
	private boolean isChoosen(){
		double rand = Math.random();
		if (rand < percent){
			return true;
		}
		return false;
	}
	/**
	 * 初始化标准为，在集合中的文档，选取出来
	 * @param hs
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, SingleDoc> init(HashSet<Integer> hs) throws Exception{
		System.out.println("init_single_list");
		HashMap<Integer, SingleDoc> result = new HashMap<Integer, SingleDoc>();
		int docNumber = doi.getNumberOfDocuments();
		System.out.println(docNumber+"");
		for (int i = 0; i < docNumber; i++){
			if (!isChoosen(hs, i)){
				continue;
			}
			SingleDoc singleDoc = getDocItemList(i);
			result.put(i, singleDoc);
		}
		this.alldocnumber = result.size();
		System.out.println("@Create - init()"
				+ "\n\t 总文档数量: "+ docNumber
				+ "\n\t 初始化文档数量: "+ result.size());
		return result;
	}
	
	/**
	 * 如果包含这篇文档
	 * @param hs
	 * @param i
	 * @return
	 */
	private static boolean isChoosen(HashSet<Integer> hs, int i){
		if (hs.contains(i)){
			return true;
		}
		return false;
	}
	
	public String  getdocString(int docno) throws Exception{
		String docString = meta.getItem("docno", docno);
		return docString;
	}
	/**
	 * 根绝文档ID，获取该文档的信息
	 * @param docno
	 * @return
	 * @throws Exception
	 */
	public SingleDoc getDocItemList(int docno) {
		SingleDoc single = null;
		try {
		HashMap<Integer, Double>hmap = new HashMap<Integer, Double>();
		int wordCount = 0;
		IterablePosting pi;
		synchronized (index) {
				pi = directIndex.getPostings(doi.getDocumentEntry(docno));
		}
		while (pi.next()!=IterablePosting.EOL){
			int freq = pi.getFrequency();
			hmap.put(pi.getId(), (double)freq);
			wordCount +=freq;
		}
		single = new SingleDoc(hmap, wordCount);
		} catch (IOException e) {
		}
		return single;
	}
	
	public String getItemString(int termno){
		Map.Entry<String,LexiconEntry> leeEntry = lexicon.getLexiconEntry(termno);
		String termString = leeEntry.getKey();
		return termString;
	}
	
	public void destory(){
		try {
			this.doi = null;
			this.lexicon.close();
			this.directIndex.close();
			this.meta.close();
			this.index.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
