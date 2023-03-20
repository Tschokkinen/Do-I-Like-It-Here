package com.tschokkinen.doilikeithere.models;

import java.util.Date;

/**
 * Class for review items.
 * A new ReviewItem is instantiated when user saves a review.
 * @author Gavril Tschokkinen
 */

public class ReviewItem {
    public String location;
    public String positives;
    public String negatives;
    public String feelings;
    public int score;
    public Date date;

    /**
     * ReviewItem constructor.
     *
     * @param  location  Location given by the user.
     * @param  positives  Positives listed by the user (an array converted to string).
     * @param  negatives  Negatives listed by the user (an array converted to string).
     * @param  feelings  Feelings listed by the user (an array converted to string).
     * @param  score  Total score calculated for the named location based on positives, negatives,
     *                and feelings.
     * @param  date  Date of the review.
     */
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
