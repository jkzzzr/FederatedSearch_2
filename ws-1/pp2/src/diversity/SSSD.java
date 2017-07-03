package diversity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * 
 * @author Administrator
 * @input	锟斤拷锟诫函锟斤拷锟斤拷锟斤拷锟诫，锟芥储锟结构为锟斤拷map锟芥储锟斤拷key为String-String锟斤拷前锟斤拷为小锟侥ｏ拷锟斤拷锟斤拷为锟斤拷锟�
 */
public class SSSD {
	public static String Path_Base = "/media/369e402a-3322-4a63-bc49-6fa2e843a7fe/";
	public static String Path_D = Path_Base+"/Query01NormScore_spamd/";
	public static String Path_R = Path_Base+"/diversity/sim/word0411/";
	public static String Path_Result = Path_Base+"Lee/0411/BFS/SSSD";
	public static double lambda = 0.25;
	public static String Path_mid = Path_Result+File.separator+"Record";
//	static int ,b,c,d,e=0;
	public static double Phi = 0.6;
	//划分集合，迭代的次数
	public static int COUNT = 10;

	public static int QID = 0;
	public static ArrayList<String> RELs = new ArrayList<String>();//store Relevance score file
	public static ArrayList<Double> RELd = new ArrayList<Double>();
	public static HashMap<String,HashMap<String,Double>> DIS = new HashMap<String,HashMap<String,Double>>();//store Dissimilarity score file
	public static ArrayList<String> SEL = new ArrayList<String>();//set of selected file
	public static ArrayList<String> DocList1 = new ArrayList<String>();
	public static HashMap<String,Double> SCORE = new HashMap<String,Double>();
	public static double Default1 = 1;
	public static double Default0 = 0;
	
	/**各个划分区域集合	 */
	public static HashMap<String,HashSet<String>> PSet = new HashMap<String,HashSet<String>>();
	
	
	
	
	/** store some document that all of whose value is "NAN"
	 */
	public static ArrayList<String> Rest = new ArrayList<String>();
	
	
	public static int K = 200;
	
	public static void main(String[] args) throws IOException {
		for(double i = 0.1;i <= 0.4;i += 0.01){
			System.out.println("i+\t"+i);
			lambda = i;
			f(lambda);
		}
//		lambda = 0;
//		f(0);
	}

	public static void f(double iterator) throws IOException{

		File f = new File(Path_R);
		String [] filename = f.list();
		String pathr = "";
		String pathd = "";
		String temp = "querys01NormScore_spamdOr.csv";
		String pathResult = "";
		filename = sort(filename);
		int year = 2009;
		System.out.println("Query:");
	
		BufferedWriter bw = new BufferedWriter(new FileWriter(Path_mid+"-"+iterator));
		long timeALL = System.currentTimeMillis();
		long timeAA = System.currentTimeMillis();
		
		for (int i = 0; i < filename.length ; i++){
			System.out.print("\t"+filename[i]);
			if(i%50==0){
				if(i!=0){
					System.out.println();
					WriteTime(System.currentTimeMillis()-timeALL,50,bw);
					timeALL = System.currentTimeMillis();
				}
			}
			
			long timeQ = System.currentTimeMillis();
			QID = Integer.parseInt(filename[i]);
			pathr = Path_D + ( year + (QID-1)/50 ) + temp;
			pathd = Path_R + QID;
			pathResult = Path_Result + File.separator+"SSSD-FAR-"+iterator+".CSV";
			
			initial(pathr , pathd , QID);
			selectP();
		//	System.out.print("PSet.size()\t"+PSet.size() +"\t");
			Function(iterator);
			showResult(pathResult);
			
			WriteTime(System.currentTimeMillis()-timeQ,1,bw);
		}
		WriteTime(System.currentTimeMillis() - timeAA,3,bw);
		bw.close();
	}
	/**
	 * x==1>>perQuery
	 * x==50>>per50Query
	 * x==3
	 * @param time
	 * @param x
	 * @throws IOException 
	 */
	public static void WriteTime(long time,int x,BufferedWriter bw) throws IOException{
		String r = "";
		if(x == 1){
			r = "\t\tper: "+time;
		}else if(x ==50){
			r = "\tALL50: "+time;
		}else {
			r = "ALL: "+time;
		}
		bw.write(r);
		bw.newLine();
		bw.flush();
		
	}
	/**
	 * sort for String[]
	 */
	private static String[] sort(String [] str) {
		String temp[] = new String [str.length];
		TreeSet<String> ts = new TreeSet<String>((o1,o2)->{
			int x = Integer.parseInt(o1);
			int y = Integer.parseInt(o2);
			return (x-y)>0? 1:(x-y)<0?-1:0;
		});
		for(int i =0;i<str.length;i++){
			ts.add(str[i]);
		}
		for(int i =0;i< str.length;i++){
			temp[i] = ts.first();
			ts.remove(ts.first());
		}
		return temp;
	}

