package com.example.mp3diddly;

import android.app.Activity;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.PopupWindow;
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
    		Toast.makeText(mParentActivity, "Popup_Song.ShowWindow(): ...", Toast.LENGTH_LONG).show();
    	}
    	catch (Exception lEx)
    	{
    	}
    }

}
