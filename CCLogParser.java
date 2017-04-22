import java.io.BufferedReader;
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
	private static List<List<Long>> times = new ArrayList<List<Long>>();

	public CCLogParser(String[] args) {

		String loginname = "";

		String filename = "C:\\Users\\" + System.getProperty("user.name") + "\\.controlgui\\logs\\app.log";

		if (args.length > 0) {
			filename = filename + "_" + args[0];
		}

		System.out.println(filename);
		// int nameArg = filename.equals(args[0]) ? 1 : 0;

		SimpleDateFormat dateFormatToday = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormatToday.format(new Date());

		if (args.length == 1) {
			dateStr = args[0];
		}

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
						System.out.println("Login Name: " + loginname);
					}
					if (line.contains("Login " + "mmueller5") || line.contains("ERROR ping")) {
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
						try {
							parsedDate = dateFormat.parse(cuts2[0]);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
						temp.add(timestamp.getTime());
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
		calcTime(dateStr);
	}

	public static void calcTime(String datestr) {
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
		System.out.println(datestr + ":\r\n" + ((t / 1000) / 60) + " Minuten geloggt = "
				+ (float) (((t / 1000) / 60) / 45.0) + " UE's");

	}
}