	public static void initial(String pathR,String pathD,int qid){
		DocList1.clear();
		RELs.clear();
		RELd.clear();
		DIS.clear();
		DocList1.clear();
		Rest.clear();
		PSet.clear();
		SCORE.clear();
		BufferedReader brR = null;
		String temp = "";
		
		try {

			brR = new BufferedReader(new FileReader(pathR));
			while((temp = brR.readLine())!=null){
				String tmpStr[]= temp.split(" ");
				if(qid > Integer.parseInt(tmpStr[0])){
					continue;
				}else if (qid < Integer.parseInt(tmpStr[0])){
					break;
				}
				String tmp1 = tmpStr[2];
				double tmp2 = Double.parseDouble(tmpStr[4]);
				
				RELs.add(tmp1);
				RELd.add(tmp2);
			}
			brR.close();
			//read dissimilarity file
			brR = new BufferedReader(new FileReader(pathD));
			
			temp = brR.readLine();
			String tmpStr[] = temp.split(" ");
			for(int i = 0;i <tmpStr.length ; i++){
				DocList1.add(tmpStr[i]);
			}
			String tmpdoc = "";
			while((temp = brR.readLine())!=null){
				tmpStr = temp.split(" ");
				tmpdoc = tmpStr[0];
				String tmpdoc2 = "";

				
				if(!RELs.contains(tmpdoc)){
					continue;
				}
				if(tmpdoc.contains("clueweb09-enwp01-49-16274")){
	
				}
				
				int tpi = 0;
				for(tpi = 0; tpi < DocList1.size(); tpi++){
					tmpdoc2 = DocList1.get(tpi);

					if(!RELs.contains(tmpdoc2)){
						continue;
					}

					if((tpi + 1) >= tmpStr.length){
						break;
					}
					if(tmpStr[tpi+1].equals("NaN")){

						continue;
					}
					
					double score = Double.parseDouble(tmpStr[tpi+1]);
		
					if(tmpdoc.equals(tmpdoc2)){
						continue;
					}
					
					boolean tb = false;
					if( tmpdoc.compareTo(tmpdoc2) > 0 ){
						tb = true;
						String t = tmpdoc;
						tmpdoc = tmpdoc2;
						tmpdoc2 = t;
					}else{
					}

					if(DIS.containsKey(tmpdoc)){
						HashMap<String,Double> tmpHM = new HashMap<String,Double>();
						if(DIS.get(tmpdoc)!=null){
							tmpHM = DIS.get(tmpdoc);
						}
						tmpHM.put(tmpdoc2, score);
						DIS.replace(tmpdoc, tmpHM);
					}else{
						HashMap<String,Double> tmpHM = new HashMap<String,Double>();
						tmpHM.put(tmpdoc2, score);
						DIS.put(tmpdoc, tmpHM);
					}	
					if(tb){
						String t = tmpdoc;
						tmpdoc = tmpdoc2;
						tmpdoc2 = t;
						tb = false;
					}
					
				}
			}
			brR.close();
			
			
		} catch (FileNotFoundException e) {
			System.out.println("(MaxSum - initial-3) - Problem in load Relevance File ");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("(MaxSum - initial-4) - Problem in Reading Relevance File ");
			e.printStackTrace();
		}
		
		return;
	}
	
	public static void Function(double lambda){
		SEL.clear();
		ArrayList<String> alDoc = new ArrayList<String>();
		int length = K > RELs.size()? RELs.size(): K;
		double dis_First_Value = Double.MAX_VALUE;
		for(int i = 0;i < RELs.size();i++){
			double distance = dis_First_Value;
			String doc1 = RELs.get(i);
			//MAX部分
			for(String doc2: PSet.keySet()){
				boolean b = false;
				double score = 0;
				if(doc1.compareTo(doc2)>0){
					String t = doc2;
					doc2 = doc1;
					doc1 = t;
					b = true;
				}
				if(canRead(doc1,doc2)){
					score = DIS.get(doc1).get(doc2);
					if(distance > score){
						distance = score;
					}
				}
				if(b){
					String t = doc2;
					doc2 = doc1;
					doc1 = t;
					b = false;
				}		
			}
			if(distance == dis_First_Value){
				distance = 1;
			}
			double ss = RELd.get(i);
			double Score = (1 - lambda) * ss + lambda * ( 1 - distance);
			if(Score<0){
				System.out.println(Score);
			}
			SCORE.put(doc1, Score);
			alDoc.add(doc1+"+"+Score);

		}			
		sort2(alDoc);
	}

