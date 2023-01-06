package com.example.doilikeithere;

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

import com.example.doilikeithere.databinding.FragmentSettingsBinding;

import org.json.JSONException;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private String TAG = "SettingsFragment";
    private FragmentSettingsBinding binding;
    private RadioGroup radioGroup;
    private String arrayName = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
               EditText itemName_editText = getView().findViewById(R.id.itemName_editText);
               String nameValue = itemName_editText.getText().toString();

               EditText itemWeight_editText = getView().findViewById(R.id.itemWeight_editText);
               int itemWeight = Integer.parseInt(itemWeight_editText.getText().toString());
               Log.d("SettingsFragment", String.valueOf(itemWeight));

               // If user entered positive value for a negative attribute, convert value to negative.
               if (arrayName.equals("Negatives") && itemWeight > 0) {
                   itemWeight = (-itemWeight);
                   //Log.d(TAG, "Converted int " + itemWeight);
               }

               if (!arrayName.isEmpty() && !nameValue.isEmpty()) {
                   try {
                       DataManager.addNewItem(getContext(), arrayName, nameValue, itemWeight);
                   } catch (IOException e) {
                       e.printStackTrace();
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   NavHostFragment.findNavController(SettingsFragment.this)
                           .navigate(R.id.action_SettingsFragment_to_MainPageFragment);
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