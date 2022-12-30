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
    static final String FileName = "Database";

    public static ArrayList<String> tempPositives = new ArrayList<>();
    public static ArrayList<String> tempNegatives = new ArrayList<>();
    public static ArrayList<String> tempFeelings = new ArrayList<>();
    public static ArrayList<String> selected = new ArrayList<>();

    // Clear temp arrays.
    public static void clearTemps(String tempName) {
        switch (tempName) {
            case "tempPositives":
                tempPositives.clear();
                break;
            case "tempNegatives":
                tempNegatives.clear();
                break;
            case "tempFeelings":
                tempFeelings.clear();
                break;
            default:
                break;
        }
    }

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
                    jsonObject.put("Weight", "None");
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
            Log.d("DataManager", "File doesn't exist.");
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
        Log.d("DataManager", "writeFile: userString " + userString);
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

    // Add a new place review into the database.
    public static void addNewReview(Context context, String arrayName) throws IOException, JSONException {
        // Read file and convert to string.
        String response = readFile(context);

        // HARD CODED FOR TESTING. MAKE ARRAY DYNAMIC.
        // Convert tempPositives to String
        String listString = String.join(", ", tempPositives);

        // Put response string to JSONObject and get array according to arrayName parameter.
        JSONObject loadedJSONObject = new JSONObject(response);
        JSONArray jsonArray = loadedJSONObject.getJSONArray(arrayName);

        // CALCULATE TOTAL SCORE BEFORE CREATING NEW ENTRY.
        JSONObject jsonObject = new JSONObject();
        // HARD CODED VALUES USED FOR TESTING PURPOSES.
        try {
            jsonObject.put("Location", "Hesa");
            jsonObject.put("Positives", listString);
            jsonObject.put("Negatives", "None");
            jsonObject.put("Feelings", "None");
            jsonObject.put("Total score", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);

        // Put updated jsonArray in loadedJSON object according to arrayName parameter.
        loadedJSONObject.put(arrayName, jsonArray);

        // Write appended JSON object back into the file.
        writeFile(context, loadedJSONObject.toString());
    }

    // Load JSON data for positive, negative, and feelings selection.
    public static ArrayList<String> loadRecyclerviewItems(Context context, String arrayName)
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
                Log.d("DataManager", jsonTemp.getString("Name"));
                requestedValues.add(jsonTemp.getString("Name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestedValues;
    }
}
