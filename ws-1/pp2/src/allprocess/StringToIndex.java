package process1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.Pointer;
import org.terrier.structures.PostingIndex;
/**
 * 处理包含clueweb ID的检索结果列表，将结果列表中的clueweb ID用对应的索引中的数字ID替换，
 * 返回结果为，（排序次序  数字ID）
 * @author Administrator
 *
 */
public class StringToIndex {

	public static String inputPath = "E:/实验数据/DPH_4.res";
	public static String outputPath = "E:/实验数据/123";
	public static HashMap<Integer, ArrayList<String>> HMAP = new HashMap<Integer, ArrayList<String>>();
	
	public Index index = null;
	private MetaIndex meta;
	private DocumentIndex doi;
	private PostingIndex<Pointer> directIndex;
	private Lexicon<String> lexicon;
	private int alldocnumber;
	public static void main(String[] args) throws Exception {
		StringToIndex sti = new StringToIndex();
		sti.init();
		
		for (int i = 151; i <=200; i++){
			ArrayList<String> hs = sti.input_Res(inputPath, i);
			ArrayList<Integer> hm = sti.check(hs);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
			for (int j = 0; j < hm.size(); j++){
				Integer index = hm.get(j);
				bw.write(i + "\t" + index + "\n");
			}
			hs.clear();
			hm.clear();			
			bw.close();
			System.out.println(i+"\tEND");
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
	public ArrayList<String> input_Res(String path, int qid) throws Exception{
		BufferedReader bReader = new BufferedReader(new FileReader(path));
		String line = "";
		ArrayList<String> al = new ArrayList<String>();
		while ((line = bReader.readLine())!=null){
			StringTokenizer st = new StringTokenizer(line);
			Integer lineQid = Integer.parseInt(st.nextToken());
			st.nextElement();
			String docid = st.nextToken();
			if (lineQid < qid){
				continue;
			}else if (lineQid == qid){
				al.add(docid);
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
	public ArrayList<Integer> check(ArrayList<String> clueStrings){
		HashMap<String, Integer> resultmap = new HashMap<String, Integer>();
		int docNumber = doi.getNumberOfDocuments();
		for (int i = 0; i < docNumber; i++){
			String doc = getdocString(i);
			resultmap.put(doc, i);
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < clueStrings.size(); i++){
			String doc = clueStrings.get(i);
			int index = resultmap.get(doc);
			result.add(index);
		}
		return result;
		
	}
}
