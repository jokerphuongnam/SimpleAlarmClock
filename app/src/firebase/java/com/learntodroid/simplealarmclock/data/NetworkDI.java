package com.learntodroid.simplealarmclock.data;

public class NetworkDI {
    public static AlarmNetwork getAlarmNetwork(){
        return FirebaseAlarmImpl.getInstance();
    }
}
