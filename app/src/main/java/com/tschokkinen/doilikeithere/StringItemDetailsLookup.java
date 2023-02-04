package com.tschokkinen.doilikeithere;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.tschokkinen.doilikeithere.adapters.SelectionRecyclerAdapter;

public class StringItemDetailsLookup extends ItemDetailsLookup {
    private final RecyclerView recyclerView;

    public StringItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {

        return null;
    }
}
