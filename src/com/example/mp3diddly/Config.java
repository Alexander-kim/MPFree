package com.example.mp3diddly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Config 
{
	public static String DeviceID;
	
	public final static String APP_NAME = "MPFree";
	public final static String APP_VERSION = "0.1";
	public final static String DEFAULT_SERVER = "mp3.buglist.io";
	
	public final static String URL_STATUS = "/mp3diddly/Status.php";
	public final static String URL_SEARCH = "/mp3diddly/YTSearch.php";
	public final static String URL_DOWNLOAD = "/mp3diddly/Download.php";
	public final static String URL_TRANSCODE = "/mp3diddly/Transcoder.php";
	public final static String URL_WATCH = "/mp3diddly/v.php";

	public final static String TITLE_FILENAME = "filename";	
	public final static String TITLE_DESCRIPTION = "description";	
	public final static String TITLE_ID = "id";
	public final static String TITLE_INTERVAL = "interval";
	public final static String TITLE_LOCATION = "location";
	public final static String TITLE_SERVER = "server";
	public final static String TITLE_STATUS = "status";


	public final static int INTERVAL[] = new int[] { 10, 30, 60, 300, 600, 1800, 3600 };
	public final static String LOCATION[] = new String[] { "/sdcard/media/", "/sdcard/DCIM/", "/sdcard/Music/", "/sdcard/Documents/", "/sdcard/Download/", "/sdcard/" };

	
	public final static Map<String, String> TRANSCODING_STATUS = new HashMap<String, String>() {{
	    put("0", "Pending");
	    put("1", "Transcoding");
	    put("2", "Ready");
	}};
	
}
