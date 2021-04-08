package com.learntodroid.simplealarmclock.alarmslist;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.learntodroid.simplealarmclock.R;
import com.learntodroid.simplealarmclock.activities.MainActivity;
import com.learntodroid.simplealarmclock.data.Alarm;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class AlarmsListFragment extends Fragment implements OnToggleAlarmListener {
    private AlarmRecyclerListAdapter alarmRecyclerListAdapter;
    private AlarmsListViewModel alarmsListViewModel;
    @BindView(R.id.fragment_listalarms_recylerView)
    RecyclerView alarmsRecyclerView;
    @BindView(R.id.extended_fab)
    ExtendedFloatingActionButton addAlarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTmp();
        alarmRecyclerListAdapter = new AlarmRecyclerListAdapter(this);
        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmsListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, alarms -> {
            if (alarms != null) {
                alarmRecyclerListAdapter.submitList(new ArrayList<>(alarms));
            }
        });

        alarmsListViewModel.getNoticeLiveData().observe(this, s -> {
            Toast.makeText(requireContext(), "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();
        });

//        FirebaseFirestore.getInstance().collection("alarms").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
//                List<Alarm> alarms = queryDocumentSnapshots.toObjects(Alarm.class);
//                alarmRecyclerListAdapter.submitList(alarms);
//                Log.i("test", "chay vao day" + System.currentTimeMillis());
//            }
//        });


    }

    public void setTmp() {
        Alarm alarm = new Alarm(0, 0, 0, "title", 0, false, false, false, false, false, false, false, false, false);
        FirebaseFirestore.getInstance().collection("alarms").document(String.valueOf(alarm.getAlarmId())).set(alarm, SetOptions.merge());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listalarms, container, false);
        ButterKnife.bind(this, view);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerListAdapter);
        alarmsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        addAlarm.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment));

        new ItemTouchHelper(new AlarmsTouchHelperCallBack((position, direction) -> {
            Alarm alarm = alarmRecyclerListAdapter.getAlarm(position);
            alarmsListViewModel.delete(alarm);
        })).attachToRecyclerView(alarmsRecyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.i("ddd", "onViewCreated: "+  auth.getCurrentUser().getUid());
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
    public void onResume() {
        super.onResume();
       alarmsListViewModel.getAlarm();
    }
}