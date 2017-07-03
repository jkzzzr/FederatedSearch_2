package Structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import K_means.K_Means;
import combineCol.Entry;
import filterCollection.TypeToCentroid;

public class Centroid extends Base implements Serializable{
	/**
	 * 根据一个数据集更新质心 频数用平均值来计算
	 * @param docList
	 */
	public Centroid(ArrayList<Integer> docList) {
		double percent = 1.0;
		if (docList.size() < 25) {
			this.docid = -1000 ;
			return;
		}
		/*else if (docList.size() <100){
			percent = 100 / docList.size();
		}else if (docList.size() < 1000){
			percent = 200 / docList.size();
		}else {
			percent = 300 / docList.size();
		}*/
		HashMap<Integer, Double> result = new HashMap<Integer, Double>();
		HashMap<Integer, Integer> nt = new HashMap<Integer, Integer>();
		double tempallcount = 0.0;
		for (int i = 0; i < docList.size(); i++){
			Integer docid = docList.get(i);
			SingleDoc singleDoc = K_Means.create_Doc2.getDocItemList(docid);
	//		SingleDoc singleDoc = TypeToCentroid.create_Doc2.getDocItemList(docid);
			tempallcount +=singleDoc.getTermMap().size();
		}
		percent = 2000 / tempallcount;
		
		for (int i = 0; i < docList.size(); i++){
			Integer docid = docList.get(i);
			SingleDoc singleDoc = K_Means.create_Doc2.getDocItemList(docid);
	//		SingleDoc singleDoc = TypeToCentroid.create_Doc2.getDocItemList(docid);
	//		SingleDoc singleDoc = Entry.initCol.getDocItemList(docid);
			HashMap<Integer, Double> temphm = singleDoc.getTermMap();
			
			for (Integer string : temphm.keySet()){
				
				double tempRandom = Math.random();
				if (tempRandom > percent){
					continue;
				}
				
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
/*	public Centroid(ArrayList<Integer> docList) {
		super();
		HashMap<Integer, Double> result = new HashMap<Integer, Double>();
		for (int i = 0; i < docList.size(); i++){
			Integer docid = docList.get(i);
			SingleDoc singleDoc = K_Means.create_Doc2.getDocItemList(docid);
			HashMap<Integer, Double> temphm = singleDoc.getTermMap();
			for (Integer string : temphm.keySet()){
				if (result.containsKey(string)){
					double freq = result.get(string);
					freq +=temphm.get(string);
					result.replace(string, freq);
				}else {
					result.put(string, temphm.get(string));
				}
			}
		}
		double temp_wordcount= 0;
		for (Integer term : result.keySet()){
			double allfreq = result.get(term);
			temp_wordcount +=allfreq;
		}
		this.termMap = result;
		this.wordCount = temp_wordcount;
		this.docid = -1;
	}*/
	public Centroid(SingleDoc singleDoc){
		super(singleDoc.termMap, singleDoc.wordCount);
		this.docid = -1;
	}
	public Centroid(Centroid centroid){
		super(centroid.termMap, centroid.wordCount);
		this.docid = -1;
	}
}
