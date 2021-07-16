package com.learntodroid.simplealarmclock.alarmslist;

import android.view.View;

import com.learntodroid.simplealarmclock.data.Alarm;

public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
    void itemClick(View v, Alarm alarm);
}
