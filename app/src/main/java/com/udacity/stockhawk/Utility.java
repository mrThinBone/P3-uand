package com.udacity.stockhawk;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by DELL-INSPIRON on 3/29/2017.
 */

public class Utility {

    private static final DecimalFormat dollarFormatWithPlus;
    private static final DecimalFormat dollarFormat;
    private static final DecimalFormat percentageFormat;

    static {
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    public static String priceFormat(float val) {
        return dollarFormat.format(val);
    }

    public static String dollarFormatWithPlus(float val) {
        return dollarFormatWithPlus.format(val);
    }

    public static String percentageFormat(float val) {
        return percentageFormat.format(val);
    }
}
