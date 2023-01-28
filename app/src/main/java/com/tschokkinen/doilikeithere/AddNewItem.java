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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewItem extends Fragment {
    private String TAG = "SettingsFragment";
    private FragmentAddNewItemBinding binding;
    private RadioGroup radioGroup;
    private String arrayName = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddNewItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //radioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = getView().findViewById(i);
                //Log.d(TAG, radioButton.getText().toString());

                // Get arrayName by radio button selection.
                arrayName = radioButton.getTag().toString();
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText itemName_editText = getView().findViewById(R.id.itemName_editText);
                String nameValue = binding.itemNameEditText.getText().toString();

                //EditText itemWeight_editText = getView().findViewById(R.id.itemWeight_editText);
                int itemWeight = Integer.parseInt(binding.itemWeightEditText.getText().toString());
                Log.d("SettingsFragment", String.valueOf(itemWeight));

                // If user entered positive value for a negative attribute, convert value to negative.
                if (arrayName.equals("Negatives") && itemWeight > 0) {
                    itemWeight = (-itemWeight);
                    //Log.d(TAG, "Converted int " + itemWeight);
                } else if (arrayName.equals("Positives") && itemWeight < 0) {
                    itemWeight *= (-1);
                }

                if (!arrayName.isEmpty() && !nameValue.isEmpty()) {
                    try {
                        DataManager.addNewItem(getContext(), arrayName, nameValue, itemWeight);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}