package com.example.mp3diddly;

import java.net.URLEncoder;

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


public class Alarm_CheckStatus extends BroadcastReceiver
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
				String lOuter = new HTTP_Server(lContext).sendGETRequest(lServer, lURI);

							
Log.v("mp3diddly", "HONK: " + lOuter);				
//				Toast.makeText(pContext, "STATUS(" + lServer + lURI + "): "+ lOuter, Toast.LENGTH_LONG).show();			
//				Toast.makeText(lContext, "STATUS(www.google.com): ", Toast.LENGTH_LONG).show();			
//				lMsg.getData().putString("status", lOuter);
			}
			catch (Exception lEx)
			{
			}				
			
			return null;
		}
	}	

}




