package com.example.mp3diddly;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.mp3diddly.datastorage.DataStorage;
import com.example.mp3diddly.http.HTTP_Server;

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
		protected Void doInBackground(Context... params) 
		{
			Context lContext =  params[0];
			try
			{
				if (mStorage == null)
					mStorage = DataStorage.getInstance(lContext);
				
				if (mDeviceId == null)
					mDeviceId = Secure.getString(lContext.getContentResolver(), Secure.ANDROID_ID);
				
				String lURI = URL_STATUS + "?uid=" + Config.DeviceID;
				String lServer =  mStorage.getStringElement("server");			
				String lStatusData = new HTTP_Server(lContext).sendGETRequest(lServer, lURI);

							
				
				if (lStatusData != null && !lStatusData.isEmpty())
				{
					JSONArray lJSONRecords = null;
					List<Map> list = new ArrayList<Map>();

					try 
					{
						JSONObject lJSONObj = new JSONObject(lStatusData);
						
						if (lJSONObj.has("records"))
						{							
							lJSONRecords = lJSONObj.getJSONArray("records");
							HTTP_Server lHTTP = new HTTP_Server(lContext);
							
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
											Log.v("mp3diddly", "Downloading: " + lYTID + " / " + lStat);
											String lFileName = lYTID+".mp3";
											String lDownloadString = "http://" + lServer + Config.URL_DOWNLOAD + "?uid=" + Config.DeviceID + "&vid=" + lYTID;
											
											if (lHTTP.downloadFile(lDownloadString, "/sdcard/media/", lFileName))
											{
												Intent intent = new Intent();
												intent.setAction("com.tutorialspoint.NEW_FILE");
												intent.putExtra(Config.TITLE_FILENAME, lFileName);
												intent.putExtra(Config.TITLE_DESCRIPTION, lDesc);
												lContext.sendBroadcast(intent);
											} // if (lHTTP...
								    	} // if (lYTI...
								    } // if (lItem...
								}
								catch (Exception lEx)
								{									
								}
							} // for (int i ...
						} // if (lJSONOb...
					}
					catch (Exception lEx)
					{						
					}
				} // if (lOuter ...
			}
			catch (Exception lEx)
			{
			}				
			
			return null;
		}
	}	

}




