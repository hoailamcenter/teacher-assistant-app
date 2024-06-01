package com.hcmute.teacher_assistant_app.helpers;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {

    private double min, max;

    // Constructor to initialize min and max values
    public InputFilterMinMax(String min, String max) {
        this.min = Double.parseDouble(min);
        this.max = Double.parseDouble(max);
    }

    // This method filters the input to restrict it within the specified range
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            // Concatenate the existing text and the new input to form a complete string
            double input = Double.parseDouble(dest.toString() + source.toString());
            // Check if the input is within the specified range
            if (isInRange(min, max, input))
                return null; // Input is valid, so return null (no changes)
        } catch (NumberFormatException nfe) { }
        // Input is invalid or out of range, so return an empty string (no changes)
        return "";
    }
    // This method checks if a value is within a specified range
    private boolean isInRange(double a, double b, double c) {
        // If b is greater than a, check if c is between a and b. Otherwise, check if c is between b and a.
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}