package com.tschokkinen.doilikeithere.database;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.tschokkinen.doilikeithere.DateFormatters;
import com.tschokkinen.doilikeithere.models.ReviewItem;
import com.tschokkinen.doilikeithere.models.SelectionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.SelectionKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class DataManager {

    public enum TempArrays {
        TEMP_POSITIVES,
        TEMP_NEGATIVES,
        TEMP_FEELINGS
    }

    public enum DeleteCommands {
        DELETE_ENTIRE_DATABASE,
        DELETE_REVIEWS,
        DELETE_ONE
    }

    public static final String positives = "Positives";
    public static final String negatives = "Negatives";
    public static final String feelings = "Feelings";
    public static final String reviews = "Reviews";
    public static final String location = "Location";
    // Location concerning the review.
    public static String locationValue = "";

    // Database filename.
    static final String FileName = "Database";

    // Log.d TAG.
    private static final String TAG = "DataManager";

    // Latest review score.
    public static int latestReviewScore = 0;

    //Temp arrays used when user is making a review.
    public static ArrayList<SelectionItem> tempPositives = new ArrayList<>();
    public static ArrayList<SelectionItem> tempNegatives = new ArrayList<>();
    public static ArrayList<SelectionItem> tempFeelings = new ArrayList<>();

    // Clear selected temp array.
    public static void clearTemps(TempArrays tempName) {
        switch (tempName) {
            case TEMP_POSITIVES:
                tempPositives.clear();
                break;
            case TEMP_NEGATIVES:
                tempNegatives.clear();
                break;
            case TEMP_FEELINGS:
                tempFeelings.clear();
                break;
            default:
                break;
        }
    }

    // Clears all tempArrays.
    public static void clearAllDataCollectionVariables() {
        clearTemps(TempArrays.TEMP_POSITIVES);
        clearTemps(TempArrays.TEMP_NEGATIVES);
        clearTemps(TempArrays.TEMP_FEELINGS);
        locationValue = "";
    }

    public static ArrayList<SelectionItem> getTempArray(String arrayName) {
        switch (arrayName) {
            case positives:
                return tempPositives;
            case negatives:
                return tempNegatives;
            case feelings:
                return tempFeelings;
        }
        Log.d(TAG, "checkTempArray returned null");
        return null;
    }

    // Initialize database with proper json arrays if file doesn't exist already.
    public static void initializeDatabase(Context context) throws JSONException, IOException {
        Log.d(TAG, "Initialize database");

        String[] jsonArrayNames = {positives, negatives, feelings, reviews};
        JSONObject newDatabase = new JSONObject();

        // Initialize Positives, Negatives, Feelings, and Reviews arrays.
        for(int i = 0; i < jsonArrayNames.length; i++) {
            JSONArray jsonArray = new JSONArray();
            newDatabase.put((String) Objects.requireNonNull(Array.get(jsonArrayNames, i)), jsonArray);
        }
        writeFile(context, newDatabase.toString());
    }

    // Read file.
    private static String readFile(Context context) throws IOException, JSONException {
        File file = new File(context.getFilesDir(), FileName);
        if (!file.exists()) {
            Log.d(TAG, "File doesn't exist.");
            initializeDatabase(context);
            file = new File(context.getFilesDir(), FileName);
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }

    // Write data to file.
    private static void writeFile(Context context, String userString) throws IOException {
        File file = new File(context.getFilesDir(), FileName);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(userString);
        bufferedWriter.close();
        Log.d(TAG, "writeFile: userString " + userString);
    }

    // Empty reviewsArray by replace existing array with an empty array.
    public static void deleteFromDatabase(Context context, DeleteCommands command, int position,
                                          String arrayName) throws JSONException, IOException {
        // Read file and convert to string.
        String response = readFile(context);

        // Convert response to a JSON object.
        JSONObject loadedJSONObject = new JSONObject(response);

        if(command == DeleteCommands.DELETE_REVIEWS) { // Delete all reviews
            // Create an empty JSONArray
            JSONArray emptyJSONArray = new JSONArray();
            loadedJSONObject.put(reviews, emptyJSONArray);
        } else if (command == DeleteCommands.DELETE_ENTIRE_DATABASE) { // Delete entire database
            // Create an empty JSONArray
            JSONArray emptyJSONArray = new JSONArray();
            loadedJSONObject.put(positives, emptyJSONArray);
            loadedJSONObject.put(negatives, emptyJSONArray);
            loadedJSONObject.put(feelings, emptyJSONArray);
            loadedJSONObject.put(reviews, emptyJSONArray);
        } else if (command == DeleteCommands.DELETE_ONE) { // Delete one item or review
            JSONArray jsonArray = loadedJSONObject.getJSONArray(arrayName);
            jsonArray.remove(position);
            loadedJSONObject.put(arrayName, jsonArray);
        }

        // Write JSON object back into the file.
        writeFile(context, loadedJSONObject.toString());
    }

    // Add new item to (positive, negative, or feeling) JSON file.
    // arrayName can be "positive", "negative", or "feelings".
    public static void addNewItem(Context context, String arrayName, String nameValue,
                                  int itemWeight) throws IOException, JSONException {
        // Read file and convert to string.
        String response = readFile(context);

        // Put response string to JSONObject and get array according to arrayName parameter.
        JSONObject loadedJSONObject = new JSONObject(response);
        JSONArray jsonArray = loadedJSONObject.getJSONArray(arrayName);

        // Check if item with the same name already exists.
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("Name").equals(nameValue)) {
                    return;
                }
            }
        }

        Log.d(TAG, String.valueOf(itemWeight));

        // Create new JSON object and put object into the selected jsonArray.
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Name", nameValue);
        jsonObject.put("Weight", itemWeight);
        jsonArray.put(jsonObject);

        // Put updated jsonArray in loadedJSON object according to arrayName parameter.
        loadedJSONObject.put(arrayName, jsonArray);

        // REWORK THIS SECTION ==>
        // Get temp array and append new item on it.
        ArrayList<SelectionItem> tempArray = getTempArray(arrayName);
        updateSelectionRecyclerviewItems(tempArray, tempArray.size()+1, nameValue, itemWeight);
        // <== REWORK THIS SECTION

        // Write appended JSON object back into the file.
        writeFile(context, loadedJSONObject.toString());
    }

    // REWORK THIS SECTION ==>
    // Update temp array.
    public static void updateSelectionRecyclerviewItems(ArrayList<SelectionItem> tempArray,
                                                        int id, String itemName, int itemWeight) {
        SelectionItem selectionItem = new SelectionItem(id, itemName, itemWeight);
        Log.d(TAG, String.valueOf(id));
        tempArray.add(selectionItem);
    }
    // <== REWORK THIS SECTION

    // Calculate place score by summing positives together and subtracting negatives from the total.
    private static int calculateScore(Context context) throws JSONException, IOException {
        int totalScore = 0;
        Log.d(TAG, "Calculate score");

        // Get all temp arrays.
        ArrayList<ArrayList<SelectionItem>> tempArrays = new ArrayList<>();
        tempArrays.add(tempPositives);
        tempArrays.add(tempNegatives);
        tempArrays.add(tempFeelings);

        for (ArrayList<SelectionItem> entry : tempArrays) {
            for (SelectionItem s : entry) {
                if (s.getHasBeenSelected()) {
                    int value = s.getWeight();
                    Log.d(TAG, "Value of current Weight " + value);
                    totalScore += value;
                }
            }
        }

        //Log.d(TAG, "Total score before return from calculateScore: " + totalScore);
        latestReviewScore = totalScore;
        return totalScore;
    }

    // Add a new place review into the database.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addNewReview(Context context, String arrayName) throws IOException, JSONException {
        // Read file and convert to string.
        String response = readFile(context);

        StringBuilder positivesListString = new StringBuilder();
        StringBuilder negativesListString = new StringBuilder();
        StringBuilder feelingsListString = new StringBuilder();

        // Get listStrings to an array.
        StringBuilder[] listStrings = new StringBuilder[]{
                positivesListString,
                negativesListString,
                feelingsListString
        };

        // Get all temp arrays.
        ArrayList<ArrayList<SelectionItem>> tempArrays = new ArrayList<>();
        tempArrays.add(tempPositives);
        tempArrays.add(tempNegatives);
        tempArrays.add(tempFeelings);

        for (int i = 0; i < listStrings.length; i++) {
            ArrayList<SelectionItem> currentTempArray = tempArrays.get(i); // Get current tempArray.
            Iterator<SelectionItem> it = currentTempArray.iterator(); // Create iterator
            ArrayList<SelectionItem> temp = new ArrayList<>(); // Create temp array for current.
            while (it.hasNext()) {
                SelectionItem s = it.next();
                if (s.getHasBeenSelected()) {
                    temp.add(s); // Get selected items to temp array
                }
            }

            it = temp.iterator();
            while (it.hasNext()) {
                SelectionItem s = it.next();
                listStrings[i].append(s.getName()); // Get name(s) of selected item(s).
                if (it.hasNext()) {
                    listStrings[i].append(", "); // Append ',' if not last item in array.
                }
            }
        }


        // Put response string to JSONObject and get array according to arrayName parameter.
        JSONObject loadedJSONObject = new JSONObject(response);
        JSONArray jsonArray = loadedJSONObject.getJSONArray(arrayName);

        // Calculate total score before creating a new entry.
        int totalScore = calculateScore(context);
        //Log.d(TAG, "Total score: " + totalScore);

        // Get current date.
        Date date = new Date();
        String dateAsString = DateFormatters.sdfCompleteDate.format(date);
        //Log.d(TAG, "DateAsString: " + dateAsString);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(location, locationValue);
            jsonObject.put(positives, positivesListString.toString());
            jsonObject.put(negatives, negativesListString.toString());
            jsonObject.put(feelings, feelingsListString.toString());
            jsonObject.put("Total score", totalScore);
            jsonObject.put("Date", dateAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);

        // Put updated jsonArray in loadedJSON object according to arrayName parameter.
        loadedJSONObject.put(arrayName, jsonArray);

        clearAllDataCollectionVariables();

        // Write appended JSON object back into the file.
        writeFile(context, loadedJSONObject.toString());
    }

    // Load JSON data for positive, negative, and feelings selection.
    public static ArrayList<SelectionItem> loadSelectionRecyclerviewItems(Context context, String arrayName)
            throws IOException, JSONException {
        // Read data back from file.
        String response = readFile(context);

        // Put response string to JSONObject and get array according to arrayName parameter.
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray(arrayName);

        // Array for requested values.
        ArrayList<SelectionItem> requestedValues = new ArrayList<>();

        // Get values by key "Name".
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTemp = jsonArray.getJSONObject(i);
                //Log.d(TAG, jsonTemp.getString("Name"));
                SelectionItem current = new SelectionItem(
                        i,
                        jsonTemp.getString("Name"),
                        Integer.parseInt(jsonTemp.getString("Weight"))
                );
                requestedValues.add(current);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Assign requested values to a corresponding temp array.
        switch (arrayName) {
            case positives:
                tempPositives = requestedValues;
                //Log.d(TAG, String.valueOf(tempPositives.size()));
                //Log.d(TAG, "Returned positives");
                return tempPositives;
            case negatives:
                tempNegatives = requestedValues;
                //Log.d(TAG, String.valueOf(tempNegatives.size()));
                //Log.d(TAG, "Returned negatives");
                return tempNegatives;
            case feelings:
                tempFeelings = requestedValues;
                //Log.d(TAG, String.valueOf(tempFeelings.size()));
                //Log.d(TAG, "Returned feelings");
                return tempFeelings;
        }
        Log.d(TAG, "Returning null");
        return null;
    }

    // Load reviews from JSON file.
    public static ArrayList<ReviewItem> loadReviewsRecyclerviewItems(Context context)
            throws JSONException, IOException, ParseException {
        String response = readFile(context);

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray(reviews);

        // Array for requested values.
        ArrayList<ReviewItem> requestedValues = new ArrayList<>();

        // Get review data.
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTemp = jsonArray.getJSONObject(i);
                Date date = DateFormatters.sdfCompleteDate.parse(jsonTemp.getString("Date"));
                //Log.d(TAG, "Parsed date: " + date);
                ReviewItem reviewItem = new ReviewItem(
                        jsonTemp.getString(location),
                        jsonTemp.getString(positives),
                        jsonTemp.getString(negatives),
                        jsonTemp.getString(feelings),
                        Integer.parseInt(jsonTemp.getString("Total score")),
                        date
                );
                requestedValues.add(reviewItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, requestedValues.toString());

        return requestedValues;
    }
}