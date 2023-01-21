package com.tschokkinen.doilikeithere.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.adapters.HistoryRecyclerAdapter;
import com.tschokkinen.doilikeithere.models.ReviewItem;
import com.tschokkinen.doilikeithere.R;
import com.tschokkinen.doilikeithere.databinding.FragmentHistoryBinding;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private final String TAG = "HistoryFragment";
    private FragmentHistoryBinding binding;

    protected ArrayList<ReviewItem> recyclerViewItems = new ArrayList<>();

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected HistoryRecyclerAdapter historyRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get items for Recyclerview
        try {
            getRecyclerViewData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        binding.getRoot();

        // Get RecyclerView
        recyclerView = binding.getRoot().findViewById(R.id.recyclerView);

        // Create new LinearLayoutManager for RecyclerView to mimic ListView layout.
        layoutManager = new LinearLayoutManager(getActivity());

        historyRecyclerAdapter = new HistoryRecyclerAdapter(recyclerViewItems, getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);

        // Set RecyclerAdapter as the adapter for RecyclerView.
        recyclerView.setAdapter(historyRecyclerAdapter);
        setRecyclerViewLayoutManager();
        //  Set List layout.
        //this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d(TAG, "onCreateView finished.");
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    private void getRecyclerViewData() throws IOException, JSONException, ParseException {
        // Get data for recyclerview.
        recyclerViewItems = DataManager.loadReviewsRecyclerviewItems(getContext());
    }
}