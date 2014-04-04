package com.example.mp3diddly;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mp3diddly.R;
import com.example.mp3diddly.datastorage.DataStorage;
import com.example.mp3diddly.http.HTTP_Server;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Activity_Main extends Activity 
{
	private ImageButton mBT_Search;
	private Button mBT_List;
	private EditText mET_SearchTerm;
	private ListView mLV_Search; 
	private SimpleAdapter mDataAdapter;
	private DataStorage mStorage;
	private List<Map> mListData = new ArrayList<Map>();
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		
		Config.DeviceID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		
		mET_SearchTerm = (EditText) findViewById(R.id.ET_Search_Term);
		mLV_Search = (ListView) findViewById(R.id.LV_Search_Output);		
		mDataAdapter = new SimpleAdapter(this, (List<? extends Map<String, ?>>) mListData, R.layout.item_search, new String[] { "userIcon", "TV_Song_Title", "TV_Song_ID" },
		   										  new int[] { R.id.userIcon, R.id.TV_Song_Title, R.id.TV_Song_ID });
		mLV_Search.setAdapter(mDataAdapter);		

		mStorage = DataStorage.getInstance(this);
	
		

		mLV_Search.setOnItemClickListener(new OnItemClickListener()
		    {
		        @Override
		        public void onItemClick(AdapterView<?> a, View v, int pIndex,long l) 
		        {		        	
		        	Map map = (HashMap) mLV_Search.getItemAtPosition(pIndex);
		        	String lTitle =  map.get("TV_Song_Title").toString();
		        	String lYTID = map.get("TV_Song_ID").toString();
		        	
					Popup_Song lSong = new Popup_Song(Activity_Main.this, lTitle, lYTID);
		        	lSong.showWindow();
		        }
            });
		

		
		
		/*
		 * Search button
		 */
	     mBT_Search = (ImageButton) findViewById(R.id.BT_Song_Search);
	     mBT_Search.setOnClickListener(new View.OnClickListener() 
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
	    				String lPath = "";
	    				HTTP_Server lHTTPClient = null;
	    				String lSearchResult = "";
	    				
	    				try
	    				{
		    				lPath = Config.URL_SEARCH + "?srch="+URLEncoder.encode(mET_SearchTerm.getText().toString());	    			
		    				lHTTPClient  = new HTTP_Server(getBaseContext());
		    				lSearchResult = lHTTPClient.sendGETRequest(mStorage.getStringElement(Config.TITLE_SERVER), lPath);	    					
	    					lMsg.getData().putString("searchresult", lSearchResult);
	    					

		    				mSearchOutputHandler.sendMessage(lMsg);
	    				}
	    				catch (Exception lEx)
	    				{
	    					Toast.makeText(getBaseContext(), "Error: " + lEx.getMessage(), Toast.LENGTH_LONG).show();
	    				}
	    				
	    			}}.start();		            	
	            }
	        });	
	     
	     
	     /*
	      * Start service
	      */
		DataStorage lStorage = DataStorage.getInstance(this);
		int lIntervalIndex = lStorage.getIntElement(Config.TITLE_INTERVAL);	     
	    int lInterval = 1;
	    
	    try
	    {
	    	lInterval = Config.INTERVAL[lIntervalIndex];	    	
	    }
	    catch (Exception lEx)
	    {	    	
	    }
	     
	    Intent notif = new Intent(this, Broadcast_CheckStatus.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, notif, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, 1000, lInterval * 1000, pi);	
		
		Log.v("mp3diddly", "Status check interval: " + lInterval);	
	}



	/*
	 * Status update handler
	 * 
	 */
	private Handler mSearchOutputHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			String lNewData = msg.getData().getString("searchresult");
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

						    if (lItem != null && lItem.has("ytid") && lItem.has("href") && lItem.has("title"))
						    {						    	
						    	String lYTID = lItem.optString("ytid");
//						    	String lHRef = lItem.optString("href");
						    	String lTitle = lItem.optString("title");
						    	
						    	Map map = new HashMap();
						    	map.put("userIcon", R.drawable.note_small);
						    	map.put("TV_Song_Title", lTitle);
						    	map.put("TV_Song_ID", lYTID);
						    	
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
						Toast.makeText(getBaseContext(), "GUI exception: " + lEx.getMessage(), Toast.LENGTH_SHORT).show();							
					}
				}
			}
			catch (JSONException lEx) 
			{
				Toast.makeText(getBaseContext(), "JSONException exception: " + lEx.getMessage(), Toast.LENGTH_SHORT).show();	
			}
			catch (Exception lEx) 
			{
				Toast.makeText(getBaseContext(), "Regular exception: " + lEx.getMessage(), Toast.LENGTH_SHORT).show();	
			}			
		}
	};		
	
	
	
	/*
	 * 
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		return true;
	}

	
	
	
    /*
     * Handle menu bar click events
     * 
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	        case R.id.action_status: 
	        	Popup_Status lStatus = new Popup_Status(this);	
	    		lStatus.showWindow(); 			        		        	
	            return true;
	        case R.id.action_settings:
	    		Popup_Settings lSettings = new Popup_Settings(this);
	    		lSettings.showWindow();     	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	
	
}
