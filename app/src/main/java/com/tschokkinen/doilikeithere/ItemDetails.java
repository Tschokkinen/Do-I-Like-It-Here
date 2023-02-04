package com.tschokkinen.doilikeithere;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class ItemDetails extends ItemDetailsLookup.ItemDetails {
    private int position;
    private String item;

    public ItemDetails(int position, String item) {
        this.position = position;
        this.item = item;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Nullable
    @Override
    public Object getSelectionKey() {
        return item;
    }
}
