package com.tschokkinen.doilikeithere;

import java.text.SimpleDateFormat;

/**
 * A class that handles date formatting.
 * @author Gavril Tschokkinen
 */

public class DateFormatters {
    public static final SimpleDateFormat sdfDMY =
            new SimpleDateFormat("dd/MM/yyyy");

    public static final SimpleDateFormat sdfCompleteDate =
            new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
}
