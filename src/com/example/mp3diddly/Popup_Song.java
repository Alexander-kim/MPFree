package com.example.mp3diddly;

import android.app.Activity;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3diddly.datastorage.DataStorage;

public class Popup_Song 
{
	private PopupWindow popupWindow;	        	
	private View popupView;
	private DataStorage mStorage;
	private Activity mParentActivity;
	private String mDeviceId;
	
	

	/*
	 * getBaseContext
	 * 
	 */
	public Popup_Song(Activity pParentActivity)
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
    		popupView = layoutInflater.inflate(R.layout.activity_settings, null);  
    		popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		    
    		    		
    		
			/*
			 * Load values
			 */

			
			/*
			 * Show popup window
			 */
			View lAnchor = ((Activity) mParentActivity).findViewById(R.id.action_status);
			popupWindow.showAsDropDown(lAnchor, 50, 50);		                     	
//			popupWindow.setFocusable(true);
//			popupWindow.update();	    	    	        	    		
    		Toast.makeText(mParentActivity, "Popup_Song.ShowWindow(): ...", Toast.LENGTH_LONG).show();
    	}
    	catch (Exception lEx)
    	{
    	}
    }

}
