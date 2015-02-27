package com.codebreeze.incubator.datetime;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.Locale;

import static org.joda.time.DateTime.now;

/**
 * * Do NOT use date/time api in your applications. Get rid of them immediately.
 * * Use dates when they make sense, and time when it makes sense. Also use DateTime when it does.
 * * Learn about ISO 1806. here is a link (http://en.wikipedia.org/wiki/ISO_8601)
 * * Produce and plan to parse iso 1806 dates. Do NOT bother with anything else unless necessary (gui?)
 * * Use JODA TIME for any java before java 8.
 * * Understand time zones and how they work (reference http://ayp-sd.blogspot.in/2012/12/joda-time-tutorial_7.html)
 * * watch for the month index difference
 *
 * let's have a look http://ayp-sd.blogspot.in/2012/12/joda-time-tutorial_7.html
 */
public class DateTimeBestPractices {

    private static void nowReadsBetter(){
        DateTime now1 = now(); //more intention revealing
        DateTime now2 = new DateTime(); //less intention revealing

        LocalDateTime now3 = LocalDateTime.now();
        LocalDateTime now4 = new LocalDateTime();

        LocalDate today1 = new LocalDate();
        LocalDate today2 = LocalDate.now();

        LocalTime now5 = new LocalTime();
        LocalTime now6 = new LocalTime();
    }

    private static void fluencyOfApi(){
        DateTime yesterday = now().minusDays(1); //more intention revealing
        DateTime nextYearYesterday = now().minusDays(1).plusYears(1); //more intention revealing
    }

    private static void parsingAndPrintingIso(){
        DateTime dateTime1 = DateTime.parse("iso datetime");
        DateTime dateTime2 = DateTime.parse("specific date time format", DateTimeFormat.forPattern("HH:MM:whatever"));
        String dateTime1Text1 = DateTimeFormat.fullDate().print(dateTime1);
        String dateTime1Text2 = DateTimeFormat.mediumDateTime().print(dateTime2);
        ISODateTimeFormat.hour().print(dateTime1);
        ISODateTimeFormat.yearMonthDay().print(dateTime1);
    }

    private static void compatibilityWithDateFromjava(){
        DateTime dateTime1 = new DateTime(new Date());
        Date date = now().toDate();
    }

    private static void formattedPrinting(){
        DateTime dateTime = new DateTime(2012, 12, 1, 11, 30,
                DateTimeZone.UTC);

        System.out.println(ISODateTimeFormat.yearMonthDay().print(dateTime));
        System.out.println(ISODateTimeFormat.hourMinuteSecond()
                .print(dateTime));
        System.out.println(ISODateTimeFormat.dateHourMinuteSecond()
                .print(dateTime));
//        2012-12-01
//        11:30:00
//        2012-12-01T11:30:00
    }

    private static void printWithLocale(){
        DateTime dateTime = new DateTime(2012, 12, 1, 12, 15,
                DateTimeZone.UTC);

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy/MMM/dd");
        DateTimeFormatter frenchFormat = format.withLocale(Locale.FRENCH);
        DateTimeFormatter germanFormat = format.withLocale(Locale.GERMAN);
        System.out.println(format.print(dateTime));
        System.out.println(frenchFormat.print(dateTime));
        System.out.println(germanFormat.print(dateTime));
//        2012/Dec/01
//        2012/déc./01
//        2012/Dez/01
    }

    private static void timeZoneHandlingFromOneToAnother(){
        DateTimeZone localTimeZone = DateTimeZone.forID("Europe/Kiev");

        DateTime utcTime = new DateTime(2012, 11, 29, 11, 40,
                DateTimeZone.UTC);
        DateTime localTime = utcTime.withZone(localTimeZone);

        System.out.println(utcTime);
        System.out.println(localTime);
//        2012-11-29T11:40:00.000Z
//        2012-11-29T13:40:00.000+02:00
    }

    private static void timeZoneHandlingReverseConversion(){
        DateTimeZone localTimeZone = DateTimeZone.forID("Europe/Kiev");

        DateTime localTime = new DateTime(2012, 11, 29, 13, 40,
                localTimeZone);
        DateTime utcTime = localTime.withZone(DateTimeZone.UTC);

        System.out.println(localTime);
        System.out.println(utcTime);
//        2012-11-29T13:40:00.000+02:00
//        2012-11-29T11:40:00.000Z
    }

    private static void dateOnlyTimeStamp(){
        DateTime startDate = new DateTime(2012, 11, 5, 10, 15,
                DateTimeZone.UTC).withTimeAtStartOfDay();
        DateTime endDate = new DateTime(2012, 11, 5 + 1, 10, 15,
                DateTimeZone.UTC).withTimeAtStartOfDay().minusSeconds(1);
    }

