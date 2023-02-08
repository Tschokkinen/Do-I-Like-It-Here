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
import com.tschokkinen.doilikeithere.databinding.FragmentSelectionBinding;
import com.tschokkinen.doilikeithere.models.SelectionItem;

import java.util.ArrayList;


public class SelectionRecyclerAdapter extends RecyclerView.Adapter<SelectionRecyclerAdapter.ViewHolder> {
    private static final String TAG = "SelectionRA";
    private ArrayList<SelectionItem> recyclerViewItems;
    private int selectedPos = RecyclerView.NO_POSITION;
    private int selectedColor = Color.parseColor("#78ABEC");
    private int defaultColor = Color.parseColor("#FFFFFF");

    public SelectionRecyclerAdapter(ArrayList<SelectionItem> dataSet) {
        recyclerViewItems = dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View v) {
            super(v);

            // Change item background and text color when item is clicked.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d(TAG, "Element " + getBindingAdapterPosition() + " clicked.");
                    textView = v.findViewById(R.id.tx);
                    SelectionItem item = getItem(getBindingAdapterPosition());
                    if (item.getHasBeenSelected()) {
                        textView.setBackgroundColor(Color.TRANSPARENT);
                        textView.setTextColor(Color.GRAY);
                        item.setHasBeenSelected();
                    } else {
                        textView.setBackgroundColor(selectedColor);
                        textView.setTextColor(Color.BLACK);
                        item.setHasBeenSelected();
                    }
                }
            });
            textView = v.findViewById(R.id.tx);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    // Get item at recyclerview position
    public SelectionItem getItem(int pos) {
        return recyclerViewItems.get(pos);
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

        // Check if item has been checked previously before before saving the review
        // i.e. user revisits selection page.
        if(recyclerViewItems.get(position).getHasBeenSelected()) {
            viewHolder.getTextView().setBackgroundColor(selectedColor);
            viewHolder.getTextView().setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

}
