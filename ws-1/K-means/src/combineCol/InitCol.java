package combineCol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import K_means.AllPath;
import Structure.Centroid;
import Structure.SingleDoc;
import Structure.type.Relation;

public class InitCol {
	public static ArrayList<SingleDoc> Singlist = new ArrayList<>();
	public static void main(String[] args) {

	}

	public void init() throws Exception{
		System.out.println("初始化所有文档   InitCol");
		int index = 0;
		for (int i = 0; i<1500; i++){
			FileInputStream fis = new FileInputStream(AllPath.inputPath_Centroid +"/" + i);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Centroid centroid  = (Centroid)ois.readObject();
			if (centroid.termMap.size() <=500 || centroid.wordCount <= 500){
				continue;
			}

/*			File file = new File(AllPath.inputPath_Centroid +"/" + i);
			if (!file.exists() ){
				continue;
			}
			BufferedReader br = new BufferedReader(new FileReader(AllPath.inputPath_Centroid +"/" + i));
			ArrayList<Integer> al = new ArrayList<>();
			String line = "";
			while ((line = br.readLine())!=null){
				Integer integer = Integer.parseInt(line);
				al.add(integer);
			}
			if (al.size() <50){
				continue;
			}
			Centroid centroid = new Centroid(al);*/
			
			
			
			Entry.alldoclist.add(index ++);
			SingleDoc singleDoc = new SingleDoc(centroid.termMap, centroid.wordCount);
			InitCol.Singlist.add(singleDoc);
	//		System.out.println(centroid.wordCount+"\t"+centroid.termMap.size());
		}
		System.out.println("所有文档数量\t"+Entry.alldoclist.size()+"\t初始化文档数量\t"+InitCol.Singlist.size());
		
		
		
		Relation.ALLDOCNUM = index;
		Relation.K = Entry.K;
	}

	public SingleDoc getDocItemList(Integer i) {
		return Singlist.get(i);
	}
}
