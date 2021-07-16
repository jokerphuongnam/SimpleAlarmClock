package com.learntodroid.simplealarmclock.alarmslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.learntodroid.simplealarmclock.data.Alarm;
import com.learntodroid.simplealarmclock.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmRecyclerListAdapter extends ListAdapter<Alarm, AlarmViewHolder> {

    private final OnToggleAlarmListener listener;
    private final ItemTouchListener deleteListener;

    public AlarmRecyclerListAdapter(OnToggleAlarmListener listener, ItemTouchListener deleteListener) {
        super(new AlarmDiffCallback());
    //    this.alarms = new ArrayList<Alarm>();
        this.listener = listener;
        this.deleteListener =deleteListener;
    }

    public Alarm getAlarm(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(itemView, listener, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = getItem(position);
        holder.bind(alarm);
    }

//    @NonNull
//    @Override
//    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
//        return new AlarmViewHolder(itemView, listener);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
//        Alarm alarm = getItem(position);
//        holder.bind(alarm);
//    }
//
//
//
//
//
//    @Override
//    public void onViewRecycled(@NonNull AlarmViewHolder holder) {
//        super.onViewRecycled(holder);
//        holder.alarmStarted.setOnCheckedChangeListener(null);
//    }

}
class AlarmDiffCallback extends DiffUtil.ItemCallback<Alarm> {

    @Override
    public boolean areItemsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
        return oldItem.getAlarmId() == newItem.getAlarmId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
        return oldItem.getHour() == newItem.getHour() && oldItem.getMinute() == newItem.getMinute() && oldItem.getCreated() == newItem.getCreated() && oldItem.getTitle().equals(newItem.getTitle()) && oldItem.isStarted() == newItem.isStarted();
    }
}



