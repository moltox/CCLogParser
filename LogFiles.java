import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFiles {
	private String logfilepath;

	public LogFiles() {
		this.logfilepath = "C:\\Users\\moltox\\.controlgui\\logs";

	}

	public LogFiles(String logfilepath) {
		this.logfilepath = logfilepath;
	}
	
	public String getLogFilePath()  {
		return logfilepath;
	}

	public String[] listFilesFromPath() {
		File file = new File(logfilepath);
		String[] filelist = file.list();
		/*
		for (int i = 0; i < filelist.length; i++) {
			System.out.println(filelist[i]);
		}
		*/
		filelist[filelist.length -1] = null;
		return filelist;
	}

	public String getDateFromLogFile(String filename) {
		String dateString = null;
		if (filename.length() == 7) {
			SimpleDateFormat dateFormatToday = new SimpleDateFormat("yyyy-MM-dd");
			dateString = dateFormatToday.format(new Date());
		} else {
			dateString = filename.substring(8, 18);
			// System.out.println("Filename: " + filename + " Date: " + dateString);

		}
		return dateString;
	}
}
