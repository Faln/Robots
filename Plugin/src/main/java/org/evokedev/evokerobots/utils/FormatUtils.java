package org.evokedev.evokerobots.utils;

import lombok.experimental.UtilityClass;

import java.text.NumberFormat;
import java.util.Locale;

@UtilityClass
public class FormatUtils {

    private static final NumberFormat FORMATTER = NumberFormat.getInstance(Locale.ENGLISH);
    private static final char[] PREFIXES = {' ', 'k', 'M', 'B', 'T', 'Q'};

    static {
        FORMATTER.setMaximumFractionDigits(2);
        FORMATTER.setMinimumFractionDigits(0);
    }

    public String formatComma(final int num) {
        return FormatUtils.FORMATTER.format(num);
    }

    public String formatComma(final double num) {
        return FormatUtils.FORMATTER.format(num);
    }

    public String format(int num) {
        if (num <= 0) {
            return String.valueOf(num);
        }

        int i = 0;
        while (num / 10 <= 10) {
            num /= 10;
            i++;
        }

        i = Math.min(i, FormatUtils.PREFIXES.length - 1);

        return String.valueOf(num) + FormatUtils.PREFIXES[i];
    }
}
