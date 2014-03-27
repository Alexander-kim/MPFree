package com.example.mp3diddly.datastorage;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
//import android.content.*;

public class DataStorage 
{	
	private static DataStorage mInstance;
	private final String PREFSNAME = "MP3Thing";
	private SharedPreferences mPreferences;
	private Context mContext;
	private SharedPreferences.Editor mEditor;
	
	
	
	/*
	 * 
	 * 
	 */
	private DataStorage(Context pContext)
	{		
		mContext = pContext;
		mPreferences = mContext.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}
	
	
	
	/*
	 * 
	 * 
	 */
	public static DataStorage getInstance(Context pContext)
	{
		if (mInstance == null)
			mInstance = new DataStorage(pContext);
		
		return(mInstance);
	}
	
	
	/*
	 * 
	 * 
	 */
	public void saveStringElement(String pKey, String pValue)
	{
		if (pKey != null && pValue != null)
		{
			mEditor.putString(pKey, pValue);
			mEditor.commit();  		
		}
	}

	/*
	 * 
	 * 
	 */
	public void saveIntElement(String pKey, int pValue)
	{
		if (pKey != null)
		{
			mEditor.putInt(pKey, pValue);
			mEditor.commit();  		
		}
	}	
	
	
	/*
	 * 
	 * 
	 */
	public String getStringElement(String pKey)
	{
		String lRetVal = "";
		
		if (pKey != null)
		{
			lRetVal = mPreferences.getString(pKey, "");			
		}
		
		return(lRetVal);
	}
	
	
	/*
	 * 
	 * 
	 */
	public int getIntElement(String pKey)
	{
		int lRetVal = -1;
		
		if (pKey != null)
		{
			lRetVal = mPreferences.getInt(pKey, -1);			
		}
		
		return(lRetVal);
	}	
	
}
