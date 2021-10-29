package com.learntodroid.simplealarmclock.createalarm;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.data.Alarm;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
@SuppressLint("NonConstantResourceId")
public class CreateAlarmFragment extends Fragment {
    @BindView(R.id.fragment_createalarm_timePicker)
    TimePicker timePicker;
    @BindView(R.id.fragment_createalarm_title)
    EditText title;
    @BindView(R.id.fragment_createalarm_scheduleAlarm)
    Button scheduleAlarm;
    @BindView(R.id.fragment_createalarm_recurring)
    MaterialCheckBox recurring;
    @BindView(R.id.fragment_createalarm_checkMon)
    MaterialCheckBox mon;
    @BindView(R.id.fragment_createalarm_checkTue)
    MaterialCheckBox tue;
    @BindView(R.id.fragment_createalarm_checkWed)
    MaterialCheckBox wed;
    @BindView(R.id.fragment_createalarm_checkThu)
    MaterialCheckBox thu;
    @BindView(R.id.fragment_createalarm_checkFri)
    MaterialCheckBox fri;
    @BindView(R.id.fragment_createalarm_checkSat)
    MaterialCheckBox sat;
    @BindView(R.id.fragment_createalarm_checkSun)
    MaterialCheckBox sun;
    @BindView(R.id.fragment_createalarm_recurring_options)
    NestedScrollView recurringOptions;
    private Alarm alarm = null;

    private CreateAlarmViewModel createAlarmViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createalarm, container, false);

        ButterKnife.bind(this, view);
        try {
            if (getArguments() != null) {
                alarm = (Alarm) getArguments().getSerializable("alarm");
            }
            timePicker.setHour(alarm.getHour());
            timePicker.setMinute(alarm.getMinute());
            title.setText(alarm.getTitle());
            recurring.setChecked(alarm.isRecurring());
            if (alarm.isRecurring()) {
                recurringOptions.setVisibility(View.VISIBLE);
            } else {
                recurringOptions.setVisibility(View.GONE);
            }
            mon.setChecked(alarm.isMonday());
            tue.setChecked(alarm.isTuesday());
            wed.setChecked(alarm.isWednesday());
            thu.setChecked(alarm.isThursday());
            fri.setChecked(alarm.isFriday());
            sat.setChecked(alarm.isSaturday());
            sun.setChecked(alarm.isSunday());
        } catch (Exception ignored) {
        }

        recurring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                recurringOptions.setVisibility(View.VISIBLE);
            } else {
                recurringOptions.setVisibility(View.GONE);
            }
        });

        scheduleAlarm.setOnClickListener(v -> {
            scheduleAlarm();
            Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
        });

        return view;
    }


    private void scheduleAlarm() {
        boolean isCreate = alarm == null;
        if (isCreate) {
            int alarmId = new Random().nextInt(Integer.MAX_VALUE);
            alarm = new Alarm();
            alarm.setAlarmId(alarmId);
        }
        alarm.setHour(TimePickerUtil.getTimePickerHour(timePicker));
        alarm.setMinute(TimePickerUtil.getTimePickerMinute(timePicker));
        alarm.setTitle(title.getText().toString());
        alarm.setCreated(System.currentTimeMillis());
        alarm.setStarted(true);
        alarm.setRecurring(recurring.isChecked());
        alarm.setMonday(mon.isChecked());
        alarm.setTuesday(tue.isChecked());
        alarm.setWednesday(wed.isChecked());
        alarm.setThursday(thu.isChecked());
        alarm.setFriday(fri.isChecked());
        alarm.setSaturday(sat.isChecked());
        alarm.setSunday(sun.isChecked());

        alarm.schedule(requireContext());
        if (isCreate) {
            createAlarmViewModel.insert(alarm);
        } else {
            createAlarmViewModel.update(alarm);
        }
    }
}
