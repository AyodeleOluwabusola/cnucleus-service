package com.coronation.nucleus.util;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

/**
 * @author toyewole
 */
@UtilityClass
public class Constants {
    public final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public final String FINAL = "FINAL";
}
