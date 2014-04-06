package com.example.mpfree;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.mpfree.datastorage.DataStorage;
import com.example.mpfree.http.HTTP_Server;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;




public class Broadcast_CheckStatus extends BroadcastReceiver
{
	private final String URL_STATUS = "/mp3diddly/Status.php";
	private DataStorage mStorage;
	private String mDeviceId;
	
	/*
	 * 
	 */
	@Override
	public void onReceive(Context pContext, Intent pIntent)
	{	
		new TestTask().execute(pContext);		
	}

	
	
	public class TestTask extends AsyncTask<Context, Void, Void>
	{

		@Override
		protected Void doInBackground(Context... lParams) 
		{			
			Log.v("mp3diddly", "checking for new downloads ...");			
			String lStatusData = "";
			String lServer = "";
			String lURI = "";
			int lLocationIndex = -1;
			String lOutputDir = "";
			JSONObject lJSONObj = null;
			HTTP_Server lHTTP = null;
			
			
			Context lContext =  lParams[0];
			
			try
			{
				if (mStorage == null)
					mStorage = DataStorage.getInstance(lContext);
				
				if (mDeviceId == null)
					mDeviceId = Secure.getString(lContext.getContentResolver(), Secure.ANDROID_ID);
				

				lLocationIndex =  mStorage.getIntElement(Config.TITLE_LOCATION);	
				lOutputDir = Config.LOCATION[lLocationIndex];
				lURI = URL_STATUS + "?uid=" + Config.DeviceID;
				lServer =  mStorage.getStringElement(Config.TITLE_SERVER);			
				lStatusData = new HTTP_Server(lContext).sendGETRequest(lServer, lURI);
			}
			catch (Exception lEx)
			{}
				
			
			if (lStatusData != null && !lStatusData.isEmpty())
			{
				JSONArray lJSONRecords = null;
				List<Map> list = new ArrayList<Map>();
										

				try 
				{
					lJSONObj = new JSONObject(lStatusData);
				}
				catch (Exception lEx)
				{}
				
				
				if (lJSONObj != null && lJSONObj.has("records"))
				{		
					try
					{
						lJSONRecords = lJSONObj.getJSONArray("records");
					}
					catch (Exception lEx) {}
					
					lHTTP = new HTTP_Server(lContext);
						
					if (lJSONRecords != null && lJSONRecords.length() > 0)
					{
						for (int i = 0; i < lJSONRecords.length(); i++) 
						{							
							try
							{
							    JSONObject lItem = lJSONRecords.getJSONObject(i);
							    if (lItem != null && lItem.has("ytid") && lItem.has("stat"))
							    {								    	
							    	String lYTID = lItem.optString("ytid");
							    	String lStat = lItem.optString("stat");	
							    	String lDesc = lItem.optString("desc");	
									    	
							    	if (lYTID != null && !lYTID.isEmpty() && lStat != null && !lStat.isEmpty())
							    	{
										String lDownloadString = "http://" + lServer + Config.URL_DOWNLOAD + "?uid=" + Config.DeviceID + "&vid=" + lYTID;
										Log.v("mp3diddly", "Requests status: " + lStat + "/" + Config.TRANSCODING_STATUS.get(lStat));
												
										// File is ready to download
										if (lStat.equals("2"))
										{
											Log.v("mp3diddly", "Downloading: " + lYTID + " / " + lStat);
											
											// Build file name
											String lFileName = lDesc.replaceAll("[^\\w\\d\\.\\-_]", "_");
											lFileName += ".mp3";
													
											if (lHTTP.downloadFile(lDownloadString, lOutputDir /* "/sdcard/media/" */, lFileName))
											{
												Intent intent = new Intent();
												intent.setAction("com.tutorialspoint.NEW_FILE");
												intent.putExtra(Config.TITLE_FILENAME, lFileName);
												intent.putExtra(Config.TITLE_DESCRIPTION, lDesc);
												intent.putExtra(Config.TITLE_ID, lYTID);
												lContext.sendBroadcast(intent);
											} // if (lHTTP...
										} // if (lSt...
							    	} // if (lYTI...
							    } // if (lItem...
							}
							catch (Exception lEx)
							{
								Log.v("mp3diddly", "Exception: " + lEx.getMessage());
							}
						} // for (int i ...
					} // if (lJSONR...
				} // if (lJSONOb...
			} // if (lOuter ...
				
			
			return null;
		}
	}	

}




