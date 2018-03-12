import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MySQLDumper {

	private static String ip = "localhost";
	private static String port = "3306";
	private static String database = "ntfjvv";
	private static String stagingDatabase = "ntfjvv-staging";
	private static String user = "root";
	private static String pass = "admin@DJ2018";
	private static String outputDirectory = "C:\\Users\\Shirmeen\\Downloads\\ntfjvv_queries\\";
	
	public static void runMySQLDump(String dumpCommand, String outputFilePath) {
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
//
//	public static void importDataInStagingDB(String dataFile) {
//		for (String formName : formList) {
//			System.out.println("********* Loading local database for table "+formName);
//			String path = outputDirectory + formName+ ".sql";
//		}
//		String dumpCommand = "mysqldump" + " -u " + user + " -p" + pass + " "+ stagingDatabase+ "\" < "+path+"\"";
//		System.out.println("dumpCommand:"+dumpCommand);
//		export(dumpCommand, path);
//	}

	public static void exportDataFromStagingTable(String tableName, int id) {
		System.out.println("********* exportDataFromStagingTable********* ");
		String path = outputDirectory + tableName+ ".sql";
		File destDir = new File(outputDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
		id=id-3;
		String dumpCommand = "mysqldump -t " + " -u " + user + " -p" + pass + " "+ database+ " --compact --tables "+tableName+ " --where=\"id > " +id+"\"";
		runMySQLDump(dumpCommand, path);

	}
	

	
	public static void loadDatabaseTable(List<String> formList) {
		for (String formName : formList) {
			System.out.println("********* Loading local database for table "+formName);
			String path = Constants.STAGING_FILES_DIR + formName+ ".sql";
			String dumpCommand = "mysql" + " -u " + user + " -p" + pass +  database + " < " +path;
			System.out.println("dumpCommand:"+dumpCommand);
			runMySQLDump(dumpCommand, path);
		}
	}
	
//	public static void importDataInStagingDB(String dataFile) throws IOException {
//		System.out.println("********* importDataInStagingDB********* ");
//		String path = outputDirectory + dataFile+ ".sql";
//		path ="C:\\Users\\Shirmeen\\Downloads\\ntfjvv_queries\\ap_form_31341.sql";
//		File destDir = new File(outputDirectory);
//        if (!destDir.exists()) {
//            destDir.mkdir();
//        }
//        File f = new File(path);
//        if(f.exists() && !f.isDirectory()) { 
//            System.out.println(path +" file exists");
//        }
//		String dumpCommand = "mysql " + " -u " + user + " -p" + pass + " "+ database+ " \"< sqldump.sql";
////		Runtime rt = Runtime.getRuntime();
////		Process pr = rt.exec("mysql -padmin@DJ2018 -h localhost ntfjvv < app_form_31341.sql");
//		System.out.println("dumpCommand:"+dumpCommand);
//		runMySQLDump(dumpCommand, path);
//	}
//	
}