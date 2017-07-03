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
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 
 * @author Administrator
 * @input	閿熸枻鎷烽敓璇嚱閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓璇紝閿熻姤鍌ㄩ敓缁撴瀯涓洪敓鏂ゆ嫹map閿熻姤鍌ㄩ敓鏂ゆ嫹key涓篠tring-String閿熸枻鎷峰墠閿熸枻鎷蜂负灏忛敓渚ワ綇鎷烽敓鏂ゆ嫹閿熸枻鎷蜂负閿熸枻鎷烽敓锟�
 */
public class QPRP {
	public static String Path_Base = "/media/369e402a-3322-4a63-bc49-6fa2e843a7fe/";
	public static String Path_D = Path_Base+"/Query01NormScore_spamd/";
	public static String Path_R = Path_Base+"/diversity/sim/word0411/";
	public static String Path_Result = Path_Base+"Lee/0411/BFS/QPRP";
	public static double lambda = 0.25;
	public static String Path_mid = Path_Result+File.separator+"Record";
//	static int ,b,c,d,e=0;
	public static double Phi = 0.6;
	//鍒掑垎闆嗗悎锛岃凯浠ｇ殑娆℃暟
	public static int COUNT = 10;

	public static int QID = 0;
	public static ArrayList<String> RELs = new ArrayList<String>();//store Relevance score file
	public static ArrayList<Double> RELd = new ArrayList<Double>();
	public static HashMap<String,HashMap<String,Double>> DIS = new HashMap<String,HashMap<String,Double>>();//store Dissimilarity score file
	public static ArrayList<String> SEL = new ArrayList<String>();//set of selected file
	public static ArrayList<String> DocList1 = new ArrayList<String>();
	//存储最后计算得到的分值
	public static HashMap<String,Double> SCORE = new HashMap<String,Double>();
	
	
	
	
	/** store some document that all of whose value is "NAN"
	 */
	public static ArrayList<String> Rest = new ArrayList<String>();
	
	
	public static int K = 200;
	
	public static void main(String[] args) throws IOException {
		for(double i = 0.1;i < 1;i += 0.1){
			System.out.println("i+\t"+i);
			f(lambda);
		}
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
			pathResult = Path_Result + File.separator+"QPRP.CSV";
			
			initial(pathr , pathd , QID);
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
			for(int i =0;i<Rest.size();i++){
				System.out.println(Rest.get(i));
			}
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

		BufferedReader brR = null;
		String temp = "";
		
		try {

			brR = new BufferedReader(new FileReader(pathR));
			int count = 0;
			
			while((temp = brR.readLine())!=null){
				count ++;
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
				if(!DIS.containsKey(tmpdoc)||DIS.get(tmpdoc).isEmpty()){
					DIS.remove(tmpdoc);
					int x = RELs.indexOf(tmpdoc);
					RELs.remove(x);
					RELd.remove(x);
					Rest.add(tmpdoc);
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
		ArrayList<String> tmpAL1 = new ArrayList<String>(RELs);
		ArrayList<Double> tmpAL2 = new ArrayList<Double>(RELd);
		int length = K > RELs.size()? RELs.size(): K;
		int iterator = 0;
		while(iterator < length){
			int choose = 0;
			double max = Double.MAX_VALUE;
			double sum = -max;

			double dddd1 = 0.0;
			double dddd2 = 0.0;
			
			for(int i = 0;i < tmpAL1.size();i++){
				String doc1 = tmpAL1.get(i);
				double score1 = tmpAL2.get(i);
				double score2 = 0;
				double scoreSum = 0;
				int count = 0;
				for(int j = 0;j < SEL.size();j++){
					String doc2 = SEL.get(j);
					double tmpScore = 0;
					
					boolean b = false;
					if(doc1.compareTo(doc2)>0){
						String t = doc2;
						doc2 = doc1;	
						doc1 = t;
						b = true;
					}
					if(canRead(doc1,doc2)){
						double t1 = tmpAL2.get(i);
						int index = RELs.indexOf(SEL.get(j));
						double t2 = RELd.get(index);
						double t3 = DIS.get(doc1).get(doc2);
						tmpScore = Math.sqrt(t1*t2)*t3;
						
						score2 += tmpScore;
						
						count++;

					}else{
					}
					if(b){
						String t = doc2;
						doc2 = doc1;
						doc1 = t;
						b = false;
					}		
				}
				if(count != 0){
					count = 1;
					score2 /= count;
				}else {
					score2 = 1;
				}
				scoreSum = score1 - score2;
				dddd1 = score1;
				dddd2 = score2;
				if(sum < scoreSum){
					sum = scoreSum;
					choose = i;
				}
			
			}			
		SEL.add(tmpAL1.get(choose));
		
		
		SCORE.put(tmpAL1.get(choose), sum);
		tmpAL1.remove(choose);
		tmpAL2.remove(choose);
		iterator++;
		}
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
		String resultStr = qid+"\tQ0\t"+docid+"\t"+rank+"\t"+s+"\t"+"WYY-NoDiv";
		return resultStr;
	}
	public static int getQID(){
		return QID;
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




































