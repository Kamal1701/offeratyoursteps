package com.example.offersatyoursteps.utilities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.activities.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class OfferAtYourStepPushNotification : FirebaseMessagingService() {
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage : RemoteMessage) {
        if(remoteMessage.notification != null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title:String, message:String){
        
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        
        var builder:NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, PUSH_CHANNEL_ID)
            .setSmallIcon(R.drawable.oays)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        
        builder = builder.setContent(getRemoveView(title, message))
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val notificationChannel = NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(0,builder.build())
    }
    
    fun getRemoveView(title : String, message : String) : RemoteViews{
        val remoteView = RemoteViews("package com.example.offersatyoursteps.utilities",R.layout.oays_push_notification)
        remoteView.setTextViewText(R.id.pushTitle, title)
        remoteView.setTextViewText(R.id.pushMessage, message)
        remoteView.setImageViewResource(R.id.pushLogoImg, R.drawable.oays)
        
        return remoteView
    
    }
}