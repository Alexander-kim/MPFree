package com.example.mp3diddly;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class Broadcast_NewFiles extends BroadcastReceiver
{
	private static int mMessageCounter = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String lFileName = intent.getStringExtra(Config.TITLE_FILENAME);
		String lSongTitle = intent.getStringExtra(Config.TITLE_DESCRIPTION);
		String lVideoID = intent.getStringExtra(Config.TITLE_ID);
		
		Notification n = new Notification.Builder(context)
        .setContentTitle(Config.APP_NAME + " - New song")
        .setContentText(lSongTitle + "(id: " + lFileName + ")")
        .setSmallIcon(R.drawable.search)
        .build();
		    
		  
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		notificationManager.notify(mMessageCounter++, n); 
		
		
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notification);
		r.play();
	}

}
