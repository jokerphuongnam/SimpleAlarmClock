package com.learntodroid.simplealarmclock.fcm;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class RemoteService extends FirebaseMessagingService {
}