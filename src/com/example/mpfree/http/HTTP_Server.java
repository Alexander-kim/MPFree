package com.example.mpfree.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
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
	 * Download file
	 * 
	 */
	public boolean downloadFile(String pURL, String pDirectory, String pFileName) 
	{
		boolean lRetval = false;
		Log.v("mp3diddly", "downloadFile(1): |" + pURL + "|" + pDirectory + "|" + pFileName + "|");
		
		if (pURL != null && !pURL.isEmpty() && pDirectory != null && !pDirectory.isEmpty() && pFileName != null && !pFileName.isEmpty())
		{
			Log.v("mp3diddly", "downloadFile(2):");
		    try 
		    {
		    	int count = 0;
		    	File lOutputFile = new File(pDirectory, pFileName);
	            URL url = new URL(pURL);
	            URLConnection connection = url.openConnection();
	            connection.connect();
	            int lengthOfFile = connection.getContentLength();
	            long total = 0;
	            InputStream input = new BufferedInputStream(url.openStream());
	            OutputStream output = new FileOutputStream(lOutputFile);
	            byte data[] = new byte[1024];
	            
	            
	            while ((count = input.read(data)) != -1) 
	            {
	                total += count;
	                output.write(data, 0, count);
	            }
	            output.flush();
	            output.close();
	            input.close();
			        
				lRetval = true;
		    }
		    catch (IllegalStateException e) 
		    {
				Log.v("mp3diddly", "downloadFile(Exception): " + e.getMessage());
		    } 
		    catch (IOException e) 
		    {
				Log.v("mp3diddly", "downloadFile(Exception): " + e.getMessage());
		    }
		    catch (Exception e) 
		    {
				Log.v("mp3diddly", "downloadFile(Exception): " + e.getMessage());
		    }
		} // if (pURL...

		return(lRetval);
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
