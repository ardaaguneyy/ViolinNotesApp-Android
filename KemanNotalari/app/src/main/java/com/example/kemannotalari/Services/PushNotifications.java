package com.example.kemannotalari.Services;

import android.content.Context;

import com.onesignal.OneSignal;

public class PushNotifications {
    public void push(Context context){

  //OneSignal Initialization
    OneSignal.startInit(context)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();


        }
    
}
