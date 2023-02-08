package com.tschokkinen.doilikeithere.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.R;
import com.tschokkinen.doilikeithere.models.SelectionItem;
import com.tschokkinen.doilikeithere.viewholders.HistoryViewHolder;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class SelectionRecyclerAdapter extends RecyclerView.Adapter<SelectionRecyclerAdapter.ViewHolder> {
    private static final String TAG = "SelectionRA";
    private ArrayList<SelectionItem> recyclerViewItems;
    private int selectedPos = RecyclerView.NO_POSITION;
    private int selectedColor = Color.parseColor("#78ABEC");
    private Context context;
    private String arrayName;

    public SelectionRecyclerAdapter(ArrayList<SelectionItem> dataSet, Context context, String arrayName) {
        recyclerViewItems = dataSet;
        this.context = context;
        this.arrayName = arrayName;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View v) {
            super(v);

            // Change item color if it has been clicked.
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

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = viewHolder.getBindingAdapterPosition();

                if (pos != selectedPos) {
                    // Create dialog. Position indicates item position in JSON file
                    // NOTE: Currently HistoryRecyclerAdapter creates its own Alert inline:
                    // when deleting entire database or all reviews separate Alert class is used.
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Delete item?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Remove selected review from database.
                                    try {
                                        DataManager.deleteFromDatabase(context,
                                                DataManager.DeleteCommands.DELETE_ONE, pos, arrayName);
                                        removeItem(pos, viewHolder);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
                return true;
            }
        });
    }

    // Remove item from recyclerViewItems array and update RecyclerView.
    public void removeItem(int position, ViewHolder viewHolder) {
        //Log.d(TAG, "RecyclerItems pos " + position);
        recyclerViewItems.remove(position);
        //Log.d(TAG, "ViewHolder position " + position);
        assert viewHolder.getBindingAdapter() != null;
        viewHolder.getBindingAdapter().notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

}
