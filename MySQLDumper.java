import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

public class MySQLDumper {

	private static String ip = "localhost";
	private static String port = "3306";
	private static String database = "ntfjvv";
	private static String stagingDatabase = "ntfjvv-staging";
	private static String user = "root";
	private static String pass = "admin@DJ2018";
	private static String outputDirectory = "C:\\Users\\Shirmeen\\Downloads\\ntfjvv_queries\\data_from_staging_db\\";

	public static void export(String dumpCommand, String outputFilePath) {
		System.out.println("dumpCommand:"+dumpCommand);
		Runtime rt = Runtime.getRuntime();
		File test = new File(outputFilePath);
		PrintStream ps;

		try {
			Process child = rt.exec(dumpCommand);
			ps = new PrintStream(test);
			InputStream in = child.getInputStream();
			int ch;
			while ((ch = in.read()) != -1) {
				ps.write(ch);
				System.out.write(ch); // to view it by console
			}

			InputStream err = child.getErrorStream();
			while ((ch = err.read()) != -1) {
				System.out.write(ch);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static void importDataInStagingDB(String dataFile) {
		System.out.println("********* importDataInStagingDB********* ");
		String path = outputDirectory + dataFile+ ".sql";
		File destDir = new File(outputDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
		String dumpCommand = "mysqldump" + " -u " + user + " -p" + pass + " "+ stagingDatabase+ "\" < "+path+"\"";
		System.out.println("dumpCommand:"+dumpCommand);
		export(dumpCommand, path);
	}

	public static void exportDataFromStagingTable(String tableName, int id) {
		System.out.println("********* exportDataFromStagingTable********* ");
		String path = outputDirectory + tableName+ ".sql";
		File destDir = new File(outputDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
		id=id-3;
		String dumpCommand = "mysqldump -t " + " -u " + user + " -p" + pass + " "+ stagingDatabase+ " --compact --tables "+tableName+ " --where=\"id > " +id+"\"";
		export(dumpCommand, path);

	}
	

	
	public static void importDataInProdDB(String dataFile) {
		System.out.println("********* importDataInProdDB ************");
		String path = outputDirectory + dataFile+ ".sql";
		File destDir = new File(outputDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
		String dumpCommand = "mysqldump " + " -u " + user + " -p" + pass + " "+ database + " \" < "+path+"\"";
		System.out.println("dumpCommand:"+dumpCommand);
		export(dumpCommand, path);
	}
	
}