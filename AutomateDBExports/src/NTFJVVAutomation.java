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

import javax.swing.plaf.FileChooserUI;

import com.mysql.jdbc.StringUtils;

public class NTFJVVAutomation {

	static final Map<String, Integer> idMap = new HashMap<String, Integer>();
	static final List<String> formList = new ArrayList<String>();
	
	// static String formName = "ap_form_31341";
//	static String HOME_DIR = "C://ntfjvv/";

	public static void main(String[] args) {
		try {

//			String NTFJVV_DIR = "ntfjvv/";
//			String ntfjvv_directory = HOME_DIR + NTFJVV_DIR;
			File destDir = new File(Constants.HOME_DIR);
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			// formList.add("ap_form_19950");
			// formList.add("ap_form_25001");
			// formList.add("ap_form_24747");
			// formList.add("ap_form_24400");
			// formList.add("ap_form_23591");
			// formList.add("ap_form_18276");
			// formList.add("ap_form_20859");
			// formList.add("ap_form_25488");
			// formList.add("ap_form_27092");
			// formList.add("ap_form_26629");
			// formList.add("ap_form_30441");
			// formList.add("ap_form_29456");
			// formList.add("ap_form_28990");
			// formList.add("ap_form_28393");
			// formList.add("ap_form_27932");
			// formList.add("ap_form_26190");
			// formList.add("ap_form_25835");
			// formList.add("ap_form_15744");
			// formList.add("ap_form_30559");
			formList.add("ap_form_31341");
			formList.add("ap_form_31514");
			formList.add("ap_form_32467");
			// formList.add("ap_form_14132");
			
			loadLatestIds();

			setUp();
			
			// Unzipping the downloaded file from national database
			UnzipUtility.unzip(Constants.ZIPPED_DATA_FILE, Constants.HOME_DIR);

			// Create separate file in staging directory with new data only
			filterDataRecords(Constants.UNZIPPED_DATA_FILE);
			
			cleanUp();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void filterDataRecords(String rawDatafile) throws Exception {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(rawDatafile));
			String line;
			String input = "";
			String lastDataRecord = "";
			String staging_directory = Constants.STAGING_FILES_DIR;
			File destDir = new File(staging_directory);
			if (!destDir.exists()) {
				destDir.mkdir();
			}
			while ((line = file.readLine()) != null) {

				if (line.contains(" SET ")) {
					line = "";
					input += line + '\n';
				}
				
				if (line.contains(formList.get(0))) {
					int recordId = getDataRecordId(line);
					List<String> lines = new ArrayList<>();
					if (recordId > idMap.get(formList.get(0))) {
						lines.add(line);
						writeTextToFile(lines, Constants.STAGING_FILE);
						lastDataRecord = line;
					}

				} else if (line.contains(formList.get(1))) {
					int recordId = getDataRecordId(line);
					List<String> lines = new ArrayList<>();
					if (recordId > idMap.get(formList.get(1))) {
						lines.add(line);
						writeTextToFile(lines, Constants.STAGING_FILE);
						lastDataRecord = line;
					}

				} else if (line.contains(formList.get(2))) {
					int recordId = getDataRecordId(line);
					List<String> lines = new ArrayList<>();
					if (recordId > idMap.get(formList.get(2))) {
						lines.add(line);
						writeTextToFile(lines,  Constants.STAGING_FILE);
						lastDataRecord = line;
					}

				} /*
					 * else if (line.contains(formList.get(3))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(3))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(3) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(4))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(4))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(4) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(5))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(5))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(5) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(6))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(6))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(6) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(7))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(7))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(7) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(8))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(8))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(8) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(9))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(9))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(9) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(10))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(10))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(10) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(11))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(11))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(11) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(12))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(12))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(12) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(13))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(13))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(13) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(14))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(14))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(14) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(15))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(15))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(15) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(16))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(16))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(16) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(17))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(17))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(17) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(18))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(18))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(18) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(19))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(19))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(19) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(20))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(20))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(20) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(21))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(21))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(21) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * } else if (line.contains(formList.get(22))) { int
					 * recordId=getDataRecordId(line); List<String> lines = new ArrayList<>();
					 * if(recordId > idMap.get(formList.get(22))) { lines.add(line);
					 * writeTextToFile(lines, staging_directory + formList.get(22) + ".sql");
					 * lastDataRecord = line; }
					 * 
					 * }
					 */

				line = "";
				input += line + '\n';
			}
			// FileOutputStream File = new FileOutputStream(
			// "C:\\\\Users\\\\Shirmeen\\\\Downloads\\\\ntfjvv_queries\\\\ntfjvv201803081754\\\\ntfjvv201803081754.sql");
			// File.write(input.getBytes());
			// file.close();
			// File.close();
		} finally {
			if (file != null) {
				file.close();
			}
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
				System.out.println("Latest id for " + formName + " is " + latestId);
				idMap.put(formName, latestId);
			}

		} finally {
			if (conn != null) {
				conn.close();
				conn = null;
			}

		}

	}
	
	private static void setUp() throws IOException {
		Files.deleteIfExists(Paths.get(Constants.STAGING_FILE));
	}
	
	private static void cleanUp() throws IOException {
		Files.deleteIfExists(Paths.get(Constants.ZIPPED_DATA_FILE));
		Files.deleteIfExists(Paths.get(Constants.UNZIPPED_DATA_FILE));
		
	}

}
