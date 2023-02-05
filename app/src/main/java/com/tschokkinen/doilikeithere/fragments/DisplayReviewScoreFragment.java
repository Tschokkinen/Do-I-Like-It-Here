package com.tschokkinen.doilikeithere.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.R;
import com.tschokkinen.doilikeithere.databinding.FragmentDisplayReviewScoreBinding;

public class DisplayReviewScoreFragment extends Fragment {
    private FragmentDisplayReviewScoreBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDisplayReviewScoreBinding.inflate(inflater, container, false);
        binding.getRoot();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            TextView textView = (TextView) getView().findViewById(R.id.displayScoreTextView);
            textView.setText(String.valueOf(DataManager.latestReviewScore));

            binding.okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(DisplayReviewScoreFragment.this)
                            .navigate(R.id.action_DisplayReviewScoreFragment_to_MainPageFragment);
                }
            });
    }
}