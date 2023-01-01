package com.example.doilikeithere;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doilikeithere.databinding.FragmentHistoryBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    protected ArrayList<String> recyclerViewItems = new ArrayList<>();
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected HistoryRecyclerAdapter historyRecyclerAdapter;

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public HistoryFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HistoryFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HistoryFragment newInstance(String param1, String param2) {
//        HistoryFragment fragment = new HistoryFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        // Get items for Recyclerview
        try {
            getRecyclerViewData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        binding.getRoot();

        // Get RecyclerView
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.recyclerView);

        // Create new LinearLayoutManager for RecyclerView to mimic ListView layout.
        layoutManager = new LinearLayoutManager(getActivity());

        setRecyclerViewLayoutManager();

        historyRecyclerAdapter = new HistoryRecyclerAdapter(recyclerViewItems);
        // Set RecyclerAdapter as the adapter for RecyclerView.
        recyclerView.setAdapter(historyRecyclerAdapter);

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

    private void getRecyclerViewData() throws IOException, JSONException {
        // Get data for recyclerview.
        // Make arrayName selection DYNAMIC later. Currently returns only items labeled as positive.
        recyclerViewItems = DataManager.loadReviewsRecyclerviewItems(getContext());
    }
}