package com.learntodroid.simplealarmclock.alarmslist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.data.Alarm;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class AlarmsListFragment extends Fragment implements OnToggleAlarmListener, ItemTouchListener {
    private AlarmRecyclerListAdapter alarmRecyclerListAdapter;
    private AlarmsListViewModel alarmsListViewModel;
    @BindView(R.id.fragment_listalarms_recylerView)
    RecyclerView alarmsRecyclerView;
    @BindView(R.id.extended_fab)
    ExtendedFloatingActionButton addAlarm;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTmp();
        alarmRecyclerListAdapter = new AlarmRecyclerListAdapter(this, this);
        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmsListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, alarms -> {
            if (alarms != null) {
                alarmRecyclerListAdapter.submitList(new ArrayList<>(alarms));
            } else {
                alarmRecyclerListAdapter.submitList(new ArrayList<>());
            }
            alarmRecyclerListAdapter.notifyDataSetChanged();
        });

        alarmsListViewModel.getNoticeLiveData().observe(this, s -> {
            Toast.makeText(requireContext(), "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
        });
        alarmsListViewModel.updateToken();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listalarms, container, false);
        ButterKnife.bind(this, view);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerListAdapter);
        alarmsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        addAlarm.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment, null));
        new ItemTouchHelper(new AlarmsTouchHelperCallBack(this)).attachToRecyclerView(alarmsRecyclerView);
        return view;
    }

    @Override
    public void swipe(int position, int direction) {
        showNotify(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showNotify(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Delete alarm " + alarmRecyclerListAdapter.getAlarm(position).getTitle() + "?");
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            Alarm alarm = alarmRecyclerListAdapter.getAlarm(position);
            alarmsListViewModel.delete(alarm);
            dialogInterface.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> alarmRecyclerListAdapter.notifyDataSetChanged());
        AlertDialog alertDialog = builder.create(); //create
        alertDialog.show(); //Show it.
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.i("ddd", "onViewCreated: " + auth.getCurrentUser().getUid());
    }

    @Override
    public void onToggle(@NotNull Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(requireContext());
        } else {
            alarm.schedule(requireContext());
        }
        alarmsListViewModel.update(alarm);
    }

    @Override
    public void itemClick(View v, Alarm alarm) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm", alarm);
        Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment, bundle);
    }

    UpdateAlarmReceiver receiver;
    @Override
    public void onResume() {
        super.onResume();
        alarmsListViewModel.getAlarm();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.khang.simplealarmclock");
        receiver = new UpdateAlarmReceiver();
        requireContext().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(receiver);
        receiver = null;
    }

    public class UpdateAlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            alarmsListViewModel.getAlarm();
        }
    }
}