package allprocess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import compute.Com_Sim_K_means;
import process2.Pre_resultTOsingledoc;
import structure.SingleDoc;

/**
 * 构建相似度矩阵，输出到文件中
 * @author Administrator
 *
 */
public class p1 {
	public static int thread_Number = 10;
	public static Stack<Byte> sTACK = new Stack<Byte>();

	public static String inPathString="E:/实验数据/123";
	public static String outPathString="E:/实验数据/result";
	
	public static void main(String []argsStrings){
		ExecutorService es = Executors.newFixedThreadPool(thread_Number);
		for (int i = 151; i < 152; i++){
			MyThread myThread = new MyThread(i, inPathString, outPathString+"/"+i);
		//	es.execute(myThread);
			myThread.run();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while (!sTACK.isEmpty()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		es.shutdownNow();
		System.out.println("END");
		
	}
}
class MyThread extends Thread{
	public int qid ;
	public String inputPath;
	private String outputPath;
	/**
	 * 先将qid docno转化为singledoc的list
	 * 再计算相似度
	 */
	@Override
	public void run(){
		p1.sTACK.add((byte) 0);

		Pre_resultTOsingledoc p = new Pre_resultTOsingledoc();
		ArrayList<Integer> alArrayList = new ArrayList<Integer>();
		ArrayList<SingleDoc> result = new ArrayList<SingleDoc>();
		try {
			alArrayList = p.init_al_int(inputPath, qid);
			result = p.init_al_singledoc(alArrayList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Com_Sim_K_means csk = new Com_Sim_K_means(qid, true);
		csk.allDocs = result;
	//	System.out.println("!!!!" + csk.allDocs.size() +"\t" + (csk.allDocs.get(100)==null));
		for (int i = 0; i < alArrayList.size(); i++){
			for (int j = 0; j < alArrayList.size(); j++){
				Integer index1 = alArrayList.get(i);
				Integer index2 = alArrayList.get(j);
				csk.getSim(i, j);
			}
		}
		write(csk.SIMap);
		p1.sTACK.pop();
	}
	public MyThread(int qid, String inputPath, String outputPath) {
		super();
		this.qid = qid;
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}
	public void write(Object object){
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(new File(outputPath));
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
