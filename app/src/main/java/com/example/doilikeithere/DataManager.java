package com.example.doilikeithere;

import android.content.Context;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.Objects;

public class DataManager {

    public static enum TempArrays {
        TEMP_POSITIVES,
        TEMP_NEGATIVES,
        TEMP_FEELINGS
    }
    static final String FileName = "Database";
    private static final String TAG = "DataManager";

    public static String location = "";
    public static ArrayList<String> tempPositives = new ArrayList<>();
    public static ArrayList<String> tempNegatives = new ArrayList<>();
    public static ArrayList<String> tempFeelings = new ArrayList<>();
    public static ArrayList<String> selected = new ArrayList<>();

    // Clear temp arrays.
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

    // Called when a new review has been saved.
    private static void clearAllVariables() {
        clearTemps(TempArrays.TEMP_POSITIVES);
        clearTemps(TempArrays.TEMP_NEGATIVES);
        clearTemps(TempArrays.TEMP_FEELINGS);
        location = "";
    }

    // DO THIS INSIDE A TRY/CATCH STATEMENT????
    // Initialize database with proper json arrays if file doesn't exist already.
    public static void initializeDatabase(Context context) throws JSONException, IOException {
        Log.d("DataManager", "Initialize database");
        File file = new File(context.getFilesDir(), FileName);

        if (!file.exists()) {
            Log.d("DataManager", "File doesn't exist.");
            String[] jsonArrayNames = {"Positives", "Negatives", "Feelings", "Reviews"};
            JSONObject newDatabase = new JSONObject();

            // Initialize Positives, Negatives, and Feelings arrays.
            for(int i = 0; i < jsonArrayNames.length-1; i++) {
                JSONObject jsonObject = new JSONObject();

                // Values used for testing. REMOVE when testing done.
                try {
                    jsonObject.put("Name", "None");
                    jsonObject.put("Weight", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                newDatabase.put((String) Objects.requireNonNull(Array.get(jsonArrayNames, i)), jsonArray);
            }
            // Initialize Reviews array.
            {
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("Location", "None");
                    jsonObject.put("Positives", "None");
                    jsonObject.put("Negatives", "None");
                    jsonObject.put("Feelings", "None");
                    jsonObject.put("Total score", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                String nameOfLastArrayElement = jsonArrayNames[jsonArrayNames.length-1];
                newDatabase.put(nameOfLastArrayElement, jsonArray);
            }
            writeFile(context, newDatabase.toString());
        }
    }

    // Read file.
    private static String readFile(Context context) throws IOException, JSONException {
        File file = new File(context.getFilesDir(), FileName);
        if (!file.exists()) {
            Log.d(TAG, "File doesn't exist.");
            initializeDatabase(context);
            file = new File(context.getFilesDir(), FileName);
        }
        if (!file.exists()) {
            Log.d(TAG, "File doesn't exist.");
            writeFile(context, "");
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
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("Name").equals(nameValue)) {
                return;
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
        // JSON ARRAY NAME HARD CODED FOR TESTING PURPOSES. MAKE DYNAMIC (e.g. String[]).
        JSONObject jsonObject = new JSONObject(response);

        JSONArray jsonArray = jsonObject.getJSONArray("Positives");
        totalScore = calculate(jsonArray, tempPositives, totalScore);

        jsonArray = jsonObject.getJSONArray("Negatives");
        totalScore = calculate(jsonArray, tempNegatives, totalScore);

        jsonArray = jsonObject.getJSONArray("Feelings");
        totalScore = calculate(jsonArray, tempFeelings, totalScore);

        Log.d(TAG, "Total score before return from calculateScore: " + totalScore);
        return totalScore;
    }

    // NOTE TO SELF: Combine this to calculateScore method.
    private static int calculate (JSONArray jsonArray, ArrayList<String> tempArray, int totalScore)
                throws JSONException {
        // Get Weight of each temp item.
        // Ignore multiple entries with same name by breaking loop after first one is found.
        // Not the most elegant solution, but works for now. :)
        for (String s : tempArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject current = jsonArray.getJSONObject(i);
                if (current.getString("Name").equals(s)) {
                    int value = current.getInt("Weight");
                    Log.d(TAG, "Value of calculate total score " + value);
                    totalScore += value;
                    break;
                }
            }
        }
        return totalScore;
    }


    // Add a new place review into the database.
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

        // CALCULATE TOTAL SCORE BEFORE CREATING NEW ENTRY.

        int totalScore = calculateScore(context);
        Log.d(TAG, "Total score: " + totalScore);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Location", location);
            jsonObject.put("Positives", positivesListString);
            jsonObject.put("Negatives", negativesListString);
            jsonObject.put("Feelings", feelingsListString);
            jsonObject.put("Total score", totalScore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);

        // Put updated jsonArray in loadedJSON object according to arrayName parameter.
        loadedJSONObject.put(arrayName, jsonArray);

        clearAllVariables();

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
        // Start index from 1 to exclude empty initialization value.
        // Fix file initialization in the long run so that the initialization value gets
        // overwritten by the first addNewItem object. :)
        try {
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonTemp = jsonArray.getJSONObject(i);
                Log.d(TAG, jsonTemp.getString("Name"));
                requestedValues.add(jsonTemp.getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestedValues;
    }

    public static ArrayList<String> loadReviewsRecyclerviewItems(Context context)
            throws JSONException, IOException {
        String response = readFile(context);

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("Reviews");

        // Array for requested values.
        ArrayList<String> requestedValues = new ArrayList<>();

        // Get review data.
        // STREAMLINE THIS PROSES IN THE LONG RUN.
        try {
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonTemp = jsonArray.getJSONObject(i);
                //Log.d("DataManager", jsonTemp.getString("Location"));
                String result = "";
                result += "Location: ";
                result += jsonTemp.getString("Location");
                result += " ";
                result += "Positives: ";
                result += jsonTemp.getString("Positives");
                result += " ";
                result += "Negatives: ";
                result += jsonTemp.getString("Negatives");
                result += " ";
                result += "Feelings: ";
                result += jsonTemp.getString("Feelings");
                result += " ";
                result += "Total score: ";
                result += jsonTemp.getString("Total score");
                requestedValues.add(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, requestedValues.toString());

        return requestedValues;
    }
}
