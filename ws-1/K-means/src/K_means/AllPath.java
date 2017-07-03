package K_means;

public class AllPath {
	//用来，大的记录
	public static String outputPath_Centroid = "/home/Lee/data/output-FR/Sample/centroid/centroid";
	//实验过程中的记录
	public static String outputPath_Type = "/home/Lee/data/output-FR/Sample/type";
	public static double percent = 0.01;
	
	public static double alldoc_Start = 0.0/3;
	public static double alldoc_end = 3.0/3;
	
	public static int K_num = 100;//166,167,167
	public static int K_Start = 0;
	public static int K_End = K_num + K_Start;
	public static String inputPath_Centroid = "/home/Lee/data/output-FR/K-means/abc111-final";
	
	//当某个质心下面的点的个数为这个数目的时候，就flush出来，并clear掉
	public static int flush_num = 300;
	//与上面一个配套的，是否是最后一步骤，将所有的点归类，如果是的话，才需要上面的flush的参数
	public static boolean IsLastStep_flush = true;
	
	/*//用来，大的记录
	public static String outputPath_Centroid = "/home/Lee/data/output-FR/K-means/centroid/centroid";
	//实验过程中的记录
	public static String outputPath_Type = "//home/Lee/data/output-FR/K-means/type";
	public static double percent = 1;
	
	public static double alldoc_Start = 0.0/3;
	public static double alldoc_end = 1.0;
	
	public static int K_num = 100;//166,167,167
	public static int K_Start = 0;
	public static int K_End = K_num + K_Start;
	public static String inputPath_Centroid = "/home/Lee/data/FR结果/all-2";
	
	//当某个质心下面的点的个数为这个数目的时候，就flush出来，并clear掉
	public static int flush_num = 300;
	//与上面一个配套的，是否是最后一步骤，将所有的点归类，如果是的话，才需要上面的flush的参数
	public static boolean IsLastStep_flush = false;*/
}
