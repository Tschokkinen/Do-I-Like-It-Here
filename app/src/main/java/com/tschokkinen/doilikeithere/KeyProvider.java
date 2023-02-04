package com.tschokkinen.doilikeithere;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import java.util.ArrayList;

public class KeyProvider extends ItemKeyProvider<String> {
    private ArrayList<String> items;

    /**
     * Creates a new provider with the given scope.
     *
     * @param scope Scope can't be changed at runtime.
     */
    protected KeyProvider(int scope, ArrayList<String> items) {
        super(scope);
        this.items = items;
    }

    @Nullable
    @Override
    public String getKey(int position) {
        return items.get(position);
    }

    @Override
    public int getPosition(@NonNull String key) {
        return items.indexOf(key);
    }

}
