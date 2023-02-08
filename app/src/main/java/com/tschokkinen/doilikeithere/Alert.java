package com.tschokkinen.doilikeithere;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.tschokkinen.doilikeithere.database.DataManager;

import org.json.JSONException;

import java.io.IOException;


public class Alert extends DialogFragment {
    private String message;
    private DataManager.DeleteCommands command;
    private int position;
    private String arrayName;

    public Alert(String message, DataManager.DeleteCommands command, int position, String arrayName) {
        this.message = message;
        this.command = command;
        this.position = position;
        this.arrayName = arrayName;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Empty review database.
                        try {
                            DataManager.deleteFromDatabase(getContext(), command, position, arrayName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}