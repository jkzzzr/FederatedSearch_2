package combineCol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import K_means.AllPath;
import K_means.Classify;
import K_means.K_Means;
import K_means.ToolsIO;
import Structure.Centroid;
import Structure.Create_Doc2;
import Structure.SingleDoc;
import Structure.type.Relation;

public class Entry {

	private static final int ITERATION = 100;
	private static final double DISTance = 1.5;
	public static Stack<Boolean> STACK_Centroid = new Stack<>();

	//分的个数
	public static int K = 100;
	//所有文档名列表
	
	
	public static InitCol initCol = null;
	public static int startIteration = -1;
	public static ArrayList<Integer> alldoclist = new ArrayList<Integer>();
	public static ArrayList<Centroid>  centroidList = new ArrayList<Centroid>();
	public static double lambda = 0.5;
	
	public static Stack<Boolean> STACK_Run = new Stack<>();
	public static Stack<Boolean> STACK_Record = new Stack<>();
	public static double distanceDEFAULT = Double.MIN_VALUE;
	
	public static ArrayList<ArrayList<Integer>> SomeArrayList = new ArrayList<>();
	
	public static int num_Run = 12;
	public static int num_Centroid = 5;
	public static int currentIteration;
	
	public static void main(String[] args){
		K_Means.create_Doc2 = new Create_Doc2();
		long startTime = System.currentTimeMillis();
		System.out.println("START  /K-means/src/combineCol/Entry.java--给3部分产生的数据集，再归类");
		Relation.initRelation();
		Entry classify = new Entry();
		try {
			classify.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("总用时：：：：\t"+(System.currentTimeMillis() - startTime));
	}
	public Entry(){
		System.out.println("!!!!!!!!");
		initCol = new InitCol();
		try {
			initCol.init();
			initCentroid();
			init_SomeArrayList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化静态变量
	 */
	public void initdocList(){
		System.out.println("@K_meams - initStatic()\tClassify初始化静态变量 singleList和docList");
		try {
//			create_Doc2.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("@Classify - initStatic() - end");
	}
	public void init_SomeArrayList(){
		int skipIndex = Relation.ALLDOCNUM / Entry.num_Run;
//		System.out.println(skipIndex+"==="+Relation.ALLDOCNUM + Entry.num_Run);
		int fromIndex = 0;
		int endIndex = 0;
		int allcount = 0;
		for (int ii = 0; ii <= Entry.num_Run *2;ii++){
			fromIndex = endIndex;
			endIndex = fromIndex + skipIndex;
			if (fromIndex >= alldoclist.size()){
				break;
			}
			if (endIndex >alldoclist.size()){
				endIndex = alldoclist.size();
			}
			//前闭后开[ , )
			ArrayList<Integer> aList = new ArrayList<>(alldoclist.subList(fromIndex, endIndex));
			SomeArrayList.add(aList);
			allcount += aList.size();
		}
		alldoclist.clear();
		System.out.println("list分快结束!总文件数量为:\t"+allcount);
	}
	/**
	 * 初始化质心
	 * @throws Exception 
	 * @throws Exception 
	 */
	public void initCentroid() throws Exception{
	//	Thread.sleep(10000);
		System.out.println("@K_means - initCentroid()\t初始化质心");
		centroidList = new ArrayList<Centroid>();
		//随机选出K个文档作为中心，存放在centoridmap中
		ArrayList<Integer> indexlist = new ArrayList<Integer>();
		for (int i = AllPath.K_Start; i< AllPath.K_End; i++){
			while (true){
				int no = (int) ( Entry.alldoclist.size()* Math.random());
				if (indexlist.contains(no)){
					continue;
				}else {
				//	SingleDoc singleDoc = AllDocMap.get(K_means.K_Means.docList.get(no));
					int docnumber = Entry.alldoclist.get(no);
					SingleDoc singleDoc = initCol.getDocItemList(docnumber);
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
		indexlist.clear();
		System.out.println("@K_means - initCentroid()\tend\t质心数量"+centroidList.size());
		System.err.println("初始化质心完成");
	}
	private boolean isEnd(ArrayList<Centroid>  tempCentroidList){
		//TODO
		if (tempCentroidList == null){
			return false;
		}
		for (int i = 0; i < tempCentroidList.size(); i ++){
			double dist = distance(Entry.centroidList.get(i), tempCentroidList.get(i)) ;
			if (dist < Entry.DISTance){
				return false;
			}
		}
		return true;
	}
	private void record(){
		Thread thread = new Thread(){
			@Override
			public void run(){
				ArrayList<ArrayList<Integer>> temp = null;
				synchronized (Relation.relation) {
					temp = (ArrayList<ArrayList<Integer>>) Relation.relation.clone();
					Relation.initRelation();
				}
				ToolsIO.write(Entry.currentIteration, centroidList);	
	//			Relation.writeType(K_Means.currentIteration, true, temp);
			}
		};
		thread.start();
		System.err.println("RECORD！");
	}
	/**
	 * 整体流程
	 * @throws Exception 
	 */
	public void run() throws Exception{
		System.out.println("@Entry - run()\t开始执行");
		ArrayList<Centroid>  tempCentroidList = null;
		System.err.println("@Entry - run()\t 文档总数量为:\t" +  Entry.alldoclist.size());
		
		for (int iteration = startIteration + 1; iteration < ITERATION; iteration++){
			System.out.println("@K_means - run()/t第"
					+ iteration +"次迭代\t");
			//是否满足终止条件
			if ( isEnd(tempCentroidList) ){
				centroidList = tempCentroidList;
				record();
				return;
			}
			Entry.currentIteration = iteration;
			//每次迭代开始，清空记录表
			tempCentroidList = centroidList;
			if (iteration != 0){
				record();
			}
						
				long currentTime = System.currentTimeMillis();
				for (int ii = 0; ii < SomeArrayList.size(); ii ++){	
					ArrayList<Integer> tempaal = SomeArrayList.get(ii);
					MyThread_Run myThread_Run = new MyThread_Run(tempaal);
					myThread_Run.start();
				}
			while(true){
				//每半个小时检测一遍，看看是否运行完了
				Thread.sleep(1000 * 10);
				if (STACK_Run.isEmpty()){
					break;
				}
			}
			System.out.println("@K_means - run()/t第"
					+ iteration +"次迭代的分类耗时：\t" +(System.currentTimeMillis() - currentTime));
			currentTime = System.currentTimeMillis();
			//更新质心
			refreshAllCentroid();
			System.out.println("@K_means - run()/t第"
					+ iteration +"次迭代计算质心耗时：\t" + (System.currentTimeMillis() - currentTime));
		}
		write();
/*	*******************************************************
		System.out.println("@Classify - run()\t开始执行");
		System.err.println("@Classify - run()\t 文档总数量为:\t" +  alldoclist.size());
		long currentTime = System.currentTimeMillis();
		for (int ii = 0; ii < SomeArrayList.size(); ii ++){	
			ArrayList<Integer> tempaal = SomeArrayList.get(ii);
			MyThread_1 myThread_1 = new MyThread_1(tempaal);
			myThread_1.start();
		}
		System.out.println("@Classify - run()/t"
				+ "分类耗时：\t" +(System.currentTimeMillis() - currentTime));
		currentTime = System.currentTimeMillis();
		//更新质心
//		write();
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				System.out.print("WRITE");
				try {
					write();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, 1000 * 1,1000 * 1);
		while (true){
			//每2分钟检查一次看看执行好了没
			Thread.sleep(1000 * 3);
			if (STACK_Record.isEmpty()){
		//		timer.cancel();
				write();
				break;
			}
		}
		System.out.println("@Classify - run()/t第"
				+ "写出耗时：\t" + (System.currentTimeMillis() - currentTime));*/
	}
	private void refreshAllCentroid() throws Exception {
	//	write();
		System.out.print("(");
		int skip = K / num_Centroid;
		int fromIndex = 0;
		int toIndex = -1;
		for (int ii = 0; ii <= num_Run * 2; ii ++){
			fromIndex = ++ toIndex ;
			toIndex = fromIndex +skip;
			if (fromIndex >= AllPath.K_End){
				continue;
			}
			if (toIndex >= AllPath.K_End){
				toIndex = AllPath.K_End-1;
			}
		//	refreshSingleCentroid(cenNo);
			MyThread_centroid myThread_centroid = new MyThread_centroid(fromIndex, toIndex);
			myThread_centroid.start();
			
		}
		while (true){
			//每2分钟检查一次看看执行好了没
	//		Thread.sleep(1000 * 120);
			Thread.sleep(1000 * 1);
			if (STACK_Centroid.isEmpty()){
				break;
			}
		}
		System.out.println(")");
	}
	public void write() throws Exception{
		int skip = K / Entry.num_Centroid;
		int fromIndex = 0;
		int toIndex = -1;
		for (int ii = 0; ii <= K + 1; ii ++){
			fromIndex =  ++ toIndex;
			toIndex = fromIndex +skip;
			if (fromIndex >= K){
				continue;
			}
			if (toIndex >= K){
				toIndex = K-1;
			}
			MyThread_Record myThread_Record = new MyThread_Record(fromIndex, toIndex);
			myThread_Record.start();
			
		}
		System.out.println(")");
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
	public static double distance(Integer string, Centroid centroid){
		SingleDoc singleDoc = initCol.getDocItemList(string);
		return distance(singleDoc.getTermMap(), singleDoc.getWordCount(), centroid.getTermMap(), centroid.getWordCount());
	}
	public static double distance (HashMap<Integer, Double> map1, double tokenier1, HashMap<Integer, Double>map2, double tokenier2){
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
			double pbw = computPBW(term);
			double pdw = (1 - lambda) * count1/tokenier1 + lambda * pbw;
			//第一部分
			double part1 = pcw * Math.log(pdw / (lambda * pbw));
			double part2 = pdw * Math.log(pcw / (lambda * pbw));
			result += part1 + part2;
		}
		return result;
	}

	private static double computPBW(Integer term){
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
	public static void write (ArrayList<Integer> alArrayList, int cenNo){

		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(AllPath.outputPath_Type +"/type-200" + "/TYPE-" + cenNo, true));
			for (int i = 0; i < alArrayList.size(); i++){
				bw.write(alArrayList.get(i)+"\n");
		}
			bw.close();
			alArrayList.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}

class MyThread_1 extends Thread{
	ArrayList<Integer> arrayList = new ArrayList<>();
	public MyThread_1(ArrayList<Integer> tempAlist){
		arrayList = tempAlist;
	}
	@Override
	public void run(){
		Entry.STACK_Run.add(false);
		System.out.print(" + ");
		try{
			for (int ii = 0; ii < arrayList.size(); ii++){
				int docNo = arrayList.get(ii);
				//计算文档与各个中心之间的距离
				double maxDouble = Entry.distanceDEFAULT;
				int cenIndex = 0;
				//选择合适的中心
				for (int cenNo = 0; cenNo < Entry.centroidList.size(); cenNo ++){
					double tempDistance = Entry.distance(docNo, Entry.centroidList.get(cenNo));
					if (maxDouble < tempDistance){
						maxDouble = tempDistance;
						cenIndex = cenNo;
					}
				}
				//将文档加入新类中
				Relation.add(cenIndex, docNo, Entry.currentIteration);
			}
		}finally {
			Entry.STACK_Run.pop();
			System.err.print("S("+ arrayList.size()+")   ");
		}
		Thread.currentThread().interrupt();
	}
}
class MyThread_Record extends Thread{
	int fromIndex;
	int toIndex;
	public MyThread_Record(int from, int to){
		fromIndex = from;
		toIndex = to;
	}
	@Override
	public void run(){
		Entry.STACK_Record.add(false);
		for (int cenNo = fromIndex; cenNo <= toIndex; cenNo++){
			Centroid centroid = null;
			if (Relation.relation.size() <= cenNo || cenNo <0){
				continue;
			}
			ArrayList<Integer> alArrayList = Relation.relation.get(cenNo);
			File file = new File(AllPath.outputPath_Type +"/" +"END");
			if (!file.exists() || !file.isDirectory()){
				file.mkdir();
			}
			ArrayList<Integer> tempp = new ArrayList<>();
			synchronized (alArrayList) {
				tempp = new ArrayList<Integer>(alArrayList);
				alArrayList.clear();
			}
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(AllPath.outputPath_Type +"/END" + "/TYPE " + cenNo, true));
	//			int xxxshow = alArrayList.size() / 500;
				for (int i = 0; i < tempp.size(); i++){
					bw.write(tempp.get(i)+"\n");
	/*				if (i % xxxshow ==0){
						bw.flush();
					}
	*/			}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Entry.STACK_Record.pop();
		Thread.currentThread().interrupt();
		return;
	}
	
}


class MyThread_Run extends Thread{
	ArrayList<Integer> arrayList = new ArrayList<>();
	public MyThread_Run(ArrayList<Integer> tempAlist){
		arrayList = tempAlist;
	}
	@Override
	public void run(){
		Entry.STACK_Run.add(false);
		System.out.print(" + ");
		try{
			for (int ii = 0; ii < arrayList.size(); ii++){
				int docNo = arrayList.get(ii);
		/*		System.out.println(docNo);
				if (!K_Means.alldoclist.contains(docNo)){
					System.out.println("!!!!!" + docNo);
					continue;
				}*/
				//计算文档与各个中心之间的距离
				double maxDouble = Entry.distanceDEFAULT;
				int cenIndex = 0;
				//选择合适的中心
				for (int cenNo = 0; cenNo < Entry.centroidList.size(); cenNo ++){
					double tempDistance = Entry.distance(docNo, Entry.centroidList.get(cenNo));
					if (maxDouble < tempDistance){
						maxDouble = tempDistance;
						cenIndex = cenNo;
					}
				}
				//将文档加入新类中
			//	Relation.relation.get(cenIndex).add(docNo);
				Relation.add(cenIndex, docNo, Entry.currentIteration);
			}
		}finally {
			Entry.STACK_Run.pop();
			System.err.print("S("+ arrayList.size()+")   ");
		}
		Thread.currentThread().interrupt();
	}
}
class MyThread_centroid extends Thread{
	int fromIndex;
	int toIndex;
	public MyThread_centroid(int from, int to){
		fromIndex = from;
		toIndex = to;
	}
	@Override
	public void run(){
		Entry.STACK_Centroid.add(false);
		for (int cenNo = fromIndex; cenNo <= toIndex; cenNo++){
			Centroid centroid = null;
			/*if (!Relation.relation.contains(cenNo)){
				continue;
			}*/
			if (cenNo >= Relation.relation.size() || cenNo <0){
				continue;
			}

			ArrayList<Integer> tempAl = Relation.relation.get(cenNo);
			/*if (tempAl==null){
				tempAl = new ArrayList<Integer>();
			}*/
			if (tempAl!=null && tempAl.size() !=0){
				Classify.write(tempAl, cenNo, Entry.currentIteration);
			}

			
			if (Relation.relation.get(cenNo).size() == 0){
				int size = InitCol.Singlist.size();
				System.out.print("(SIZE:"+ size+")");
				size *= Math.random();
				centroid = new Centroid(Entry.initCol.getDocItemList(size));
			}else{
				ArrayList<Integer> docList = new ArrayList<>(10000);
				docList.addAll(Relation.relation.get(cenNo));
				centroid = new Centroid(docList);
			}
		Entry.centroidList.set(cenNo, centroid);
		}
		Entry.STACK_Centroid.pop();
		Thread.currentThread().interrupt();
		return;
	}
}








