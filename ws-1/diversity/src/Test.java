import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;


public class Test {

	public static void main(String asg[]) throws Exception{
		HashMap<Integer, HashMap<Integer, Double>> hm = null;
		FileInputStream fis = new FileInputStream(new File("E:\\实验数据\\result/155"));
		ObjectInputStream ois = new ObjectInputStream(fis);
		hm = (HashMap<Integer, HashMap<Integer, Double>>) ois.readObject();
	}
}