    private static void dateArithmetic(){
        DateTime newYear = new DateTime(DateTimeZone.UTC).dayOfYear()
                .withMaximumValue().plusDays(1);

        DateTime firstWednesday = newYear.plusDays(
                DateTimeConstants.WEDNESDAY - newYear.getDayOfWeek());
    }

    private static void dateTimeAssembly(){
        LocalDate partialDate = new LocalDate(2012, 12, 3); //only date
        LocalTime partialTime = new LocalTime(12, 50); //only time
        DateTime dateTime = partialDate.toDateTime(partialTime,
                DateTimeZone.UTC); // date, time, timezone!

        LocalTime partialTime1 = new LocalTime(23, 30);
        DateTime dateTime1 = partialTime.toDateTime(
                new DateTime(DateTimeZone.UTC));

        YearMonth expirationDate = new YearMonth(2014, 12);

        String expirationDay = expirationDate.toDateTime(
                new DateTime(DateTimeZone.UTC).dayOfMonth().withMinimumValue())
                .dayOfWeek().getAsText();

        MonthDay birthday = new MonthDay(7, 1);

        String dayOfWeek = birthday.toDateTime(new DateTime(DateTimeZone.UTC)
                .plusYears(1)).dayOfWeek().getAsText();

        DateTime instant = new DateTime(2012, 12, 21, 8, 15,
                DateTimeZone.UTC);

        LocalDate partialDate2 = new LocalDate(instant);
        LocalTime partialTime2 = new LocalTime(instant);
        LocalDateTime partialDateTime2 = new LocalDateTime(instant);
        YearMonth yearMonth = new YearMonth(instant);
        MonthDay monthDay = new MonthDay(instant);

        System.out.println(partialDate.toString(
                ISODateTimeFormat.yearMonthDay()));
        System.out.println(partialTime.toString(
                ISODateTimeFormat.hourMinute()));
        System.out.println(partialDateTime2.toString("yyyy.MM.dd/HH:mm:ss"));
        System.out.println(yearMonth.toString("yyyy\\MMM"));
        System.out.println(monthDay.toString("dd-MMM"));
    }

    private static void intervalWork(){
        DateTime today = new DateTime(DateTimeZone.UTC);

        Interval firstMeeting = new Interval(
                new LocalTime(9, 0).toDateTime(today),
                new LocalTime(10, 0).toDateTime(today));

        Interval coffeeBreak = new Interval(
                new LocalTime(10, 0).toDateTime(today),
                new LocalTime(10, 30).toDateTime(today));

        Interval secondMeeting = new Interval(
                new LocalTime(10, 30).toDateTime(today),
                new LocalTime(12, 0).toDateTime(today));

        Interval lunchTime = new Interval(
                new LocalTime(13, 0).toDateTime(today),
                new LocalTime(14, 0).toDateTime(today));

        Interval thirdMeeting = new Interval(
                new LocalTime(16, 0).toDateTime(today),
                new LocalTime(18, 0).toDateTime(today));

        Interval sportEvent = new Interval(
                new LocalTime(17, 0).toDateTime(today),
                new LocalTime(19, 0).toDateTime(today));

        System.out.printf(
                "First meeting abuts coffee break: %b\n",
                firstMeeting.abuts(coffeeBreak));
        System.out.printf(
                "Lunch time is before second meeting: %b\n",
                lunchTime.isBefore(secondMeeting));
        System.out.printf(
                "Sport event is after the lunch: %b\n",
                sportEvent.isAfter(lunchTime));
        System.out.printf(
                "Sport event overlaps with the third meeting: %b\n",
                sportEvent.overlaps(thirdMeeting));

        Interval gap = thirdMeeting.gap(lunchTime);
        Interval overlap = thirdMeeting.overlap(sportEvent);

        System.out.printf(
                "I will miss the part of sport event: from %s to %s\n",
                ISODateTimeFormat.hourMinute().print(overlap.getStart()),
                ISODateTimeFormat.hourMinute().print(overlap.getEnd()));

        System.out.printf(
                "I have spare time after lunch: from %s to %s\n",
                ISODateTimeFormat.hourMinute().print(gap.getStart()),
                ISODateTimeFormat.hourMinute().print(gap.getEnd()));
        /**
         First meeting abuts coffee break: true
         Lunch time is before second meeting: false
         Sport event is after the lunch: true
         Sport event overlaps with the third meeting: true
         I will miss the part of sport event: from 17:00 to 18:00
         I have spare time after lunch: from 14:00 to 16:00
         */
    }
}
