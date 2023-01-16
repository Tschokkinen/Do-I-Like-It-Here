package com.example.doilikeithere;

import android.util.Log;

public class ReviewItem {
    public String location;
    public String positives;
    public String negatives;
    public String feelings;
    public int score;

    public ReviewItem(String location, String positives, String negatives, String feelings, int score) {
        this.location = location;
        this.positives = positives;
        this.negatives = negatives;
        this.feelings = feelings;
        this.score = score;
    }
}
