package com.tschokkinen.doilikeithere.adapters;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tschokkinen.doilikeithere.Alert;
import com.tschokkinen.doilikeithere.R;
import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.viewholders.HistoryViewHolder;
import com.tschokkinen.doilikeithere.models.ReviewItem;
import com.tschokkinen.doilikeithere.databinding.HistoryRowItemBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

//implements View.OnClickListener, View.OnLongClickListener

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryViewHolder>  {
    private static final String TAG = "HistoryRecyclerAdapter";
    private ArrayList<ReviewItem> recyclerViewItems;
    private int selectedPos = RecyclerView.NO_POSITION;
    Context context;
    private Fragment fragment;

    public HistoryRecyclerAdapter(ArrayList<ReviewItem> dataSet, Context context, Fragment fragment) {
        this.recyclerViewItems = dataSet;
        this.context = context;
        this.fragment = fragment;
        //Log.d(TAG, "HistoryRecyclerAdapter instantiated.");
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

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Clicked " + position);
//            }
//        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = viewHolder.getBindingAdapterPosition();

                if (pos != selectedPos) {
                    // Create dialog. Position indicates item position in JSON file
                    // Currently HistoryRecyclerAdapter creates its own Alert inline:
                    // when deleting entire database or all reviews Alert class is used.
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Delete review?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Empty review database.
                                    try {
                                        DataManager.deleteFromDatabase(context,
                                                DataManager.DeleteCommands.DELETE_ONE, pos);
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
    public void removeItem(int position, HistoryViewHolder viewHolder) {
        //Log.d(TAG, "RecyclerItems pos " + position);
        recyclerViewItems.remove(position);
        //Log.d(TAG, "ViewHolder position " + position);
        viewHolder.getBindingAdapter().notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }
}
