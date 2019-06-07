package util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConverterUtil {

    @NotNull
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000+0300");

    @NotNull
    public static String convertDateFormat(@NotNull final Date date) {
        return DEFAULT_DATE_FORMAT.format(date);
    }

    @Nullable
    public static Date convertFromStringToDate(@NotNull final String dateString) {
        try {
            return DEFAULT_DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}