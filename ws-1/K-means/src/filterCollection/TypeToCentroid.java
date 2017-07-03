package filterCollection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Structure.Centroid;
import Structure.Create_Doc2;
import Structure.SingleDoc;

public class TypeToCentroid {

	public static Create_Doc2 create_Doc2 = null;
	public static void main(String[] args) throws Exception {
		create_Doc2 = new Create_Doc2();
		ArrayList<SingleDoc> aldoc = create_Doc2.init();
		int count = 0;
		
		File file = new File("/home/Lee/data/FR结果/all-type");
		File[] files = file.listFiles();
		int i = 0;
		for (File file2:files){
			ArrayList<Integer > al = new ArrayList<>();
			BufferedReader br = new BufferedReader(new FileReader(file2));
			String line = "";
			while ((line = br.readLine())!=null){
				al.add(Integer.parseInt(line));
			}
			Centroid centroid = new Centroid(al);
			if (centroid.getDocid() == -1000){
				continue;
			}
			FileOutputStream fos = new FileOutputStream("/home/Lee/data/FR结果/centroid-all/"+(count++));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(centroid);
			oos.close();
			
			System.out.println(file2.getName());
		}
		System.err.println("END");
	}

}
