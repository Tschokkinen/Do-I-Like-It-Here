package com.tschokkinen.doilikeithere.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.R;
import com.tschokkinen.doilikeithere.models.SelectionItem;

import java.util.ArrayList;


public class SelectionRecyclerAdapter extends RecyclerView.Adapter<SelectionRecyclerAdapter.ViewHolder> {
    private static final String TAG = "SelectionRA";
    private ArrayList<SelectionItem> recyclerViewItems;
    private int selectedPos = RecyclerView.NO_POSITION;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View v) {
            super(v);

            int selected = Color.parseColor("#78ABEC");
            int defaultColor = Color.parseColor("#FFFFFF");

            // Click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d(TAG, "Element " + getBindingAdapterPosition() + " clicked.");
                    textView = v.findViewById(R.id.tx);
                    if (textView.getCurrentTextColor() == Color.BLACK) {
                        textView.setBackgroundColor(Color.TRANSPARENT);
                        textView.setTextColor(Color.GRAY);

                        SelectionItem item = getItem(getBindingAdapterPosition());
                        // Save selection to DataManager selected ArrayList
                        if (DataManager.selected.contains(item)) {
                            DataManager.selected.remove(item);
                            Log.d(TAG, "Removed: " + textView.getText().toString());
                        }
                    } else {
                        textView.setBackgroundColor(selected);
                        textView.setTextColor(Color.BLACK);

                        SelectionItem item = getItem(getBindingAdapterPosition());
                        // Remove selection from DataManager selected ArrayList
                        if (!DataManager.selected.contains(item)) {
                            DataManager.selected.add(item);
                            Log.d(TAG, "Added: " + textView.getText().toString());
                            SelectionItem selected = getItem(getAbsoluteAdapterPosition());
                            Log.d(TAG, selected.getName());
                        }
                    }
                }
            });
            textView = v.findViewById(R.id.tx);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public SelectionItem getItem(int pos) {
        return recyclerViewItems.get(pos);
    }

    public SelectionRecyclerAdapter(ArrayList<SelectionItem> dataSet) {
        recyclerViewItems = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.itemView.setSelected(selectedPos == position);

        // Replace elements with recyclerViewItems elements.
        viewHolder.getTextView().setText(recyclerViewItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

}
