package com.example.doilikeithere;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private ArrayList<String> recyclerViewItems;
    private int selectedPos = RecyclerView.NO_POSITION;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d(TAG, "Element " + getBindingAdapterPosition() + " clicked.");
                    textView = v.findViewById(R.id.tx);
//                    if (textView.getCurrentTextColor() == Color.RED) {
//                        textView.setTextColor(Color.BLACK);
//                        // Save selection to DataManager selected ArrayList
//                        if (DataManager.selected.contains(textView.getText().toString())) {
//                            DataManager.selected.remove(textView.getText().toString());
//                            Log.d(TAG, "Removed: " + textView.getText().toString());
//                        }
//                    } else {
//                        textView.setTextColor(Color.RED);
//                        // Remove selection from DataManager selected ArrayList
//                        if (!DataManager.selected.contains(textView.getText().toString())) {
//                            DataManager.selected.add(textView.getText().toString());
//                            Log.d(TAG, "Added: " + textView.getText().toString());
//                        }
//                    }
                }
            });
            textView = v.findViewById(R.id.tx);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public HistoryRecyclerAdapter(ArrayList<String> dataSet) {
        recyclerViewItems = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_row_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.itemView.setSelected(selectedPos == position);

        // Replace elements with recyclerViewItems elements.
        viewHolder.getTextView().setText(recyclerViewItems.get(position));
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

}
