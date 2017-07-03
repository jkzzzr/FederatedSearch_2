package Structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import K_means.K_Means;

public class Centroid extends Base implements Serializable{
	/**
	 * 根据一个数据集更新质心 频数用平均值来计算
	 * @param docList
	 */
	public void Centroid2(ArrayList<String> docList) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		HashMap<String, Integer> nt = new HashMap<String, Integer>();
		for (int i = 0; i < docList.size(); i++){
			String docid = docList.get(i);
			SingleDoc singleDoc = K_Means.AllDocMap.get(docid);
			HashMap<String, Double> temphm = singleDoc.getTermMap();
			for (String string : temphm.keySet()){
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
		for (String term : result.keySet()){
			double allfreq = result.get(term);
			double allcount = nt.get(term);
			double freq = allfreq / allcount;
			result.replace(term, freq);
			temp_wordcount +=freq;
		}
		this.termMap = result;
		this.wordCount = temp_wordcount;
		this.docid = "Centroid";
	}
	public Centroid(ArrayList<String> docList) {
		super();
		HashMap<String, Double> result = new HashMap<String, Double>();
	//	HashMap<String, Integer> nt = new HashMap<String, Integer>();
		for (int i = 0; i < docList.size(); i++){
			String docid = docList.get(i);
			SingleDoc singleDoc = K_Means.AllDocMap.get(docid);
			HashMap<String, Double> temphm = singleDoc.getTermMap();
			for (String string : temphm.keySet()){
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
		for (String term : result.keySet()){
			double allfreq = result.get(term);
	//		double allcount = nt.get(term);
	//		double freq = allfreq / allcount;
	//		result.replace(term, freq);
	//		temp_wordcount +=freq;
			temp_wordcount +=allfreq;
		}
		this.termMap = result;
		this.wordCount = temp_wordcount;
		this.docid = "Centroid";
	}
	public Centroid(SingleDoc singleDoc){
		super(singleDoc.termMap, singleDoc.wordCount);
		this.docid = "Centroid";
	}
}
