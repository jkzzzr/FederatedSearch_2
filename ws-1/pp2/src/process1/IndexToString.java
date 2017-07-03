package process1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.xml.crypto.Data;

import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;

import structure.Data_;
/**
 * 处理包含clueweb ID的检索结果列表，将结果列表中的clueweb ID用对应的索引中的数字ID替换，
 * 返回结果为，（排序次序  数字ID）
 * @author Administrator
 *
 */
public class IndexToString {

	public static String inputPath = "/home/Lee/data/结果备份/final.3.choose-100";
	public static String outputPath = "/home/Lee/data/结果备份/final.3.string.choose-100";
	
	public static int outputNumber = 500;
	
	
	public static HashMap<Integer, ArrayList<String>> HMAP = new HashMap<Integer, ArrayList<String>>();
	
	public Index index = null;
	private MetaIndex meta;
	private DocumentIndex doi;
	private PostingIndex<Pointer> directIndex;
	private Lexicon<String> lexicon;
	private int alldocnumber;
	
	
	private ArrayList<Data_> data_list = new ArrayList<>();
	public static void main(String[] args) throws Exception {
		IndexToString sti = new IndexToString();
		sti.init();
		
		for (int i = 201; i <=250; i++){
			ArrayList<Integer> hs = sti.input_Res(inputPath, i);
			ArrayList<String> hm = sti.check(hs);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, true));
			for (int j = 0; j < hm.size(); j++){
				String docid = hm.get(j);
				Data_ data_ = sti.data_list.get(j);
				
				bw.write(data_.toString2(docid) + "\n");
			}
			hs.clear();
			hm.clear();			
			bw.close();
			System.out.println(i+"\tEND");
			sti.clear();
		}
		
		
	}

	public void init(){
		this.index = Index.createIndex();
		this.meta = index.getMetaIndex();
		this.doi = index.getDocumentIndex();
		this.directIndex = (PostingIndex<Pointer>)index.getDirectIndex();
		this.lexicon = index.getLexicon();
		this.alldocnumber = doi.getNumberOfDocuments();
	}
	/**
	 * 从结果列表中，获取到docString
	 * @param path
	 * @param qid
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Integer> input_Res(String path, int qid) throws Exception{
		BufferedReader bReader = new BufferedReader(new FileReader(path));
		String line = "";
		ArrayList<Integer> al = new ArrayList<Integer>();
		int count =0;
		while ((line = bReader.readLine())!=null){
			StringTokenizer st = new StringTokenizer(line);
			Integer lineQid = Integer.parseInt(st.nextToken());
			st.nextElement();
			String docid = st.nextToken();
			if (lineQid < qid){
				continue;
			}else if (lineQid == qid){
				if (count > outputNumber){
					break;
				}
				count ++;
				Data_ data_ = new Data_(line);
				this.data_list.add(data_);
				al.add(Integer.parseInt(docid));
			}else {
				break;
			}
		}
		bReader.close();
		
//		StringToIndex.HMAP = hmtemp;
		return al;
	}
	
	public String  getdocString(int docno){
		String docString = null;
		try {
			docString = meta.getItem("docno", docno);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return docString;
	}
	/**
	 * docid - docno
	 * @param clueStrings
	 * @return
	 */
	public ArrayList<String> check(ArrayList<Integer> clueStrings){
		TreeSet<Integer> ts = new TreeSet<>(clueStrings);
		ArrayList<Integer> copyList = new ArrayList<>(clueStrings);
		Collections.sort(copyList);
		
		HashMap<Integer, String> resultmap = new HashMap<Integer, String>();
		int docNumber = doi.getNumberOfDocuments();
		for (int i = 0; i < docNumber; i++){
			if (copyList.size() ==0){
				break;
			}else {
				if (i != copyList.get(0)){
					continue;
				}else {
					copyList.remove(0);
				}
			}
			String doc = getdocString(i);
			resultmap.put(i, doc);
		}
		
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < clueStrings.size(); i++){
			int doc = clueStrings.get(i);
			String docid = resultmap.get(doc);
			result.add(docid);
		}
		return result;
	}
	public void clear(){
		this.data_list.clear();
	}
}
