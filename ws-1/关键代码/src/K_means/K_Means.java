package K_means;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.terrier.structures.Index;

import Structure.Centroid;
import Structure.Create_Doc2;
import Structure.SingleDoc;

public class K_Means {

	private static final double DISTance = 1.5;

	public static int ITERATION = 100;
	public static double lambda = 0.5;
	public static int currentIteration = 0;
	public static int K = 10;
	//所有<文档ID，对应的文档信息词汇等>
	public static HashMap<String, SingleDoc> AllDocMap = new HashMap<String, SingleDoc>();

	private static ArrayList<SingleDoc> singleList = new ArrayList<SingleDoc>();
	//所有文档名列表
	private static ArrayList<String> docList = new ArrayList<String>();
	public static double distanceDEFAULT = Double.MIN_VALUE;
	//质心
	private ArrayList<Centroid>  centroidList = new ArrayList<Centroid>();; 
	//所有<分属的类别，文档ID-List>
	private HashMap<Integer, ArrayList<String>> TypeDocList = new HashMap<Integer, ArrayList<String>>();
	
	public static void main(String[] args){
		System.out.println("!!");
		K_Means k_Means = new K_Means();
		k_Means.run();
	}
	public K_Means(){
		initStatic();
		initCentroid();
	}
	/**
	 * 初始化静态变量
	 */
	public void initStatic(){
		System.out.println("@K_meams - initStatic()\tK_means初始化静态变量 singleList和docList");
		Create_Doc2 create_Doc2 = new Create_Doc2();
		try {
			//alldoclist
		/*	create_Doc2.initDocList();*/
			create_Doc2.init_single_list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		K_Means.singleList = Create_Doc2.allsinglelist;
		K_Means.docList = Create_Doc2.alldoclsit;
		System.err.println("@K_means - initStatic() - end");
	}
	/**
	 * 初始化质心
	 */
	public void initCentroid(){
		System.out.println("@K_means - initCentroid()\t初始化质心");
		this.centroidList = new ArrayList();
		//随机选出K个文档作为中心，存放在centoridmap中
		ArrayList<Integer> indexlist = new ArrayList();
		for (int i = 0; i<K; i++){
			while (true){
				int no = (int) ( K_means.K_Means.docList.size()* Math.random());
				if (indexlist.contains(no)){
					continue;
				}else {
					SingleDoc singleDoc = AllDocMap.get(K_means.K_Means.docList.get(no));
					if (singleDoc.getWordCount() < 100){
						continue;
					}else {
						centroidList.add(new Centroid(singleDoc));
						indexlist.add(no);
						break;
					}
				}
			}
		}
/*		System.out.println("距离矩阵");
		for (int i = 0; i < centroidList.size();i++){
			for (int j = 0; j < centroidList.size(); j++){
				System.out.print(distance(centroidList.get(i),centroidList.get(j))+"\t");
			}
			System.out.println();
		}*/
		System.out.println("@K_means - initCentroid()\tend\t质心数量"+centroidList.size());
	}
	/**
	 * 整体流程
	 */
	public void run(){
		System.out.println("@K_meas - run()\t开始执行");
		ArrayList<Centroid>  tempCentroidList = null;
		System.err.println("@K_Means - run()\t docList.size:\t" +  docList.size());
		for (int iteration = 0; iteration < K_Means.ITERATION; iteration++){
			System.out.println("@K_means - run()/t第"
					+ iteration +"次迭代");
			//是否满足终止条件
			if ( isEnd(tempCentroidList) ){
				return;
			}
			this.currentIteration = iteration;
			//每次迭代开始，清空记录表
			this.TypeDocList = new HashMap<Integer, ArrayList<String>>();
			tempCentroidList = this.centroidList;
			if (iteration != 0){
				record();
			}
			
			for (int docNo = 0; docNo < docList.size(); docNo ++){
				String docString = docList.get(docNo);
				//计算文档与各个中心之间的距离
				double maxDouble = K_Means.distanceDEFAULT;
				int cenIndex = 0;
				//选择合适的中心
				for (int cenNo = 0; cenNo < centroidList.size(); cenNo ++){
					double tempDistance = distance(docList.get(docNo), centroidList.get(cenNo));
					if (maxDouble < tempDistance){
						maxDouble = tempDistance;
						cenIndex = cenNo;
					}
				}
				//将文档加入新类中
				ArrayList<String> tempAList = null;
				
				if (TypeDocList.containsKey(cenIndex)){
					tempAList = TypeDocList.get(cenIndex);
				}else {
					tempAList = new ArrayList<String>();
				}
				tempAList.add(docString);
				TypeDocList.put(cenIndex, tempAList);
		//		System.out.print(cenIndex);
			}
			//更新质心
			refreshAllCentroid();
		}
	}
	/*
	 * 应当结束就为真
	 */
	private boolean isEnd(ArrayList<Centroid>  tempCentroidList){
		//TODO
		if (tempCentroidList == null){
			return false;
		}
		for (int i = 0; i < tempCentroidList.size(); i ++){
			double dist = distance(this.centroidList.get(i), tempCentroidList.get(i)) ;
			if (dist < K_Means.DISTance){
				return false;
			}
		}
		return true;
	}
	/**
	 * 计算质心的距离
	 * @param centroid
	 * @param centroid2
	 * @return
	 */
	private double distance(Centroid centroid, Centroid centroid2) {
		if (centroid.getTermMap() != null && centroid2.getTermMap()!=null){
			return distance(centroid.getTermMap(), centroid.getWordCount(), centroid2.getTermMap(), centroid2.getWordCount());
		}
		return 0;
	}
	//计算某个文档和质心的距离
	private double distance(String string, Centroid centroid){
		SingleDoc singleDoc = AllDocMap.get(string);
		if (centroid == null){
			System.out.println("!!!");
		}
		if (singleDoc == null){
			System.out.println("==" + string+"\t"+AllDocMap.containsKey(string));
		}
		return distance(singleDoc.getTermMap(), singleDoc.getWordCount(), centroid.getTermMap(), centroid.getWordCount());
	}
	public double distance (HashMap<String, Double> map1, double tokenier1, HashMap<String, Double>map2, double tokenier2){
		Set<String> termSet1 = map1.keySet();
		Iterator<String> iterator = termSet1.iterator();
		double result = 0.0;
		while (iterator.hasNext()){
			String term = iterator.next();
			if (!map2.containsKey(term)){
				continue;
			}
			double count1 = map1.get(term);
			double count2 = map2.get(term);
			//计算变量
			double pcw = count2 / tokenier2;
			double pbw = computPBW(term);
			double pdw = (1 - K_Means.lambda) * count1/tokenier1 + K_Means.lambda * pbw;
			//第一部分
			double part1 = pcw * Math.log(pdw / (K_Means.lambda * pbw));
			double part2 = pdw * Math.log(pcw / (K_Means.lambda * pbw));
			result += part1 + part2;
		}
		return result;
	}
	//更新全部质心
	private void refreshAllCentroid() {
		System.out.print("(");
		for (int cenNo = 0; cenNo < centroidList.size(); cenNo ++){
			refreshSingleCentroid(cenNo);
		}
		System.out.println(")");
	}
	/**
	 * 更新单个质心
	 * @param cenNo
	 */
	private void refreshSingleCentroid(int cenNo){
		Centroid centroid = null;
		if (!TypeDocList.containsKey(cenNo) ||TypeDocList.get(cenNo).size() == 0){
			int size = K_Means.singleList.size();
			size *= Math.random();
			centroid = new Centroid(K_Means.singleList.get(size));
			System.err.print(","+cenNo+"="+size+"\t");
		}else{
			System.out.print(","+cenNo+"="+TypeDocList.get(cenNo).size()+"\t");
			ArrayList<String> docList = TypeDocList.get(cenNo);
			centroid = new Centroid(docList);
		}
		centroidList.set(cenNo, centroid);
		return;
	}
	/**
	 * 每次迭代完了，记录一些数据，保存一下断点
	 * 需要记录：1：质心集合列表，
	 * 					2：所有文档的类别和质心
	 */
	private void record(){
		ToolsIO.write(centroidList);
		ToolsIO.write(TypeDocList);		
	}
	/**
	 * 有问题
	 * @param term
	 * @return
	 */
	/*private double computPBW(String term){
		//TODO
		double p_all = 0.0;
		int num = 0;
		for(int i = 0; i < centroidList.size(); i ++){
			if (!centroidList.get(i).getTermMap().containsKey(term)){
				p_all+= 0; 
			}else {
				p_all +=  centroidList.get(i).getTermMap().get(term)/ centroidList.get(i).getWordCount();
				num++;
			}
		}
		if (num == 0){
			return 0;
		}else {
			return p_all/num;
		}
	}*/
	private double computPBW(String term){
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
}















