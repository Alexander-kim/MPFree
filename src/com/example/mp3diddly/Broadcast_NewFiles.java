package com.example.mp3diddly;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Broadcast_NewFiles extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String lFileName = intent.getStringExtra(Config.TITLE_FILENAME);
		String lSongTitle = intent.getStringExtra(Config.TITLE_DESCRIPTION);
		
		Notification n = new Notification.Builder(context)
        .setContentTitle(Config.APP_NAME + " - New song")
        .setContentText(lSongTitle)
        .setSmallIcon(R.drawable.search)
        .build();
		    
		  
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, n); 
	}

}
