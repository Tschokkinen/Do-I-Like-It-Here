package com.tschokkinen.doilikeithere.viewholders;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tschokkinen.doilikeithere.DateFormatters;
import com.tschokkinen.doilikeithere.models.ReviewItem;
import com.tschokkinen.doilikeithere.databinding.HistoryRowItemBinding;

/**
 * A class for history view ViewHolder.
 * @author Gavril Tschokkinen
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private final HistoryRowItemBinding binding;

    private String TAG = "HistoryViewHolder";

    private Fragment fragment;

    /**
     * HistoryViewHolder constructor.
     *
     * @param  binding  item for binding.
     */
    public HistoryViewHolder(HistoryRowItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * SelectionItem constructor.
     *
     * @param  reviewItem  Instantiated ReviewItem used to populate the view
     *                     with included values.
     * @param  resources  Value not in use currently
     */
    public void setViewHolderData(ReviewItem reviewItem, Resources resources) {
        // Set location data.
        this.binding.location.setText(reviewItem.location);

        // Set date
        this.binding.dateAndTime.setText(DateFormatters.sdfDMY.format(reviewItem.date));

        // Set positives data.
        this.binding.positives.setText(reviewItem.positives);

        // Set negatives data.
        this.binding.negatives.setText(reviewItem.negatives);

        // Set feelings data.
        this.binding.feelings.setText(reviewItem.feelings);

        // Set review score data.
        this.binding.score.setText(String.valueOf(reviewItem.score));

        Log.d("HistoryViewHolder", "Here's some history for ya.");
    }
}