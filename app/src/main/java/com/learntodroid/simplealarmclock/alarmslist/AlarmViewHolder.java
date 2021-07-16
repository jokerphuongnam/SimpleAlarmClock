package com.learntodroid.simplealarmclock.alarmslist;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.data.Alarm;

import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private final TextView alarmTime;
    private final ImageView alarmRecurring;
    private final TextView alarmRecurringDays;
    private final TextView alarmTitle;
    private final CardView container;
    private final ItemTouchListener deleteListener;

    SwitchMaterial alarmStarted;

    private final OnToggleAlarmListener listener;

    public AlarmViewHolder(@NonNull View itemView, OnToggleAlarmListener listener,ItemTouchListener deleteListener) {
        super(itemView);
        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);
        container = itemView.findViewById(R.id.container);
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    public void bind(@NotNull Alarm alarm) {
        String alarmText = format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Once Off");
        }

        if (alarm.getTitle() != null && alarm.getTitle().length() != 0) {
            alarmTitle.setText(format("%s | %d | %d", alarm.getTitle(), alarm.getAlarmId(), alarm.getCreated()));
        } else {
            alarmTitle.setText(format("%s | %d | %d", "Alarm", alarm.getAlarmId(), alarm.getCreated()));
        }

        alarmStarted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alarm.setRecurring(isChecked);
            listener.onToggle(alarm);
        });

        container.setOnLongClickListener((view) -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.pop_up_option_menu);
            popupMenu.setOnMenuItemClickListener((menuItem) -> {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        deleteListener.swipe(getAdapterPosition(), 0 );
                        break;
                    case R.id.edit:
                        listener.itemClick(view, alarm);
                        break;
                }
                return true;
            });
            popupMenu.show();
            return true;
        });

        container.setOnClickListener((view) -> {
            listener.itemClick(view, alarm);
        });
    }
}
