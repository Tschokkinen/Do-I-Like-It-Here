package com.tschokkinen.doilikeithere.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tschokkinen.doilikeithere.Alert;
import com.tschokkinen.doilikeithere.database.DataManager;
import com.tschokkinen.doilikeithere.R;
import com.tschokkinen.doilikeithere.databinding.FragmentSettingsBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private String TAG = "SettingsFragment";
    private FragmentSettingsBinding binding;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add new item to database.
        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingsFragment.this)
                        .navigate(R.id.action_settingsFragment_to_addNewItem);
            }
        });

        // Empty reviews from database.
        binding.emptyReviewsDatabase.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DialogFragment alert = new Alert("Empty reviews from database",
                       DataManager.DeleteCommands.DELETE_REVIEWS);
               alert.show(getParentFragmentManager(),"Empty reviews from database");
           }
        });

        // Empty entire database
        binding.emptyEntireDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment alert = new Alert("Empty entire database",
                        DataManager.DeleteCommands.DELETE_ENTIRE_DATABASE);
                alert.show(getParentFragmentManager(), "Empty entire database");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}