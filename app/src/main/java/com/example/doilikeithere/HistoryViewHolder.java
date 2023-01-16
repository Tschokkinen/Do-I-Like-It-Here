package com.example.doilikeithere;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doilikeithere.databinding.HistoryRowItemBinding;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private final HistoryRowItemBinding binding;

    public HistoryViewHolder(HistoryRowItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void setViewHolderData(ReviewItem reviewItem, Resources resources) {
        // Set location data.
        this.binding.location.setText(reviewItem.location);

        // Set positives data.
        this.binding.positives.setText(reviewItem.positives);

        // Set negatives data.
        this.binding.negatives.setText(reviewItem.negatives);

        // Set feelings data.
        this.binding.feelings.setText(reviewItem.feelings);

        // Set review score data.
        this.binding.score.setText(String.valueOf(reviewItem.score));
    }
}
