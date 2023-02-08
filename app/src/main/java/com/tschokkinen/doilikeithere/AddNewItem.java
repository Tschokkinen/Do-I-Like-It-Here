package com.tschokkinen.doilikeithere;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.databinding.FragmentAddNewItemBinding;
import com.tschokkinen.doilikeithere.fragments.SettingsFragment;

import org.json.JSONException;

import java.io.IOException;

public class AddNewItem extends Fragment {
    private String TAG = "AddNewItem";
    private FragmentAddNewItemBinding binding;

    private String arrayName = "";
    private String nameValue = "";
    private int itemWeight = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddNewItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Radio group listener.
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = getView().findViewById(i);
                //Log.d(TAG, radioButton.getText().toString());

                // Get arrayName by radio button selection.
                arrayName = radioButton.getTag().toString();
                Log.d(TAG, "ArrayName " + arrayName);
            }
        });

        // Save button listener
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if any of the fields are empty.
                // Log.d(TAG, "Trying to save.");
                if(binding.itemNameEditText.getText().toString().length() != 0 &&
                        binding.itemWeightEditText.getText().toString().length() != 0 &&
                        !arrayName.equals("")) {
                    // Log.d(TAG, "Checks passed.");
                    saveUserInput();
                    resetAll();
                } else {
                    // Give empty field message.
                    // (not yet implemented)
                }
            }
        });
    }

    // Save user input.
    private void saveUserInput() {
        nameValue = binding.itemNameEditText.getText().toString();
        itemWeight = Integer.parseInt(binding.itemWeightEditText.getText().toString());
        // If user entered positive value for a negative attribute, convert value to negative.
        // If user entered negative value for a positive attribute, convert value to positive.
        if (arrayName.equals("Negatives") && itemWeight > 0) {
            itemWeight = (-itemWeight);
            //Log.d(TAG, "Converted int " + itemWeight);
        } else if (arrayName.equals("Positives") && itemWeight < 0) {
            itemWeight *= (-1);
        }

        try {
            DataManager.addNewItem(getContext(), arrayName, nameValue, itemWeight);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    // Reset all UI fields and variables to their initial state.
    private void resetAll() {
        nameValue = "";
        itemWeight = 0;
        binding.itemNameEditText.getText().clear();
        binding.itemWeightEditText.getText().clear();
        Log.d(TAG, "Values have been reset.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}