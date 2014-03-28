package com.example.mp3diddly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;


import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.example.mp3diddly.datastorage.DataStorage;
import com.example.mp3diddly.http.HTTP_Server;
import com.example.mp3diddly.R;


public class Popup_Status 
{
	private PopupWindow mPopupWindow;	        	
	private View mPopupView;
	private DataStorage mStorage;
	private Context mParentActivity;
	private String mDeviceId;
	private List<Map> mListData = new ArrayList<Map>();
	private ListView mLV_Status;
	private SimpleAdapter mDataAdapter;
	private final String URL_STATUS = "/hans/Status.php";

	/*
	 * getBaseContext
	 * 
	 */
	public Popup_Status(Activity pParentActivity)
	{
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
    		mPopupView = layoutInflater.inflate(R.layout.activity_status, null);  
    		mPopupWindow = new PopupWindow(mPopupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);          		
    		
    		mLV_Status = (ListView) mPopupView.findViewById(R.id.LV_Download_Status);    		
    		mDataAdapter = new SimpleAdapter(mParentActivity, (List<? extends Map<String, ?>>) mListData, R.layout.item_status, new String[] { "userIcon", "TV_Song_Descr", "TV_Song_Status" },
    		   										  new int[] { R.id.userIcon, R.id.TV_Song_Descr, R.id.TV_Song_Status });
    		mLV_Status.setAdapter(mDataAdapter);

    		


    		
    		/*
    		 * Cancel button event handler
    		 */
			ImageButton lBT_Cancel = (ImageButton) mPopupView.findViewById(R.id.BT_Status_Cancel);
			lBT_Cancel.setOnClickListener(new Button.OnClickListener()
			{
					@Override
					public void onClick(View v) 
					{
						mPopupWindow.dismiss();
			     	}
			});
              

			/*
			 * Show popup window
			 */
			View lAnchor = ((Activity) mParentActivity).findViewById(R.id.action_status);
			
			mPopupWindow.showAsDropDown(lAnchor, 50, 50);		                     	
//			mPopupWindow.setFocusable(true);
//			mPopupWindow.update();			
	
			
			/*
			 * Start status thraed
			 */
			new Thread(){ public void run()
			{			
				Message lMsg = new Message();
				
				try
				{
					String lServer = mStorage.getStringElement("server");			
					String lOuter = new HTTP_Server(mPopupView.getContext()).sendGETRequest(lServer, URL_STATUS + "?uid=" + mDeviceId);
					
					lMsg.getData().putString("status", lOuter);
				}
				catch (Exception lEx)
				{
				}
				
				mStatusHandler.sendMessage(lMsg);
			}}.start();	
    	}
    	catch (Exception lEx)
    	{

    		Toast.makeText(mParentActivity, "Exception Status.showWindow(): " + lEx.getMessage(), Toast.LENGTH_LONG).show();
    		Log.v("MP3Thing", "Error status activity: " + lEx.getMessage());        		
    	}		
	}	
	



	/*
	 * Status update handler
	 * 
	 */
	private Handler mStatusHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String lNewData = msg.getData().getString("status");
			JSONArray lJSONRecords = null;
			List<Map> list = new ArrayList<Map>();
			
			try 
			{
				JSONObject lJSONObj = new JSONObject(lNewData);
				
				if (lJSONObj.has("records"))
				{
					lJSONRecords = lJSONObj.getJSONArray("records");
					
					for (int i = 0; i < lJSONRecords.length(); i++) 
					{
						try
						{
						    JSONObject lItem = lJSONRecords.getJSONObject(i);
						    
						    if (lItem != null && lItem.has("desc"))
						    {
//						    	String lID = lItem.optString("ytid");
//						    	int lStat = lItem.optInt("stat");
						    	String lStatStr = lItem.optString("statstr");
						    	String lDesc = lItem.optString("desc");
						    	
						    	Map map = new HashMap();
						    	map.put("userIcon", R.drawable.note_small);
						    	map.put("TV_Song_Status", lStatStr);
						    	map.put("TV_Song_Descr", lDesc);						    	
						    	list.add(map);
						    } // if (lIte...
						}
						catch (Exception lEx)
						{							
							// error handling would be something ... !
						}
					} // for (int ...									
				} // if (lJSO...
				
																
				/*
				 * Update GUI 
				 */
				if (list.size() > 0)
				{
					try
					{
			    		mListData.clear();		    		
						mListData.addAll(list);
						mDataAdapter.notifyDataSetChanged();	    		
					}
					catch (Exception lEx)
					{						
					}
				}
			}
			catch (JSONException lEx) 
			{
				Toast.makeText(mParentActivity, "Status exception: " + lEx.getMessage(), Toast.LENGTH_SHORT).show();	
			}			
		}
	};	
}




