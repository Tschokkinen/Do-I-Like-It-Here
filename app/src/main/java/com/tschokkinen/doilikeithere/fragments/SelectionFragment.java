package com.tschokkinen.doilikeithere.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.R;
import com.tschokkinen.doilikeithere.adapters.SelectionRecyclerAdapter;
import com.tschokkinen.doilikeithere.databinding.FragmentSelectionBinding;
import com.tschokkinen.doilikeithere.models.SelectionItem;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class SelectionFragment extends Fragment {
    private String TAG = "SelectionFragment";
    private FragmentSelectionBinding binding;

    protected ArrayList<SelectionItem> recyclerViewItems = new ArrayList<>();
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private String arrayName;

    protected RecyclerView.LayoutManager layoutManager;
    protected SelectionRecyclerAdapter selectionRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayName = getArguments().getString("Selection");
        Log.d(TAG, "ArrayName " + arrayName);

        // Set action bar title according to user selection.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select " + arrayName);

        // Device back button click.
        // Doesn't do anything at the moment.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button even
//                Log.d("BACKBUTTON", "Back button clicks");
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // Get items for Recyclerview
        ArrayList<SelectionItem> previousTempArrayExists = DataManager.checkTempArray(arrayName);
        if (previousTempArrayExists != null) {
            recyclerViewItems = previousTempArrayExists;
            Log.d(TAG, "Not null");
        } else {
            try {
                getRecyclerViewData(arrayName);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectionBinding.inflate(inflater, container, false);
        binding.getRoot();

        // Create new LinearLayoutManager for RecyclerView to mimic ListView layout.
        layoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        selectionRecyclerAdapter = new SelectionRecyclerAdapter(recyclerViewItems, getContext(), arrayName);
        // Set RecyclerAdapter as the adapter for RecyclerView.
        binding.recyclerView.setAdapter(selectionRecyclerAdapter);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Go back to the previous page.
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SelectionFragment.this)
                        .navigate(R.id.action_SelectionFragment_to_ReviewFragment);
            }
        });

        binding.buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SelectionFragment.this)
                        .navigate(R.id.action_SelectionFragment_to_addNewItem);
            }
        });
    }

    private void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        if (binding.recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) binding.recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.scrollToPosition(scrollPosition);
    }

    private void getRecyclerViewData(String arrayName) throws IOException, JSONException {
        // Get data for recyclerview.
        recyclerViewItems = DataManager.loadSelectionRecyclerviewItems(getContext(), arrayName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}