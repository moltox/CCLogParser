import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CCLogParser {
	private List<List<Long>> times = new ArrayList<List<Long>>();
	private String filename;
	private String loginname;
	private String dateStr;

	public CCLogParser() {
		/*
		 * filename =
		 * "C:\\Users\\" + System.getProperty("user.name") + "\\.controlgui\\
		 * logs\\app.log"; SimpleDateFormat dateFormatToday = new
		 * SimpleDateFormat("yyyy-MM-dd"); this.dateStr =
		 * dateFormatToday.format(new Date());
		 */
		LogFiles logFilesO = new LogFiles();
		String[] logFiles = logFilesO.listFilesFromPath();
		String logfilepath = logFilesO.getLogFilePath();
		for (int i = 0; i < logFiles.length; i++) {
			if (logFiles[i] != null) {
				dateStr = logFilesO.getDateFromLogFile(logFiles[i]);
				String fullfilename = logfilepath + "\\" + logFiles[i];
				parseLogFile(fullfilename, dateStr);
			}
		}

	}

	public CCLogParser(String date) {
		filename = "C:\\Users\\" + System.getProperty("user.name") + "\\.controlgui\\logs\\app.log" + "_" + date;
		this.dateStr = date;
		parseLogFile(filename, this.dateStr);
	}

	public void parseLogFile(String filename, String dateStr) {

		// System.out.println(filename);
		// int nameArg = filename.equals(args[0]) ? 1 : 0;

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			List<Long> temp = null;
			// SimpleDateFormat dateFormatToday = new
			// SimpleDateFormat("yyyy-MM-dd");
			// String dateStr = dateFormatToday.format(new Date());
			while ((line = br.readLine()) != null) {

				if (line.contains(dateStr)) {

					if (line.contains("Login")) {
						String[] splitline = line.split(" ");
						loginname = splitline[7];
						// System.out.println("Login Name: " + loginname);
					}
					if (line.contains("Login " + loginname) || line.contains("ERROR ping")) {
						temp = new ArrayList<Long>();
						times.add(temp);
					}
					if (line.contains("closeApplicationNoMessage: Programm wurde beendet")) {
						temp = null;
					}
					if (line.contains("Ping ResponseCode: 200") && temp != null) {
						// System.out.println(line);
						String[] cuts = line.split(" ");
						String[] cuts2 = cuts[1].split(",");

						SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
						Date parsedDate = null;
						Date parsedMinTime = null;
						try {
							parsedDate = dateFormat.parse(cuts2[0]);
							parsedMinTime = dateFormat.parse("08:00:00");

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (parsedDate.getTime() > parsedMinTime.getTime()) {
							Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
							temp.add(timestamp.getTime());
						}
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calcTime(dateStr, times);
	}
	
	private static String millisToHumanTime(long time)  {
		String humanTime = null;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		humanTime = formatter.format(time);
		// humanTime = String.valueOf((time / 1000) / 60);
		return humanTime;
	}

	public static void calcTime(String datestr, List<List<Long>> times) {
		String[] datum = datestr.split("-");
		String datestrDE = datum[2] + '.' + datum[1] + '.' + datum[0];

		long t = 0L;
		int iter = 0;
		for (List<Long> list : times) {
			Collections.reverse(list);
			for (Long timestamp : list) {
				if (!(list.indexOf(timestamp) == (list.size() - 1))) {
					t += (timestamp - list.get(++iter));
				}
			}
			iter = 0;
		}
		System.out.println(datestr + ":\r\n" + millisToHumanTime(t) + " Stunden geloggt = "
				+ (float) (((t / 1000) / 60) / 45.0) + " UE's");

	}
}
