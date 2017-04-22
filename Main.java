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

public class Main {
	private static String currentWindowsUser = System.getProperty("user.name"); 
	private static String date = "2017-04-22";
	/*
	 * args[0] -> Username
	 * 
	 */
	
	public static void main(String[] args)  {
		System.out.println("Aktueller Windows User: " + currentWindowsUser) ;
		
		CCLogParser ccLogParser = new CCLogParser(args);
	
	}
}