	private static void sort2(ArrayList<String> alDoc) {
		SEL.clear();
		if(alDoc.size()<=1){
			return ;
		}
		alDoc.sort((o1,o2)->{
			String [] str1 = o1.split("[+]");
			String [] str2 = o2.split("[+]");
			String doc1 = str1[0];
			String doc2 = str2[0];
			double s1 = Double.parseDouble(str1[1]);
			double s2 = Double.parseDouble(str2[1]);
			if(s1 > s2){
				return -1;
			}else if(s1 < s2){
				return 1;
			}
			return 0;
		});
		alDoc.forEach((o1)->{
			String s[] = o1.split("[+]");
			if(s.length<1){
			}
			SEL.add(s[0]);
		});
	}
	public static void showResult(String pathR){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(pathR,true));
			for (int i = 0 ;i < SEL.size() ; i++){
				bw.write(decorate(SEL.get(i),i));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static String decorate(String docid,int rank){
		int qid = getQID();
		double s = SCORE.get(docid);
		String resultStr = qid+"\tQ0\t"+docid+"\t"+rank+"\t"+s+"\t"+"WYY"+SSSD.lambda;
		return resultStr;
	}
	public static int getQID(){
		return QID;
	}
	
	public static void selectP(){
		int temp = -1;
		for(int i = 0;i < COUNT ;i++){
			if(PSet.size()==temp){
				break;
			}else{
				temp = PSet.size();
			}
			selectP1();
			selectP2();
		}
	}
	/**对给定的中心点，根据PHi值，重新选择分类点集。
	 * @param RELs	,	PHi,	PSet
	 */
	public static void selectP1(){
		ArrayList<String> tmpAL = new ArrayList<String>(RELs);
		if(PSet.isEmpty()){
			String temp = RELs.get(0);
			tmpAL.remove(temp);
			PSet.put(temp, new HashSet<String>());
		}else{
			for(String tmpd : PSet.keySet()){
				tmpAL.remove(tmpd);
			}
		}
		//外层循环遍历所有待加入的文档，内层循环是所有已经存在的集合。
		//***********************注意最后没有i++
		for(int i = 0;i < tmpAL.size();  ){
			String doc1 = tmpAL.get(i);
			/**初始假设doc1独立于所有集合外	 */
			boolean isDoc1Out = true;
			for(String doc2 :PSet.keySet()){
				/**初始假设doc1与doc2没有发生交换	 */
				boolean b = false;
				if(doc1.compareTo(doc2) > 0){
					String tempff = doc1;
					doc1 = doc2;
					doc2 = tempff;
					b = true;
				}
				//判断是否到底部了
				boolean bottle = false;
				if(DIS.containsKey(doc1)){
					if(DIS.get(doc1)!=null){
						if(DIS.get(doc1).containsKey(doc2)){
							if(DIS.get(doc1).get(doc2)!=null){
								bottle = true;
								//遍历所有已经存在的集合看doc1是否在其中，有一个在，则设置为false，加入对应集合
								double score = DIS.get(doc1).get(doc2);
								if(score > Phi){
									isDoc1Out = false;
									HashSet<String> temphs = new HashSet<String>();
									String tmpDoc2 = doc2;
									if(b){	tmpDoc2 = doc1;}
									temphs = PSet.get(tmpDoc2);
									temphs.add(tmpAL.get(i));
									PSet.replace(tmpDoc2, temphs);
								}
							}
						}
					}
				}
				//如果是因为读取DIS数据时候，存在为空，或者什么情况，则直接作为一个单独集合存储
				if(!bottle){	isDoc1Out = true;}
				if(b){
					String tempff = doc1;
					doc1 = doc2;
					doc2 = tempff;
					b = false;
				}
			}
			if(isDoc1Out){
				PSet.put(doc1, new HashSet<String>());
			}
			tmpAL.remove(doc1);
		}
	}
	
	public static void selectP2(){
		ArrayList<String> result = new ArrayList<String>();
		for(String doc1 : PSet.keySet()){
			ArrayList<String> tmpA = new ArrayList<String>(PSet.get(doc1));
			if(tmpA.size() <= 1){
				result.add(doc1);
				continue;
			}
			tmpA.add(doc1);
			double distance = 0.0;
			double TD = Double.MAX_VALUE;
			String TempString = doc1;
			//tmpA中的所有元素，进行重新选点
			for(int i = 0;i < tmpA.size();i++){
				for(int j = 1; j<tmpA.size();j++){
					String ts1 = tmpA.get(i);
					String ts2 = tmpA.get(j);
					boolean b = false;
					if(ts1.compareTo(ts2)>0){
						String tmpStr = ts1;
						ts1 = ts2;
						ts2 = tmpStr;
						b = true;
					}
					double score = 0.0;
					
					if(canRead(ts1,ts2)){
						score = DIS.get(ts1).get(ts2);
					}
					distance += score;
					if(b){
						String tmpStr = ts1;
						ts1 = ts2;
						ts2 = tmpStr;
						b = false;
					}
				}
				if(distance < TD){
					TD = distance;
					TempString = tmpA.get(i);
				}
			
			}	
			result.add(TempString);		
		}
		PSet.clear();
		for(String str:result){
			PSet.put(str, new HashSet<String>());
		}
		return;
	}
	public static boolean canRead(String doc1 , String doc2){
		if(DIS.containsKey(doc1)){
			if(DIS.get(doc1)!=null){
				if(DIS.get(doc1).containsKey(doc2)){
					if(DIS.get(doc1).get(doc2)!=null){
						return true;
					}
				}
			}
		}
		return false;
	}
}




































