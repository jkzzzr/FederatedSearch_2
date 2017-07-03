package structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Centroid extends Base implements Serializable{
	/**
	 * 根据一个数据集更新质心 频数用平均值来计算
	 * @param docList
	 */
	public void Centroid2(ArrayList<Integer> docList) {
		HashMap<Integer, Double> result = new HashMap<Integer, Double>();
		HashMap<Integer, Integer> nt = new HashMap<Integer, Integer>();
		for (int i = 0; i < docList.size(); i++){
			Integer docid = docList.get(i);
			SingleDoc singleDoc = K_Means.create_Doc2.getDocItemList(docid);
			HashMap<Integer, Double> temphm = singleDoc.getTermMap();
			for (Integer string : temphm.keySet()){
				if (result.containsKey(string)){
					double freq = result.get(string);
					freq +=temphm.get(string);
					result.replace(string, freq);
					int count = nt.get(string);
					nt.replace(string, count + 1);
				}else {
					result.put(string, temphm.get(string));
					nt.put(string, 1);
				}
			}
		}
		double temp_wordcount= 0;
		for (Integer term : result.keySet()){
			double allfreq = result.get(term);
			double allcount = nt.get(term);
			double freq = allfreq / allcount;
			result.replace(term, freq);
			temp_wordcount +=freq;
		}
		this.termMap = result;
		this.wordCount = temp_wordcount;
		this.docid = -1;
	}
	public Centroid(ArrayList<Integer> docList) {
		super();
		HashMap<Integer, Double> result = new HashMap<Integer, Double>();
	//	HashMap<String, Integer> nt = new HashMap<String, Integer>();
		for (int i = 0; i < docList.size(); i++){
			Integer docid = docList.get(i);
			SingleDoc singleDoc = K_Means.create_Doc2.getDocItemList(docid);
			HashMap<Integer, Double> temphm = singleDoc.getTermMap();
			for (Integer string : temphm.keySet()){
				if (result.containsKey(string)){
					double freq = result.get(string);
					freq +=temphm.get(string);
					result.replace(string, freq);
		//			int count = nt.get(string);
		//			nt.replace(string, count + 1);
				}else {
					result.put(string, temphm.get(string));
		//			nt.put(string, 1);
				}
			}
		}
		double temp_wordcount= 0;
		for (Integer term : result.keySet()){
			double allfreq = result.get(term);
	//		double allcount = nt.get(term);
	//		double freq = allfreq / allcount;
	//		result.replace(term, freq);
	//		temp_wordcount +=freq;
			temp_wordcount +=allfreq;
		}
		this.termMap = result;
		this.wordCount = temp_wordcount;
		this.docid = -1;
	}
	public Centroid(SingleDoc singleDoc){
		super(singleDoc.termMap, singleDoc.wordCount);
		this.docid = -1;
	}
}
