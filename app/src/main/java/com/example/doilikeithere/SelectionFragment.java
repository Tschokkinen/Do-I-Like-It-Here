package com.example.doilikeithere;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doilikeithere.databinding.FragmentSelectionBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class SelectionFragment extends Fragment {
    private String TAG = "SelectionFragment";
    private FragmentSelectionBinding binding;

    protected ArrayList<String> recyclerViewItems = new ArrayList<>();
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private String arrayName;

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected SelectionRecyclerAdapter selectionRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayName = getArguments().getString("Selection");

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

        // Clear selections made by RecyclerAdapter.
        DataManager.selected.clear();

        // Get items for Recyclerview
        try {
            getRecyclerViewData(arrayName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelectionBinding.inflate(inflater, container, false);
        binding.getRoot();

        // Get RecyclerView
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.recyclerView);

        // Create new LinearLayoutManager for RecyclerView to mimic ListView layout.
        layoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        selectionRecyclerAdapter = new SelectionRecyclerAdapter(recyclerViewItems);
        // Set RecyclerAdapter as the adapter for RecyclerView.
        recyclerView.setAdapter(selectionRecyclerAdapter);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Save selections to a temporary array and go back to the previous page.
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear temp array that corresponds to the data displayed by the recycler view.
                if (arrayName.equals("Positives")) {
                    DataManager.clearTemps(DataManager.TempArrays.TEMP_POSITIVES);
                } else if (arrayName.equals("Negatives")) {
                    DataManager.clearTemps(DataManager.TempArrays.TEMP_NEGATIVES);
                } else if (arrayName.equals("Feelings")) {
                    DataManager.clearTemps(DataManager.TempArrays.TEMP_FEELINGS);
                }

                // Save user selections to empty temp array list.
                for(String s : DataManager.selected) {
                    if (arrayName.equals("Positives")) {
                        DataManager.tempPositives.add(s);
                    } else if (arrayName.equals("Negatives")) {
                        DataManager.tempNegatives.add(s);
                    } else if (arrayName.equals("Feelings")) {
                        DataManager.tempFeelings.add(s);
                    }
                }

                NavHostFragment.findNavController(SelectionFragment.this)
                        .navigate(R.id.action_SelectionFragment_to_ReviewFragment);
            }
        });
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