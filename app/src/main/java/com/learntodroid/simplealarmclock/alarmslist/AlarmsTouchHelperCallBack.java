package com.learntodroid.simplealarmclock.alarmslist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmsTouchHelperCallBack extends ItemTouchHelper.SimpleCallback {
    private ItemTouchListener mListener;


    public AlarmsTouchHelperCallBack(ItemTouchListener listener) {
        super(0, ItemTouchHelper.START | ItemTouchHelper.END);
        this.mListener = listener;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.swipe(viewHolder.getAdapterPosition(),direction);
    }
}
