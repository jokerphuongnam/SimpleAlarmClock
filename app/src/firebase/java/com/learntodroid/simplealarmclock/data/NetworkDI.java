package com.learntodroid.simplealarmclock.data;

import androidx.annotation.NonNull;

public class NetworkDI {
    @NonNull
    public static AlarmNetwork getAlarmNetwork(){
        return FirebaseAlarmImpl.getInstance();
    }
}
