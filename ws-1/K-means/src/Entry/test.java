import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import Structure.Centroid;


public class test {

	public static Semaphore semaphore1 = new Semaphore(1, false);
	public static ArrayList<ArrayList<Integer>> al = new ArrayList<>();
//	public static Semaphore semaphore2 = new Semaphore(-1);
	public static int count = 0;
	public static void main(String args[]) throws Exception{
		TreeSet<Integer> ts = new TreeSet<>();
		int count = 0;
		for (int i = 0; i < 433; i++){
			FileInputStream fis = new FileInputStream("/home/Lee/data/output-FR/K-means/abc/234.3");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Centroid centroid = (Centroid) ois.readObject();
			if (centroid.getTermMap().size() < 900){
				System.err.println(i+"\t" + centroid.getTermMap().size());
				continue;
			}
			ts.add(centroid.getTermMap().size());
			count ++;
	//		System.out.println(centroid.wordCount);
			ois.close();
			int x = 0/1;
		}
		ts.forEach(o1->{
			System.out.println(o1);
		});
	System.out.println("count\t"+count +"\t"+ts.size());
	}
	public static void run(){
		try {			
			System.out.println("START");
			long timeStart = System.currentTimeMillis();
			for (int i = 0; i<100000000; i++){
				test.semaphore1.acquire();
				test.semaphore1.release();
			}
			System.out.println(System.currentTimeMillis() - timeStart);
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void run1(int a, int b){
		ArrayList<Integer> temp = al.get(a);
		synchronized (temp) {
			temp.add(b);
		}
	}
}
