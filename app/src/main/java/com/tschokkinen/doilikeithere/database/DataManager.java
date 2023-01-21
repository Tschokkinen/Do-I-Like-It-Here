package com.tschokkinen.doilikeithere.database;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.tschokkinen.doilikeithere.DateFormatters;
import com.tschokkinen.doilikeithere.models.ReviewItem;

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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataManager {

    public enum TempArrays {
        TEMP_POSITIVES,
        TEMP_NEGATIVES,
        TEMP_FEELINGS
    }

    // Database filename.
    static final String FileName = "Database";

    // Log.d TAG.
    private static final String TAG = "DataManager";

    // Latest review score.
    public static int latestReviewScore = 0;

    //Temp arrays used when user is making a review.
    public static ArrayList<String> tempPositives = new ArrayList<>();
    public static ArrayList<String> tempNegatives = new ArrayList<>();
    public static ArrayList<String> tempFeelings = new ArrayList<>();

    // Location concerning the review.
    public static String location = "";

    // Selected items from the recycler view.
    public static ArrayList<String> selected = new ArrayList<>();

    // Used when calculating the review score.
    private static final Map<String, ArrayList<String>> tempArrays =
            new HashMap<String, ArrayList<String>>() {
        {
            put("Positives", tempPositives);
            put("Negatives", tempNegatives);
            put("Feelings", tempFeelings);
        }
    };

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
        location = "";
    }

    // Initialize database with proper json arrays if file doesn't exist already.
    public static void initializeDatabase(Context context) throws JSONException, IOException {
        Log.d(TAG, "Initialize database");

        String[] jsonArrayNames = {"Positives", "Negatives", "Feelings", "Reviews"};
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

        // Write appended JSON object back into the file.
        writeFile(context, loadedJSONObject.toString());
    }

    // Calculate place score by summing positives together and subtracting negatives from the total.
    private static int calculateScore(Context context) throws JSONException, IOException {
        int totalScore = 0;

        // Read file and convert to string.
        String response = readFile(context);

        // Put response string to JSONObject and get array according to arrayName parameter.
        JSONObject jsonObject = new JSONObject(response);

        // Get current JsonArray name (Positives, Negatives, Feelings) and corresponding tempArrayList.
        // Then get Weight of each temp item.
        // Ignore multiple entries with same name by breaking loop after first one is found.
        for (Map.Entry<String, ArrayList<String>> entry : tempArrays.entrySet()) {
            JSONArray jsonArray = jsonObject.getJSONArray(entry.getKey());
            for (String s : entry.getValue()) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject current = jsonArray.getJSONObject(i);
                    //Log.d(TAG, current.getString("Name"));
                    if (current.getString("Name").equals(s)) {
                        int value = current.getInt("Weight");
                        //Log.d(TAG, "Value of current Weight " + value);
                        totalScore += value;
                        break;
                    }
                }
            }
        } // Not the most elegant solution with tons of nesting, but works for now. :)

        //Log.d(TAG, "Total score before return from calculateScore: " + totalScore);
        latestReviewScore = totalScore;
        return totalScore;
    }

    // Add a new place review into the database.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addNewReview(Context context, String arrayName) throws IOException, JSONException {
        // Read file and convert to string.
        String response = readFile(context);

        // Convert tempArrays to Strings
        String positivesListString = String.join(", ", tempPositives);
        String negativesListString = String.join(", ", tempNegatives);
        String feelingsListString = String.join(", ", tempFeelings);

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
            jsonObject.put("Location", location);
            jsonObject.put("Positives", positivesListString);
            jsonObject.put("Negatives", negativesListString);
            jsonObject.put("Feelings", feelingsListString);
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
    public static ArrayList<String> loadSelectionRecyclerviewItems(Context context, String arrayName)
            throws IOException, JSONException {
        // Read data back from file.
        String response = readFile(context);

        // Put response string to JSONObject and get array according to arrayName parameter.
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray(arrayName);

        // Array for requested values.
        ArrayList<String> requestedValues = new ArrayList<>();

        // Get values by key "Name".
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTemp = jsonArray.getJSONObject(i);
                Log.d(TAG, jsonTemp.getString("Name"));
                requestedValues.add(jsonTemp.getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestedValues;
    }

    // Load reviews from JSON file.
    public static ArrayList<ReviewItem> loadReviewsRecyclerviewItems(Context context)
            throws JSONException, IOException, ParseException {
        String response = readFile(context);

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("Reviews");

        // Array for requested values.
        ArrayList<ReviewItem> requestedValues = new ArrayList<>();


        // Get review data.
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTemp = jsonArray.getJSONObject(i);
                Date date = DateFormatters.sdfCompleteDate.parse(jsonTemp.getString("Date"));
                //Log.d(TAG, "Parsed date: " + date);
                ReviewItem reviewItem = new ReviewItem(
                        jsonTemp.getString("Location"),
                        jsonTemp.getString("Positives"),
                        jsonTemp.getString("Negatives"),
                        jsonTemp.getString("Feelings"),
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