package com.codebreeze.incubator.datetime;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.util.*;

import static org.joda.time.DateTime.now;

/**
 * * Do NOT use date/time api in your applications. Get rid of them immediately.
 * * Use dates when they make sense, and time when it makes sense. Also use DateTime when it does.
 * * Learn about ISO 1806. here is a link (http://en.wikipedia.org/wiki/ISO_8601)
 * * Produce and plan to parse iso 1806 dates. Do NOT bother with anything else unless necessary (gui?)
 * * Use JODA TIME for any java before java 8.
 * * Understand time zones and how they work (reference http://ayp-sd.blogspot.in/2012/12/joda-time-tutorial_7.html)
 * * watch for the month index difference
 * <p/>
 * let's have a look http://ayp-sd.blogspot.in/2012/12/joda-time-tutorial_7.html
 * * java 8 changed the rules of the games, so be read for the new API
 */
public class DateTimeBestPracticesTest {

    @Test
    public void nowReadsBetter() {
        DateTime now1 = now(); //more intention revealing
        DateTime now2 = new DateTime(); //less intention revealing

        LocalDateTime now3 = LocalDateTime.now();
        LocalDateTime now4 = new LocalDateTime();

        LocalDate today1 = new LocalDate();
        LocalDate today2 = LocalDate.now();

        LocalTime now5 = new LocalTime();
        LocalTime now6 = new LocalTime();
    }

    @Test
    public void fluencyOfApi() {
        DateTime yesterday = now().minusDays(1); //more intention revealing
        DateTime nextYearYesterday = now().minusDays(1).plusYears(1); //more intention revealing
    }

    @Test
    public void parsingAndPrintingIso() {
        DateTime dateTime1 = DateTime.parse("2009-10-11");
        DateTime dateTime2 = DateTime.parse("2009.10.11", DateTimeFormat.forPattern("yyyy.MM.dd"));
        String dateTime1Text1 = DateTimeFormat.fullDate().print(dateTime1);
        String dateTime1Text2 = DateTimeFormat.mediumDateTime().print(dateTime2);
        System.out.println(ISODateTimeFormat.basicDate().print(dateTime1));
        System.out.println(ISODateTimeFormat.hour().print(dateTime1));
        System.out.println(ISODateTimeFormat.yearMonthDay().print(dateTime1));

        System.out.println(ISODateTimeFormat.basicDate().print(dateTime2));
        System.out.println(ISODateTimeFormat.hour().print(dateTime2));
        System.out.println(ISODateTimeFormat.yearMonthDay().print(dateTime2));
//        20091011
//        00
//        2009-10-11
//        20091011
//        00
//        2009-10-11
    }

