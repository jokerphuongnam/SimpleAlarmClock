package com.learntodroid.simplealarmclock.createalarm;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
    LinearLayout recurringOptions;

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

        recurring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                recurringOptions.setVisibility(View.VISIBLE);
            } else {
                recurringOptions.setVisibility(View.GONE);
            }
        });

        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAlarm();
                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        return view;
    }


    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                title.getText().toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked()
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(requireContext());
    }


}
