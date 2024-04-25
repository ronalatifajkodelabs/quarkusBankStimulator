package quarkus.bank.stimulator.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    public static double formatToTwoDecimals(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    public static LocalDateTime createLocalDateTimeFromString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }
}
