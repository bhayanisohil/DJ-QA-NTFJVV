import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;

public class Automate {
	static int lport;
	static String rhost;
	static int rport;
	static Map<String, Integer> idMap = new HashMap<String, Integer>();
	static List<String> formList = new ArrayList<String>();

	static String formName = "ap_form_31341";
	static final String HOME_DIR = "C://Users/Shirmeen/Downloads/ntfjvv_queries/";

	public static void main(String[] args) {
		try {
//			formList.add("ap_form_19950");
//			formList.add("ap_form_25001");
//			formList.add("ap_form_24747");
//			formList.add("ap_form_24400");
//			formList.add("ap_form_23591");
//			formList.add("ap_form_18276");
//			formList.add("ap_form_20859");
//			formList.add("ap_form_25488");
//			formList.add("ap_form_27092");
//			formList.add("ap_form_26629");
//			formList.add("ap_form_30441");
//			formList.add("ap_form_29456");
//			formList.add("ap_form_28990");
//			formList.add("ap_form_28393");
//			formList.add("ap_form_27932");
//			formList.add("ap_form_26190");
//			formList.add("ap_form_25835");
//			formList.add("ap_form_15744");
//			formList.add("ap_form_30559");
			formList.add("ap_form_31341");
			formList.add("ap_form_31514");
			formList.add("ap_form_32467");
//			formList.add("ap_form_14132");
			
			loadLatestIds();
			 String zippedFile = HOME_DIR + "ntfjvv201803081754.zip";
			 UnzipUtility.unzip(zippedFile, HOME_DIR + "ntfjvv_unzipped");
			createSeparateDumpFiles(HOME_DIR + "ntfjvv_unzipped/ntfjvv201803081754.sql");
			int idValue = idMap.get(formName);
			System.out.println("idValue:" + idValue);
			MySQLDumper.exportDataFromStagingTable(formName, idValue);
			MySQLDumper.importDataInProdDB(formName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void createSeparateDumpFiles(String rawDatafile) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(rawDatafile));
			String line;
			String input = "";
			String lastDataRecord = "";
			String staging_directory = HOME_DIR + "staging-files/";
			File destDir = new File(staging_directory);
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			while ((line = file.readLine()) != null) {
				if (line.contains(formList.get(1)) || line.contains(formList.get(2)) || line.contains(formList.get(3))
						|| line.contains(formList.get(4)) || line.contains(formList.get(5))
						|| line.contains(formList.get(6)) || line.contains(formList.get(7))
						|| line.contains(formList.get(8)) || line.contains(formList.get(9))
						|| line.contains(formList.get(10)) || line.contains(formList.get(11))
						|| line.contains(formList.get(12)) || line.contains(formList.get(13))
						|| line.contains(formList.get(14)) || line.contains(formList.get(15))
						|| line.contains(formList.get(16)) || line.contains(formList.get(17))
						|| line.contains(formList.get(18)) || line.contains(formList.get(19))
						|| line.contains(formList.get(20)) || line.contains(formList.get(21))
						|| line.contains(formList.get(22)) || line.contains(formList.get(23))) {
					List<String> lines = new ArrayList<>();
					lines.add(line);
					System.out.println(line);
					writeTextToFile(lines, staging_directory + "staging_file" + ".sql");
					lastDataRecord = line;
				}
				line = "";
				if (!StringUtils.isNullOrEmpty(lastDataRecord)) {
					idMap.put(formName, getDataRecordId(lastDataRecord));
				}
				input += line + '\n';
			}
			// FileOutputStream File = new FileOutputStream(
			// "C:\\\\Users\\\\Shirmeen\\\\Downloads\\\\ntfjvv_queries\\\\ntfjvv201803081754\\\\ntfjvv201803081754.sql");
			// File.write(input.getBytes());
			// file.close();
			// File.close();
		} catch (Exception e) {
			System.out.println("Problem reading file.");
			e.printStackTrace();
		}
	}

	private static void writeTextToFile(List<String> aLines, String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		Files.write(path, aLines, StandardOpenOption.APPEND);
	}

	private static int getDataRecordId(String line) {
		return Integer.parseInt(line.substring(line.indexOf('(') + 1, line.indexOf(',')));

	}

	private static void loadLatestIds() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		try {
			for (String formName : formList) {
				conn = DatabaseQueries.getDBConnection();
				int latestId = DatabaseQueries.getLatestIdFromTable(conn, formName);
				System.out.println("Latest id for "+formName +" is "+ latestId);
				idMap.put(formName, latestId);
			}

		} finally {
			if (conn != null) {
				conn.close();
				conn = null;
			}

		}

	}

}
