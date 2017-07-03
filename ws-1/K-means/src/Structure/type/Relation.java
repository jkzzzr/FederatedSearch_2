package Structure.type;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import K_means.AllPath;
import K_means.Classify;
import K_means.K_Means;

public class Relation {
	public static int K = K_Means.K;
	public static int SIZE = 10000;
	public static double writePercent = 0.5;
	public static long PERIOD = 1800 * 1000;//办小时刷新出来一次
	
	public static int ALLDOCNUM = -1;
	public volatile static ArrayList<ArrayList<Integer>> relation = new ArrayList<ArrayList<Integer>>(K);
	
	
//	private static ArrayList<Integer> needWrite = new ArrayList<Integer>();
	
	private static HashMap<Integer,ArrayList<Integer>> WriteContent = 
			new HashMap<Integer, ArrayList<Integer>>(K);
	
	public static void initRelation(){
		relation.clear();
		for (int i = 0; i < K; i++){
			relation.add(i, new ArrayList<Integer>(SIZE));
		}
	}
	
	public static void run(){
		System.err.print("\tRECORD\t");
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				try {
					K_Means.semaphore.acquire();
					System.out.println("真奥义·时间静止！\t检测操作");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int iteration = K_Means.currentIteration;
				//记录下需要写的内容
				timeTask();

				System.out.println("真奥义·时间恢复！\t检测完毕");
				try {
					K_Means.semaphore.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//异步操作写内容
				writeType(iteration, false, null);
				System.out.println("假奥义·写操作结束！");
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(tt, PERIOD);
	}
	/**
	 * 
	 * @param iteration
	 * @param isleft 为真表示，这次写操作是，最终的剩下的文件的写操作。为假表示，这只是timer的增量操作
	 */
	public static void writeType(int iteration, boolean isleft, ArrayList<ArrayList<Integer>> tempRelation){
		File file = new File(AllPath.outputPath_Type + "/" + iteration);
		if (!file.exists() || !file.isDirectory()){
			file.mkdir();
		}
		if (isleft){
			ArrayList<ArrayList<Integer>>  templist = tempRelation;
			try {
				for (int i = 0; i < templist.size();i++){
					String path = AllPath.outputPath_Type +"/" +iteration +"/type" + i;
					BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
					for (Integer docno : templist.get(i)){
						bw.write(docno+"\t");
					}
					bw.write("\n");
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			for (Entry<Integer, ArrayList<Integer>> entry : WriteContent.entrySet()){
				Integer typeno = entry.getKey();
				ArrayList<Integer> al = entry.getValue();
				String path = AllPath.outputPath_Type +"/" +iteration +"/type" +typeno;
				/*String path = AllPath.outputPath_Type +"/Colock" +iteration +"/type" +typeno;
				File file2 = new File(AllPath.outputPath_Type +"/Colock" +iteration);
				if (!file2.exists() || !file2.isDirectory()){
					file2.mkdir();
				}*/
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
					for (Integer docno : al){
						bw.write(docno+"\t");
					}
					bw.write("\n++++");
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			WriteContent.clear();
		}
	}
	
	
	public static void timeTask(){
		WriteContent.clear();
		for (int i = 0 ; i < relation.size(); i++){
			if (relation.get(i).size() > Relation.SIZE * Relation.writePercent){
	//			needWrite.add(i);
				WriteContent.put(i, (ArrayList<Integer>)relation.get(i).clone());
				relation.get(i).clear();
			}
		}
		
	}
	/**
	 * 
	 * @param a 质心
	 * @param b 添加的一个小小的点
	 */
	public static void add(int a, int b,int iteration){
//		Relation.relation.get(a).add(b);
		ArrayList<Integer> temp = Relation.relation.get(a);
		boolean boo = false;
		ArrayList<Integer> tt = new ArrayList<>();
		if (AllPath.IsLastStep_flush){
			synchronized (temp) {
				temp.add(b);
				if (temp.size() > AllPath.flush_num){
					tt = new ArrayList<>(temp);
					temp.clear();
					boo = true;
				}
			}
			if (boo){
				Classify.write(tt, a, iteration);
				System.err.print(a+"-");
			}
			
		}
		else {
			synchronized (temp) {
				temp.add(b);
			}
		}
		
	}
	public static void main(String args[]){
		relation.add(new ArrayList<Integer>(){{add(1);add(3);}});
		relation.get(0).add(4);
		System.out.println(relation.get(0).size());
	}
}
