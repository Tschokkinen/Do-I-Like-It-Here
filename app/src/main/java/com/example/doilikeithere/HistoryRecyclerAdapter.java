package com.example.doilikeithere;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doilikeithere.databinding.HistoryRowItemBinding;

import java.util.ArrayList;


public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private ArrayList<ReviewItem> recyclerViewItems;
    private int selectedPos = RecyclerView.NO_POSITION;
    Context context;

    public HistoryRecyclerAdapter(ArrayList<ReviewItem> dataSet, Context context) {
        this.recyclerViewItems = dataSet;
        this.context = context;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        HistoryRowItemBinding binding = HistoryRowItemBinding.inflate(layoutInflater, viewGroup, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder viewHolder, final int position) {
        viewHolder.itemView.setSelected(selectedPos == position);
        viewHolder.setViewHolderData(recyclerViewItems.get(position), context.getResources());
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

}