    @Test
    public void formattedPrinting() {
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

    @Test
    public void printWithLocale() {
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

    @Test
    public void uniqeFormatterFluentlyCreated(){
        DateTime dateTime = new DateTime(2012, 12, 1, 12, 15,
                DateTimeZone.UTC);

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendDayOfWeekShortText()
                .appendLiteral(", ")
                .appendDayOfMonth(2)
                .appendLiteral('-')
                .appendMonthOfYearShortText()
                .appendLiteral('-')
                .appendYear(4, 4)
                .appendLiteral(", ")
                .appendEraText()
                .toFormatter();

        System.out.println(formatter.print(dateTime));
        //Sat, 01-Dec-2012, AD
    }

    @Test
    public void parsingWithFormatters(){
        String strDateTime = "01.12.2012";
        DateTimeFormatter formatter = DateTimeFormat
                .forPattern("dd.MM.yyyy");
        DateTime dateTime = formatter.parseDateTime(strDateTime);

        System.out.println(strDateTime);
        System.out.println(ISODateTimeFormat.yearMonthDay()
                .print(dateTime));
//        01.12.2012
//        2012-12-01
    }

    @Test
    public void timeZoneHandlingFromOneToAnother() {
        DateTimeZone localTimeZone = DateTimeZone.forID("Europe/Kiev");

        DateTime utcTime = new DateTime(2012, 11, 29, 11, 40,
                DateTimeZone.UTC);
        DateTime localTime = utcTime.withZone(localTimeZone);

        System.out.println(utcTime);
        System.out.println(localTime);
//        2012-11-29T11:40:00.000Z
//        2012-11-29T13:40:00.000+02:00
    }

    @Test
    public void timeZoneHandlingReverseConversion() {
        DateTimeZone localTimeZone = DateTimeZone.forID("Europe/Kiev");

        DateTime localTime = new DateTime(2012, 11, 29, 13, 40,
                localTimeZone);
        DateTime utcTime = localTime.withZone(DateTimeZone.UTC);

        System.out.println(localTime);
        System.out.println(utcTime);
//        2012-11-29T13:40:00.000+02:00
//        2012-11-29T11:40:00.000Z
    }

    @Test
    public void dateOnlyTimeStamp() {
        DateTime startDate = new DateTime(2012, 11, 5, 10, 15,
                DateTimeZone.UTC).withTimeAtStartOfDay();
        DateTime endDate = new DateTime(2012, 11, 5 + 1, 10, 15,
                DateTimeZone.UTC).withTimeAtStartOfDay().minusSeconds(1);

        //convert to sql dates?
        java.sql.Date startDateAsSql = new java.sql.Date(startDate.getMillis());
        java.sql.Date endDateAsSql = new java.sql.Date(endDate.getMillis());

        //convert to java dates?
        java.util.Date startDateAsJavaDates = new java.util.Date(startDate.getMillis());
        java.util.Date endDateAsJavaDates = new java.util.Date(endDate.getMillis());
        java.util.Date startDateAsJavaDates1 = startDate.toDate();
        java.util.Date endDateAsJavaDates1 = endDate.toDate();
    }

    @Test
    public void dateArithmetic() {
        DateTime newYear = new DateTime(DateTimeZone.UTC).dayOfYear()
                .withMaximumValue().plusDays(1);

        DateTime firstWednesday = newYear.plusDays(
                DateTimeConstants.WEDNESDAY - newYear.getDayOfWeek());

        System.out.println(ISODateTimeFormat.dateHourMinuteSecond()
                .print(firstWednesday));
        //2013-01-02T15:01:13
    }

    @Test
    public void dateTimeAssemblyFromDateAndTime() {
        LocalDate partialDate = new LocalDate(2012, 12, 3);
        LocalTime partialTime = new LocalTime(12, 50);
        DateTime dateTime = partialDate.toDateTime(partialTime,
                DateTimeZone.UTC);

        System.out.println(ISODateTimeFormat.dateHourMinuteSecond()
                .print(dateTime));
        //2012-12-03T12:50:00
    }

    @Test
    public void dateTimeAssemblyFromLocalTimeAndDateTime() {
        LocalTime partialTime = new LocalTime(23, 30);
        DateTime dateTime = partialTime.toDateTime(
                new DateTime(DateTimeZone.UTC));

        System.out.println(ISODateTimeFormat.dateHourMinuteSecond()
                .print(dateTime));
        //2012-12-03T23:30:00
    }

    @Test
    public void determineDayOfWeekViaYearMonth() {
        YearMonth expirationDate = new YearMonth(2014, 12);

        String expirationDay = expirationDate.toDateTime(
                new DateTime(DateTimeZone.UTC).dayOfMonth().withMinimumValue()) //minimum value not 1st?
                .dayOfWeek().getAsText();

        System.out.println(expirationDay);
        //Monday
    }

    @Test
    public void determineDayOfWeekViaMonthDay() {
        MonthDay birthday = new MonthDay(7, 1);

        String dayOfWeek = birthday.toDateTime(new DateTime(DateTimeZone.UTC)
                .plusYears(1)).dayOfWeek().getAsText();

        System.out.println(dayOfWeek);
        //Monday
    }

    @Test
    public void partialToInstantExample() {
        DateTime instant = new DateTime(2012, 12, 21, 8, 15,
                DateTimeZone.UTC);

        LocalDate partialDate = new LocalDate(instant);
        LocalTime partialTime = new LocalTime(instant);
        LocalDateTime partialDateTime = new LocalDateTime(instant);
        YearMonth yearMonth = new YearMonth(instant);
        MonthDay monthDay = new MonthDay(instant);

        System.out.println(partialDate.toString(
                ISODateTimeFormat.yearMonthDay()));
        System.out.println(partialTime.toString(
                ISODateTimeFormat.hourMinute()));
        System.out.println(partialDateTime.toString("yyyy.MM.dd/HH:mm:ss"));
        System.out.println(yearMonth.toString("yyyy\\MMM"));
        System.out.println(monthDay.toString("dd-MMM"));
//        2012-12-21
//        08:15
//        2012.12.21/08:15:00
//        2012\Dec
//        21-Dec
    }

    @Test
    public void intervalWork() {
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

        System.out.printf("First meeting abuts coffee break: %b\n", firstMeeting.abuts(coffeeBreak));
        System.out.printf("Lunch time is before second meeting: %b\n", lunchTime.isBefore(secondMeeting));
        System.out.printf("Sport event is after the lunch: %b\n", sportEvent.isAfter(lunchTime));
        System.out.printf("Sport event overlaps with the third meeting: %b\n", sportEvent.overlaps(thirdMeeting));

        Interval gap = thirdMeeting.gap(lunchTime);
        Interval overlap = thirdMeeting.overlap(sportEvent);

        System.out.printf("I will miss the part of sport event: from %s to %s\n",
                ISODateTimeFormat.hourMinute().print(overlap.getStart()),
                ISODateTimeFormat.hourMinute().print(overlap.getEnd()));

        System.out.printf("I have spare time after lunch: from %s to %s\n",
                ISODateTimeFormat.hourMinute().print(gap.getStart()),
                ISODateTimeFormat.hourMinute().print(gap.getEnd()));
//         First meeting abuts coffee break: true
//         Lunch time is before second meeting: false
//         Sport event is after the lunch: true
//         Sport event overlaps with the third meeting: true
//         I will miss the part of sport event: from 17:00 to 18:00
//         I have spare time after lunch: from 14:00 to 16:00
    }

    @Test
    public void compareIntervalsBasedOnLength(){
        DateTime today = new DateTime(DateTimeZone.UTC);

        Set<Interval> events = new TreeSet<Interval>(
                new Comparator<Interval>() {
                    public int compare(Interval o1, Interval o2) {
                        return o1.toDuration().compareTo(o2.toDuration());
                    }
                });

        events.add(new Interval(
                new LocalTime(9, 0).toDateTime(today),
                new LocalTime(11, 0).toDateTime(today)));

        events.add(new Interval(
                new LocalTime(10, 0).toDateTime(today),
                new LocalTime(15, 0).toDateTime(today)));

        events.add(new Interval(
                new LocalTime(13, 0).toDateTime(today),
                new LocalTime(16, 0).toDateTime(today)));

        events.add(new Interval(
                new LocalTime(8, 0).toDateTime(today),
                new LocalTime(18, 0).toDateTime(today)));

        events.add(new Interval(
                new LocalTime(16, 0).toDateTime(today),
                new LocalTime(17, 0).toDateTime(today)));

        for (Interval event : events) {
            DateTimeFormatter formatter = ISODateTimeFormat.hourMinute();

            System.out.printf("event: %s-%s, duration: %d\n",
                    formatter.print(event.getStart()),
                    formatter.print(event.getEnd()),
                    event.toDurationMillis());
//            event: 16:00-17:00, duration: 3600000
//            event: 09:00-11:00, duration: 7200000
//            event: 13:00-16:00, duration: 10800000
//            event: 10:00-15:00, duration: 18000000
//            event: 08:00-18:00, duration: 36000000
        }
    }

    @Test
    public void coarseNoTimeDates(){
        LocalDate firstOfFebruary1 = new LocalDate(2012, 2, 1);
        LocalDate firstOfMarch1 = new LocalDate(2012, 3, 1);
        DateTime firstOfFebruary2 = new DateTime(2012, 2, 1, 1, 0, DateTimeZone.UTC).withTimeAtStartOfDay();
        DateTime firstOfMarch2 = new DateTime(2012, 3, 1, 0, 1, DateTimeZone.UTC).withTimeAtStartOfDay();

        DateTimeFormatter formatter = ISODateTimeFormat.yearMonthDay();

        System.out.println(firstOfFebruary1.plus(Months.ONE));
        System.out.println(firstOfMarch1.plus(Months.ONE));

        System.out.println(firstOfFebruary2.plus(Months.ONE));
        System.out.println(firstOfMarch2.plus(Months.ONE));

//        2012-03-01
//        2012-04-01
    }

    @Test
    public void periodsMath(){
        DateTimeFormatter formatter = ISODateTimeFormat.yearMonthDay();

        DateTime start = new DateTime(2013, 1, 1, 0, 0, DateTimeZone.UTC).withTimeAtStartOfDay();
        DateTime end = new DateTime(2014, 8, 1, 0, 0, DateTimeZone.UTC).withTimeAtStartOfDay();

        Period period = new Period(start, end);

        DateTime startMinusPeriod = start.minus(period);
        DateTime endPlusPeriod = end.plus(period);

        System.out.printf("before start: %s\n", formatter
                .print(startMinusPeriod));
        System.out.printf("period start: %s\n", formatter.print(start));
        System.out.printf("period end:   %s\n", formatter.print(end));
        System.out.printf("after  end:   %s\n", formatter
                .print(endPlusPeriod));

//        before start: 2011-06-01
//        period start: 2013-01-01
//        period end:   2014-08-01
//        after  end:   2016-03-01
    }

    @Test
    public void periodsMathWithLocalDate(){
        DateTimeFormatter formatter = ISODateTimeFormat.yearMonthDay();

        LocalDate start = new LocalDate(2013, 1, 1);
        LocalDate end = new LocalDate(2014, 8, 1);

        Period period = new Period(start, end);

        LocalDate startMinusPeriod = start.minus(period);
        LocalDate endPlusPeriod = end.plus(period);

        System.out.printf("before start: %s\n", formatter
                .print(startMinusPeriod));
        System.out.printf("period start: %s\n", formatter.print(start));
        System.out.printf("period end:   %s\n", formatter.print(end));
        System.out.printf("after  end:   %s\n", formatter
                .print(endPlusPeriod));

//        before start: 2011-06-01
//        period start: 2013-01-01
//        period end:   2014-08-01
//        after  end:   2016-03-01
    }

    @Test
    public void interoperabilityBetweenTypesAndFluency(){
        DateTime dateTime = new DateTime(
                2013, 1, 1, 11, 15, DateTimeZone.UTC);

        Date instantToDate = dateTime.toInstant().toDate();
        Date dateTimeToDate = dateTime.toDate();
        Date dateMidnightToDate = dateTime.toDateMidnight().toDate();
        Date mutableDateTimeToDate = dateTime.toMutableDateTime().toDate();

        Instant dateToInstant = new Instant(instantToDate.getTime());
        DateTime dateToDateTime = new DateTime(dateTimeToDate.getTime());
        DateMidnight dateToDateMidnight = new DateMidnight(
                dateMidnightToDate.getTime());
        MutableDateTime dateToMutableDateTime = new MutableDateTime(
                mutableDateTimeToDate.getTime());
    }
}
