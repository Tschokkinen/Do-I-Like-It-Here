package com.example.doilikeithere;

import org.json.JSONException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.example.doilikeithere.databinding.FragmentReviewBinding;

import java.io.IOException;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Save review to database and go back to the main page (i.e. FirstFragment).
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DataManager.addNewReview(getContext(), "Reviews");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NavHostFragment.findNavController(ReviewFragment.this)
                        .navigate(R.id.action_ReviewFragment_to_MainPageFragment);
            }
        });

        // Go to selection page where user can select attributes to review the current place.
        binding.selectPositivesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Selection", "Positives");
                NavHostFragment.findNavController(ReviewFragment.this)
                        .navigate(R.id.action_ReviewFragment_to_SelectionFragment, bundle);
            }
        });

        binding.selectNegativesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Selection", "Negatives");
                NavHostFragment.findNavController(ReviewFragment.this)
                        .navigate(R.id.action_ReviewFragment_to_SelectionFragment, bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}