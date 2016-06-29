package com.epsilon.Leak.Hawk.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class LeakHawkUtils {
	
	public static Properties properties = new Properties();
	
	private static SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm:ss");

	// Reading the configuration file
	public static void readConfigFile() {
		
		InputStream input = null;
		try {
			System.out.println("************* Reading the configuration file ***************");
			input = new FileInputStream("config.properties");

			// load a properties file
			properties.load(input);
			
			//allowedSyntaxList = Arrays.asList( prop.getProperty("allowed.syntax.list").split(","));
			
			// get the property value and print it out
			//System.out.println( "Key Word List : " + keyWordList );
			//System.out.println( "Allowed Syntax List : " + allowedSyntaxList );	

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// convert InputStream to String
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	public static void printDateTime(){
		
	     Date dNow = new Date( );	      
	     System.out.println("Current Time: " + ft.format(dNow));
	}
	

}
