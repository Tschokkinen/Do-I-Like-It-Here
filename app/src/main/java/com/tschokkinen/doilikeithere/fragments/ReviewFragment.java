package com.tschokkinen.doilikeithere.fragments;

import org.json.JSONException;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.R;
import com.tschokkinen.doilikeithere.databinding.FragmentReviewBinding;

import java.io.IOException;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Check Bundle to see if user navigated to fragment from main_page.
        // If so, clear all temp arrays and location.
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("FromMainPage")) {
            DataManager.clearAllDataCollectionVariables();
        }

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //location_editText = (EditText) getView().findViewById(R.id.editTextLocation);

        if (!DataManager.locationValue.isEmpty()) {
            binding.editTextLocation.setText(DataManager.locationValue);
        } else {
            binding.editTextLocation.setText("");
        }

        // Save review to database and go back to the main page (i.e. FirstFragment).
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    checkLocationEditText();
                    DataManager.addNewReview(getContext(), "Reviews");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                NavHostFragment.findNavController(ReviewFragment.this)
                        .navigate(R.id.action_ReviewFragment_to_displayReviewScore);
            }
        });

        // Go to selection page where user can select attributes to review the current place.
        binding.selectPositivesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationEditText();
                Bundle bundle = new Bundle();
                bundle.putString("Selection", "Positives");
                NavHostFragment.findNavController(ReviewFragment.this)
                        .navigate(R.id.action_ReviewFragment_to_SelectionFragment, bundle);
            }
        });

        binding.selectNegativesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationEditText();
                Bundle bundle = new Bundle();
                bundle.putString("Selection", "Negatives");
                NavHostFragment.findNavController(ReviewFragment.this)
                        .navigate(R.id.action_ReviewFragment_to_SelectionFragment, bundle);
            }
        });

        binding.selectFeelingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationEditText();
                Bundle bundle = new Bundle();
                bundle.putString("Selection", "Feelings");
                NavHostFragment.findNavController(ReviewFragment.this)
                        .navigate(R.id.action_ReviewFragment_to_SelectionFragment, bundle);
            }
        });
    }

    // Check if location has been entered in location_editText field.
    private void checkLocationEditText() {
        if (!binding.editTextLocation.getText().toString().isEmpty()) {
            DataManager.locationValue = binding.editTextLocation.getText().toString();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}