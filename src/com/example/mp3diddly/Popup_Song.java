package com.example.mp3diddly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3diddly.datastorage.DataStorage;
import com.example.mp3diddly.http.HTTP_Server;

public class Popup_Song 
{
	private PopupWindow mPopupWindow;	        	
	private View mPopupView;
	private DataStorage mStorage;
	private Activity mParentActivity;
	private String mDeviceId;
	private String mSongDescr;
	private String mYTID;
	

	/*
	 * getBaseContext
	 * 
	 */
	public Popup_Song(Activity pParentActivity, String pSongDescr, String pYTID)
	{
		mSongDescr = pSongDescr;
		mYTID = pYTID;
		mParentActivity = pParentActivity;
		mStorage = DataStorage.getInstance(mParentActivity);
		mDeviceId = Secure.getString(mParentActivity.getContentResolver(), Secure.ANDROID_ID); 				
	}

	/*
	 * 
	 * 
	 */
	public void showWindow()
	{
    	try
    	{
    		LayoutInflater layoutInflater = (LayoutInflater) mParentActivity.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);  
    		mPopupView = layoutInflater.inflate(R.layout.activity_song, null);  
    		mPopupWindow = new PopupWindow(mPopupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		    
    		/*
    		 * Cancel button event handler
    		 */
			ImageButton lBT_Cancel = (ImageButton) mPopupView.findViewById(R.id.BT_Song_Cancel);
			lBT_Cancel.setOnClickListener(new Button.OnClickListener()
			{
					@Override
					public void onClick(View v) 
					{
						mPopupWindow.dismiss();
			     	}
			});
			
    		/*
    		 * Save button event handler
    		 */
			ImageButton lBT_Download = (ImageButton) mPopupView.findViewById(R.id.BT_Song_Download);
			lBT_Download.setOnClickListener(new Button.OnClickListener()
			{
					@Override
					public void onClick(View v) 
					{
						/*
						 * Start status thraed
						 */
						new Thread(){ public void run()
						{			
							Message lMsg = new Message();
							
							try
							{
								String lServer = mStorage.getStringElement("server");			
								String lURL = Config.URL_TRANSCODE + "?uid=" + Config.DeviceID + "&vid="+ mYTID;
								String lOuter = new HTTP_Server(mPopupView.getContext()).sendGETRequest(lServer, lURL);

//								lMsg.getData().putString("status", lOuter);
								lMsg.getData().putString("info", "Downloading song: " + mSongDescr);
							}
							catch (Exception lEx)
							{
							}
							
							mDownloadHandler.sendMessage(lMsg);
						}}.start();							
			     	}
			});	   
			
			
			/*
			 * Load values
			 */

			
			/*
			 * Show popup window
			 */
			View lAnchor = ((Activity) mParentActivity).findViewById(R.id.action_status);
			mPopupWindow.showAsDropDown(lAnchor, 50, 50);		                     	
//			mPopupWindow.setFocusable(true);
//			mPopupWindow.update();	    	    	        	    		
//    		Toast.makeText(mParentActivity, "Popup_Song.ShowWindow(): ...", Toast.LENGTH_LONG).show();
    	}
    	catch (Exception lEx)
    	{
    	}
    }
	
	

	/*
	 * Status update handler
	 * 
	 */
	private Handler mDownloadHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String lNewData = msg.getData().getString("info");
			Toast.makeText(mParentActivity, "Msg: " + lNewData, Toast.LENGTH_LONG).show();			
		}
	};		
	

}
