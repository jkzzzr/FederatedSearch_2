package compute;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import pubData.PubData;
import structure.Base;
import structure.SingleDoc;

public class Com_Sim_K_means {
	public static double lambda = 0.5; 
	public ArrayList<SingleDoc> allDocs = new ArrayList<SingleDoc>();
	int qid;
	boolean isCache = true;
	public Com_Sim_K_means(int qid, boolean iss) {
		super();
		this.qid = qid;
		this.isCache = iss;
		SIMap = new HashMap<Integer, HashMap<Integer,Double>>();
	}

	public HashMap<Integer,HashMap<Integer, Double>> SIMap = new HashMap<Integer, HashMap<Integer,Double>>();
	
	public Double getSim(Integer index1, int index2) {
		if (isCache){
			if (SIMap.containsKey(index1)){
				HashMap<Integer, Double> temp = SIMap.get(index1);
				if (temp == null){
					temp = new HashMap<Integer, Double>();
				}
				if(temp.containsKey(index2)){
					return temp.get(index2);
				}else {
					double result = sim(index1, index2);
					temp.put(index2, result);
					SIMap.put(index1, temp);
					return result;
				}
			}else {
				HashMap<Integer, Double> temp = new HashMap<Integer, Double>();
				double result = sim(index1, index2);
				temp.put(index2, result);
				SIMap.put(index1, temp);
				return result;
			}
		}else {
			double result = sim(index1, index2);
			return result;
		}
	}
	private double sim(Integer in1, Integer in2){
		SingleDoc s1 = allDocs.get(in1);
		SingleDoc s2 = allDocs.get(in2);
		double result = sim(s1, s2);
		return result;
	}
	private double sim(SingleDoc s1 , SingleDoc s2){
		double result = sim(s1.getTermMap(), s1.getWordCount(), s2.getTermMap(), s2.getWordCount());
		return result;
	}
	private double sim (HashMap<Integer, Double> map1, double tokenier1, HashMap<Integer, Double>map2, double tokenier2){
		Set<Integer> termSet1 = map1.keySet();
		Iterator<Integer> iterator = termSet1.iterator();
		double result = 0.0;
		while (iterator.hasNext()){
			Integer term = iterator.next();
			if (!map2.containsKey(term)){
				continue;
			}
			double count1 = map1.get(term);
			double count2 = map2.get(term);
			//计算变量
			double pcw = count2 / tokenier2;
			double pbw = computPBW(term, PubData.allDocs);
			double pdw = (1 - Com_Sim_K_means.lambda) * count1/tokenier1 + Com_Sim_K_means.lambda * pbw;
			//第一部分
			double part1 = pcw * Math.log(pdw / (Com_Sim_K_means.lambda * pbw));
			double part2 = pdw * Math.log(pcw / (Com_Sim_K_means.lambda * pbw));
			result += part1 + part2;
		}
		return result;
	}
	
	/**
	 * 计算词项term在所有质心中的出现概率平均值
	 * @param term
	 * @return
	 */
	private double computPBW(Integer term, ArrayList<? extends Base> centroidList){
		//TODO
		double p_all = 0.0;
		int num = 0;
		for(int i = 0; i < centroidList.size(); i ++){
			if (!centroidList.get(i).getTermMap().containsKey(term)){
				p_all+= 0; 
			}else {
				double temp=  centroidList.get(i).getTermMap().get(term)/ centroidList.get(i).getWordCount();
				p_all +=temp;
				if (temp >=1){
					System.out.println("err\t"+ temp+"\t"+centroidList.get(i).getTermMap().get(term)+"\t"+centroidList.get(i).getWordCount());
				}
				num++;
			}
		}
		
		if (num == 0){
			return 0;
		}else {
			return p_all/num;
		}
	}
	
	/**
	 * 计算词项term在所有质心中的出现概率平均值
	 * @param term
	 * @return
	 */
	private double computPBW(Integer term, Base base){
		if (!base.getTermMap().containsKey(term)){
			return 0;
		}else {
			double temp=  base.getTermMap().get(term)/ base.getWordCount();
			return temp;
		}
	}
}

