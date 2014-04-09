package com.example.mpfree;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mpfree.R;
import com.example.mpfree.datastorage.DataStorage;
import com.example.mpfree.http.HTTP_Server;

public class Popup_Song 
{
	private PopupWindow mPopupWindow;	        	
	private View mPopupView;
	private DataStorage mStorage;
	private Activity mParentActivity;
	private String mDeviceId;
	private String mSongDescr;
	private String mYTID;
	private WebView mYoutubeWebView;
	private TextView mVideoTitle;
	private ImageButton mBT_Download;
	private ImageButton mBT_Cancel;
	
	/*
	 * 
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
			mBT_Cancel = (ImageButton) mPopupView.findViewById(R.id.BT_Song_Cancel);
			mBT_Cancel.setOnClickListener(new Button.OnClickListener()
			{
					@Override
					public void onClick(View v) 
					{
						try
						{
							mYoutubeWebView.stopLoading();
						} catch (Exception lEx) {}
						
						try 
						{
							Class.forName("android.webkit.WebView")
							.getMethod("onPause", (Class[]) null)
							.invoke(mYoutubeWebView, (Object[]) null);
						}
						catch (Exception lEx) 
						{
						}						
						
						
						mPopupWindow.dismiss();
			     	}
			});
			
    		/*
    		 * Save button event handler
    		 */
			mBT_Download = (ImageButton) mPopupView.findViewById(R.id.BT_Song_Download);
			mBT_Download.setOnClickListener(new Button.OnClickListener()
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
								String lServer = mStorage.getStringElement(Config.TITLE_SERVER);			
								String lURL = Config.URL_TRANSCODE + "?uid=" + Config.DeviceID + "&vid="+ mYTID;
								String lOuter = new HTTP_Server(mPopupView.getContext()).sendGETRequest(lServer, lURL);

								lMsg.getData().putString("info", "Downloading song: " + mSongDescr);
								;
							}
							catch (Exception lEx)
							{
							}
							
							mDownloadHandler.sendMessage(lMsg);
						}}.start();							
			     	}
			});	   
			
			
			
			/*
			 * Video title
			 */
			mVideoTitle = (TextView) mPopupView.findViewById(R.id.TV_Song_TitleValue);
			mVideoTitle.setText(mSongDescr);

			
			/*
			 * Web view
			 */
			String lServer = mStorage.getStringElement(Config.TITLE_SERVER);
			String lURL = "http://" + lServer + Config.URL_WATCH + "?vid=" + mYTID;
			mYoutubeWebView = (WebView) mPopupView.findViewById(R.id.WV_Video);
			mYoutubeWebView.getSettings().setJavaScriptEnabled(true);
			mYoutubeWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
			mYoutubeWebView.getSettings().setSupportMultipleWindows(false);
	        mYoutubeWebView.getSettings().setSupportZoom(false);
	        mYoutubeWebView.setVerticalScrollBarEnabled(false);
	        mYoutubeWebView.setHorizontalScrollBarEnabled(false);			
			
			mYoutubeWebView.setWebChromeClient(new WebChromeClient() {});
			mYoutubeWebView.loadUrl(lURL);
			
			
			/*
			 * Show popup window
			 */
			View lAnchor = ((Activity) mParentActivity).findViewById(R.id.action_status);
			mPopupWindow.showAsDropDown(lAnchor, 50, 50);		                     	
			mPopupWindow.setFocusable(true);
			mPopupWindow.update();	    	    	        	    		
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
			mBT_Download.setEnabled(false);
			String lNewData = msg.getData().getString("info");
			Toast.makeText(mParentActivity, lNewData, Toast.LENGTH_LONG).show();			
		}
	};		
	

}
