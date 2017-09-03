package K_means;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import Structure.Centroid;

public class ToolsIO {
	/*public static enum WriteType {
		CENTROID_LIST, CENTROID_SINGLE, DOC_LIST, DOC_SINGLE
	}*/
	/*public static String Path_Centorid_ALL= "";
	public static String Path_DOC_ALL = "";
	public static BufferedWriter bWriter_Centorid_ALL = null;
	public static BufferedWriter bWriter_DOC_ALL = null;*/


	public ToolsIO() {
	}

	public static void init(){
		
	}
	public static void close(){
		/*try {
			if (bWriter_Centorid_ALL != null){
				bWriter_Centorid_ALL.close();
			}
			if (bWriter_DOC_ALL != null){
				bWriter_DOC_ALL.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}
	
	public static void main(String[] args) {

	}

	public static void write(ArrayList<Centroid> aList){
		/*bWriter_Centorid_ALL.write("#CENTROID RECORD\n");
		bWriter_Centorid_ALL.write("#ITERATION:"+K_Means.currentIteration+"\n");*/
		for (int i = 0; i < aList.size(); i++){
			new ToolsIO().writeSingleCentroid(aList.get(i), AllPath.outputPath_Centroid+i);
		}
	}
	public static void write(HashMap<Integer, ArrayList<String>> hMap){
		new ToolsIO().writeTypeDoc(hMap, AllPath.outputPath_Type);
	}
	/**
	 * 序列化一个质心
	 * @param centroid
	 * @param outputPath
	 */
	private void writeSingleCentroid(Centroid centroid, String outputPath){
		FileOutputStream fos;
		ObjectOutputStream  oos;
		try {
			fos = new FileOutputStream(outputPath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(centroid);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 反序列化一个质心
	 * @param inputPath
	 * @return
	 */
	private Centroid readSingleCentroid(String inputPath){
		FileInputStream fis;
		ObjectInputStream ois;
		Centroid result = null;
		try {
			fis = new FileInputStream(inputPath);
			ois = new ObjectInputStream(fis);
			result = (Centroid)ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
	private void writeTypeDoc(HashMap<Integer, ArrayList<String>> hMap, String outputPath){
		FileOutputStream fos;
		ObjectOutputStream  oos;
		try {
			fos = new FileOutputStream(outputPath);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(hMap);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 反序列化一
	 * @param inputPath
	 * @return
	 */
	private HashMap<Integer, ArrayList<String>> readTypeDoc(String inputPath){
		FileInputStream fis;
		ObjectInputStream ois;
		HashMap<Integer, ArrayList<String>> result = null;
		try {
			fis = new FileInputStream(inputPath);
			ois = new ObjectInputStream(fis);
			result = (HashMap<Integer, ArrayList<String>>)ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
}
