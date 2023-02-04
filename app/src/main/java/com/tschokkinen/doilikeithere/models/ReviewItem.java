package com.tschokkinen.doilikeithere.models;

import java.util.Date;

public class ReviewItem {
    public String location;
    public String positives;
    public String negatives;
    public String feelings;
    public int score;
    public Date date;

    public ReviewItem(String location, String positives, String negatives, String feelings,
                      int score, Date date) {
        this.location = location;
        this.positives = positives;
        this.negatives = negatives;
        this.feelings = feelings;
        this.score = score;
        this.date = date;
    }
}
