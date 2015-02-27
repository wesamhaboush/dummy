package com.codebreeze.incubator.xml;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

public class XmlBestPracticse {
    @Autowired
    private Marshaller marshaller;

    @Autowired
    private Unmarshaller unmarshaller;

    public static LocalDate parseLocalDate(final String localDateAsString){
        return LocalDate.parse(localDateAsString);
    }

    public static String printLocalDate(final LocalDate localDate){
        return localDate.toString();
    }

    public static LocalTime parseLocalTime(final String localTimeAsString){
        return LocalTime.parse(localTimeAsString);
    }

    public static String printLocalTime(final LocalTime localTime){
        return localTime.toString();
    }

    public static DateTime parseDateTime(final String dateTimeAsString){
        return DateTime.parse(dateTimeAsString);
    }

    public static String printDateTime(final DateTime dateTime){
        return dateTime.toString();
    }
}
