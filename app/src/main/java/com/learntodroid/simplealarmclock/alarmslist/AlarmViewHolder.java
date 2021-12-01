package com.learntodroid.simplealarmclock.alarmslist;

import static java.lang.String.format;

import android.annotation.SuppressLint;
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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class AlarmViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_alarm_time)
    TextView alarmTime;
    @BindView(R.id.item_alarm_recurring)
    ImageView alarmRecurring;
    @BindView(R.id.item_alarm_recurringDays)
    TextView alarmRecurringDays;
    @BindView(R.id.item_alarm_title)
    TextView alarmTitle;
    @BindView(R.id.container)
    CardView container;
    @BindView(R.id.item_alarm_started)
    SwitchMaterial alarmStarted;

    private final OnToggleAlarmListener listener;
    private final ItemTouchListener deleteListener;

    public AlarmViewHolder(@NonNull View itemView, OnToggleAlarmListener listener,ItemTouchListener deleteListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    public void bind(@NotNull Alarm alarm) {
        String alarmText = format(Locale.getDefault(),"%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText(R.string.one_off);
        }

        if (alarm.getTitle() != null && alarm.getTitle().length() != 0) {
            alarmTitle.setText(format(Locale.getDefault(),"%s | %d | %d", alarm.getTitle(), alarm.getAlarmId(), alarm.getCreated()));
        } else {
            alarmTitle.setText(format(Locale.getDefault(),"%s | %d | %d", "Alarm", alarm.getAlarmId(), alarm.getCreated()));
        }

        alarmStarted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()){
                alarm.setRecurring(isChecked);
                listener.onToggle(alarm);
            }
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

        container.setOnClickListener((view) -> listener.itemClick(view, alarm));
    }
}
