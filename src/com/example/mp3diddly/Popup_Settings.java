package com.example.mp3diddly;

import com.example.mp3diddly.datastorage.DataStorage;
import com.example.mp3diddly.R;

import android.content.Context;
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




public class Popup_Settings 
{
	private PopupWindow popupWindow;	        	
	private View popupView;
	private DataStorage mStorage;
	private Context mContext;
	private View mParentView;
	private String mDeviceId;
	
	/*
	 * 
	 * 
	 */
	public Popup_Settings(Context pContext, View pParentView)
	{	
		mContext = pContext;
		mParentView = pParentView;
		mStorage = DataStorage.getInstance(mContext);
		mDeviceId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
	}
	
	
	
	/*
	 * 
	 * 
	 */
	public void showWindow()
	{
    	try
    	{       
    		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);  
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
						}
						catch (Exception lEx)
						{
//							Toast.makeText(getApplicationContext(), "Error: " + lEx.getMessage(), Toast.LENGTH_LONG).show();
						}
						
						// 2. Save config params
						mStorage.saveIntElement("location", lLocation);
						mStorage.saveIntElement("interval", lInterval);
						mStorage.saveStringElement("server", lServer);
												
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
			lDeviceID.setText(mDeviceId);

			
			/*
			 * Show popup window
			 */
			View lAnchor = mParentView.findViewById(R.id.action_settings);
			
			popupWindow.showAsDropDown(lAnchor, 50, 50);		                     	
			popupWindow.setFocusable(true);
			popupWindow.update();			
    	}
    	catch (Exception lEx)
    	{
    		Log.v("MP3Thing", "Error status activity: " + lEx.getMessage());        		
    	}		
	}
	
}
