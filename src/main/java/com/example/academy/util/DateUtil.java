package com.example.academy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

   private final static DateTimeFormatter FORMATTER= DateTimeFormatter.ofPattern("dd-MM-yyyy");

   public static String formatDate(LocalDateTime localDateTime){
       return localDateTime.format(FORMATTER);
   }

}
