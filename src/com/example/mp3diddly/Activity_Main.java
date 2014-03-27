package com.example.mp3diddly;



//import com.example.w3o1.MyCursorAdapter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mp3diddly.R;

import android.content.ContentValues;
import android.content.Context;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Activity_Main extends Activity 
{
	private ImageButton mBT_Search;
	private Button mBT_List;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
	     mBT_Search = (ImageButton) findViewById(R.id.BT_Song_Search);
	     mBT_Search.setOnClickListener(new View.OnClickListener() 
	     {

	            @Override
	            public void onClick(View v) 
	            {
	            	Toast.makeText(getApplicationContext(), "search ...", Toast.LENGTH_SHORT).show();
	            }
	        });		
	     
	     
	 
	}

	
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
