package com.example.mp3diddly.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;




public class HTTP_Server 
{
	private Context mContext;
		
	/*
	 * 
	 * 
	 */
	public HTTP_Server(Context pContext)
	{
		mContext = pContext;
	}
	
	
	/*
	 * 
	 * 
	 */
	public String sendGETRequest(String pHost, String pPath)
	{
		InputStream content = null;
		String lRetVal = "";
		String lURL = "http://"+pHost+pPath;
		
		if (!isNetworkUp())
		{
			lRetVal = "Error: No network";
		}
		else
		{			
			try 
			{
			    HttpClient lClient = new DefaultHttpClient();
			    HttpGet lGetReq = new HttpGet(lURL);
			    HttpResponse lResponse = lClient.execute(lGetReq);
			    
			    content = lResponse.getEntity().getContent();
			    lRetVal = getStringFromInputStream(content);			    		    			    
			} 
			catch (Exception lEx) 
			{
				lRetVal = "Error(0):" + lEx.getMessage();
			}
		}
		
		return lRetVal;	
	}
	
	
	
	
	/*
	 * Convert InputStream to String
	 * 
	 */
	private String getStringFromInputStream(InputStream is) 
	{
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder(); 
		String line;
		
		try 
		{ 
			br = new BufferedReader(new InputStreamReader(is));
			
			while ((line = br.readLine()) != null)
				sb.append(line);		 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if (br != null) 
			{
				try 
				{
					br.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
	}

	
	
	/*
	 * 
	 * 
	 */
	public boolean isNetworkUp() 
	{
	    ConnectivityManager connMgr = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    
	    if (networkInfo != null && networkInfo.isConnected())
	        return(true);
	    else 
	    	return(false);
	}	
	
	
}
