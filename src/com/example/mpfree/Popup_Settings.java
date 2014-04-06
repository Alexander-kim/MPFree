package com.example.mpfree;

import com.example.mp3diddly.R;
import com.example.mpfree.datastorage.DataStorage;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Gravity;
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




public class Popup_Settings 
{
	private PopupWindow popupWindow;	        	
	private View popupView;
	private DataStorage mStorage;
	private Activity mParentActivity;
	
	
	
	/*
	 * 
	 * 
	 */
	public Popup_Settings(Activity pParentActivity)
	{	
		mParentActivity = pParentActivity;
		mStorage = DataStorage.getInstance(mParentActivity);		
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
    		popupView = layoutInflater.inflate(R.layout.activity_settings, null);  
    		popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		    
    	    	        	    
    		/*
    		 * Cancel button event handler
    		 */
			ImageButton lBT_Cancel = (ImageButton) popupView.findViewById(R.id.BT_Cancel);
			lBT_Cancel.setOnClickListener(new Button.OnClickListener()
			{
					@Override
					public void onClick(View v) 
					{
				    	popupWindow.dismiss();
			     	}
			});
              
    		/*
    		 * Save button event handler
    		 */
			ImageButton lBT_Save = (ImageButton) popupView.findViewById(R.id.BT_Save);
			lBT_Save.setOnClickListener(new Button.OnClickListener()
			{
			
				
					@Override
					public void onClick(View v) 
					{
						int lLocation = -1; 
						int lInterval = -1; 
						String lServer = "";
						
						// 1. Read all config params
						Spinner lS_Location = (Spinner) popupView.findViewById(R.id.SpinnerLocation);
						Spinner lS_Interval = (Spinner) popupView.findViewById(R.id.SpinnerInterval);
						EditText lET_Server = (EditText) popupView.findViewById(R.id.ET_Server);
						
						try
						{
							lLocation = lS_Location.getSelectedItemPosition();
							lInterval = lS_Interval.getSelectedItemPosition();
							lServer = lET_Server.getText().toString();
							if (lServer == null || lServer.isEmpty())
								lServer = Config.DEFAULT_SERVER;
						}
						catch (Exception lEx)
						{
							Toast.makeText(mParentActivity, "Exception(0): " + lEx.getMessage(), Toast.LENGTH_LONG).show();
				    		Log.v("MP3Diddly", "Exception(0): " + lEx.getMessage());   
						}
						
						// 2. Save config params
						mStorage.saveIntElement(Config.TITLE_LOCATION, lLocation);
						mStorage.saveIntElement(Config.TITLE_INTERVAL, lInterval);
						mStorage.saveStringElement(Config.TITLE_SERVER, lServer);
												
						// 3. Close window
						popupWindow.dismiss();
			     	}
			});	

			/*
			 * Load values
			 */
			
			// 1. Read all config params
			Spinner lLocation = (Spinner) popupView.findViewById(R.id.SpinnerLocation);
			Spinner lInterval = (Spinner) popupView.findViewById(R.id.SpinnerInterval);
			EditText lServer = (EditText) popupView.findViewById(R.id.ET_Server);
			TextView lDeviceID = (TextView) popupView.findViewById(R.id.TV_DeviceIDValue);
			
			lServer.setText(mStorage.getStringElement("server"));		
			lInterval.setSelection(mStorage.getIntElement("interval"));	
			lLocation.setSelection(mStorage.getIntElement("location"));
			lDeviceID.setText(Config.DeviceID);

			
			/*
			 * Show popup window
			 */
			View lAnchor = ((Activity) mParentActivity).findViewById(R.id.action_status);
//			popupWindow.showAsDropDown(lAnchor, 50, 50);		       
			popupWindow.showAtLocation(lAnchor, Gravity.CENTER, 0, 0);			
			
			
			popupWindow.setFocusable(true);
			popupWindow.update();			
    	}
    	catch (Exception lEx)
    	{
    		Toast.makeText(mParentActivity, "Exception(1): " + lEx.getMessage(), Toast.LENGTH_LONG).show();
    		Log.v("MP3Diddly", "Exception(1): " + lEx.getMessage());        		
    	}		
	}
	
}
