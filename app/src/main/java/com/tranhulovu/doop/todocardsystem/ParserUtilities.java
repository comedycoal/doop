package com.tranhulovu.doop.todocardsystem;

import android.util.Pair;

import com.google.android.gms.tasks.Task;

import java.security.InvalidParameterException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUtilities
{
    private final static Map<String, Integer> Numbers;
    private final static Map<String, DayOfWeek> Weekdays;

    static
    {
        Numbers = new HashMap<>();
        Numbers.put("a", 1);
        Numbers.put("an", 1);
        Numbers.put("one", 1);
        Numbers.put("two", 2);
        Numbers.put("three", 3);
        Numbers.put("four", 4);
        Numbers.put("five", 5);
        Numbers.put("six", 6);
        Numbers.put("seven", 7);
        Numbers.put("eight", 8);
        Numbers.put("nine", 9);
        Numbers.put("ten", 10);

        Weekdays = new HashMap<>();
        Weekdays.put("monday", DayOfWeek.MONDAY);
        Weekdays.put("tuesday", DayOfWeek.TUESDAY);
        Weekdays.put("wednesday", DayOfWeek.WEDNESDAY);
        Weekdays.put("thursday", DayOfWeek.THURSDAY);
        Weekdays.put("friday", DayOfWeek.FRIDAY);
        Weekdays.put("saturday", DayOfWeek.SATURDAY);
        Weekdays.put("sunday", DayOfWeek.SUNDAY);
        Weekdays.put("mon", DayOfWeek.MONDAY);
        Weekdays.put("tue", DayOfWeek.TUESDAY);
        Weekdays.put("wed", DayOfWeek.WEDNESDAY);
        Weekdays.put("thu", DayOfWeek.THURSDAY);
        Weekdays.put("fri", DayOfWeek.FRIDAY);
        Weekdays.put("sat", DayOfWeek.SATURDAY);
        Weekdays.put("sun", DayOfWeek.SUNDAY);

    }

    public static List<Pair<String, String>> splitIntoTokens(String text)
    {
        Pattern p = Pattern.compile("@[^ ]+", Pattern.DOTALL);
        Matcher m = p.matcher(text);

        List<Pair<Integer, Integer>> tokenIndices = new ArrayList<>();
        while (m.find())
        {
            tokenIndices.add(new Pair<>(m.start(), m.end()));
        }

        List<Pair<String, String>> sToSs = new ArrayList<>();

        if (tokenIndices.get(0).first > 0)
        {
            sToSs.add(new Pair<>("", text.substring(0, tokenIndices.get(0).first)));
        }

        for (int i = 0; i < tokenIndices.size(); ++i)
        {
            int end = i < tokenIndices.size() - 1 ? tokenIndices.get(i+1).first : text.length();
            sToSs.add(new Pair<>(
                    text.substring(tokenIndices.get(i).first, tokenIndices.get(i).second),
                    text.substring(tokenIndices.get(i).first, end)));
        }

        return sToSs;
    }

    public static List<String> splitLines(String text)
    {
        String[] splitted = text.split("\n");
        return Arrays.asList(splitted);
    }

    private static class TimeParser
    {
        String exactTime = "";
        String dateMod = "";
        String weekDate = "";
        String diviator = "";

        private static TimeParser from(String value)
        {
            TimeParser r = new TimeParser();
            Pattern p = Pattern.compile("([\\d:h]+|one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve)( ?[ap]m|)?", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(value);
            if (m.find())
                r.exactTime = m.group();

            p = Pattern.compile("(morning|afternoon|evening|night)", Pattern.CASE_INSENSITIVE);
            m = p.matcher(value);
            if (m.find())
                r.dateMod = m.group();

            p = Pattern.compile("((mon|tue|wed(nes)?|thu(rs)?|fri|sat(ur)?|sun)(day)?|tomorrow|today)", Pattern.CASE_INSENSITIVE);
            m = p.matcher(value);
            if (m.find())
                r.weekDate = m.group();

            p = Pattern.compile("next (week|day)", Pattern.CASE_INSENSITIVE);
            m = p.matcher(value);
            if (m.find())
                r.diviator = m.group();

            return r;
        }

        public ZonedDateTime implyTime()
                throws InvalidParameterException
        {
            try
            {
                ZonedDateTime time = ZonedDateTime.now();

                if (Weekdays.containsKey(weekDate))
                {
                    time.with(TemporalAdjusters.next(Weekdays.get(weekDate)));
                }
                else if (weekDate.equalsIgnoreCase("tomorrow"))
                {
                    time.plusDays(1);
                }

                if (!exactTime.isEmpty())
                {
                    if (Numbers.containsKey(exactTime.toLowerCase()))
                    {
                        time.with(LocalTime.of(Numbers.get(exactTime.toLowerCase()), 0));
                    }
                    else
                    {
                        // Try the following pattern:
                        String[] patterns = {
                                "H", "h a", "ha", "h:mm a", "H:mm"
                        };

                        String upped = exactTime.toUpperCase();
                        for (String pattern : patterns)
                        {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                            try
                            {
                                LocalTime parsed = LocalTime.parse(upped, formatter);
                                time.with(parsed);
                            }
                            catch (Exception e)
                            {
                                continue;
                            }
                        }
                    }
                }
                else if (!dateMod.isEmpty())
                {
                    if (dateMod.equalsIgnoreCase("morning"))
                        time.with(LocalTime.of(6,0));
                    if (dateMod.equalsIgnoreCase("afternoon"))
                        time.with(LocalTime.of(13,0));
                    else
                        time.with(LocalTime.of(19,0));
                }
                else
                    time.with(LocalTime.of(6, 0));

                if (!diviator.isEmpty())
                {
                    if (diviator.equalsIgnoreCase("next week"))
                        time.plusDays(7);
                    if (diviator.equalsIgnoreCase("next day"))
                        time.plusDays(1);
                }

                return time;
            }
            catch(Exception e)
            {
                throw new InvalidParameterException("ParserUtilities.TimeParser#implyTime: Cannot parse with String value");
            }
        }

        public ZonedDateTime implyTimeEnd()
                throws InvalidParameterException
        {
            try
            {
                ZonedDateTime time = ZonedDateTime.now();

                if (Weekdays.containsKey(weekDate))
                {
                    time.with(TemporalAdjusters.next(Weekdays.get(weekDate)));
                }
                else if (weekDate.equalsIgnoreCase("tomorrow"))
                {
                    time.plusDays(1);
                }

                if (!dateMod.isEmpty())
                {
                    if (dateMod.equalsIgnoreCase("morning"))
                        time.with(LocalTime.of(11,0));
                    if (dateMod.equalsIgnoreCase("afternoon"))
                        time.with(LocalTime.of(18,0));
                    else
                        time.with(LocalTime.of(23,0));
                }
                else
                    time.with(LocalTime.of(23, 0));

                if (!diviator.isEmpty())
                {
                    if (diviator.equalsIgnoreCase("next week"))
                        time.plusDays(7);
                    if (diviator.equalsIgnoreCase("next day"))
                        time.plusDays(1);
                }

                return time;
            }
            catch(Exception e)
            {
                throw new InvalidParameterException("ParserUtilities#implyTimeEnd: Cannot parse with String value");
            }
        }
    }

    public static long implyDuration(String value)
            throws InvalidParameterException
    {
        try
        {
            String[] token = value.split(" ");
            long ammount = 0;
            long unit = 0;

            try
            {
                ammount = Integer.parseInt(token[0]);
            }
            catch (Exception e)
            {
                Numbers.get(token[0]);
            }

            String unitS = token[1];
            if (unitS.equalsIgnoreCase("day") || unitS.equalsIgnoreCase("days"))
                unit = 24 * 60;
            if (unitS.equalsIgnoreCase("hour") || unitS.equalsIgnoreCase("hours"))
                unit = 60;
            if (unitS.equalsIgnoreCase("minute") || unitS.equalsIgnoreCase("minutes"))
                unit = 1;
            else
                throw new Exception("Dummy");

            return ammount * unit;
        }
        catch (Exception e)
        {
            throw new InvalidParameterException("ParserUtilities#implyDuration: Cannot parse with String value");
        }
    }

    public static TaskFitter.Task getTask(List<Pair<String, String>> tokens,
                                          TaskFitter.Task priorTask,
                                          ZoneId zoneId)
            throws InvalidParameterException
    {
        TaskFitter.Task t = new TaskFitter.Task();
        boolean queueToo = false;
        for (Pair<String, String> token : tokens)
        {
            String keyword = token.first;
            String value = token.second;

            TimeParser parser = TimeParser.from(value);

            if (keyword.isEmpty())
            {
                t.name = value;
                continue;
            }

            if (keyword.equalsIgnoreCase("@then"))
            {
                t.priorTask = priorTask;
                continue;
            }

            if (keyword.equalsIgnoreCase("@on"))
            {
                t.start = parser.implyTime().toEpochSecond();
                t.end = parser.implyTimeEnd().toEpochSecond();
                break;
            }

            if (keyword.equalsIgnoreCase("@in"))
            {
                t.length = implyDuration(value);
                break;
            }

            if (keyword.equalsIgnoreCase("@from"))
            {
                t.start = parser.implyTime().toEpochSecond();
                queueToo = true;
                continue;
            }

            if (queueToo && keyword.equalsIgnoreCase("@to"))
            {
                t.end = parser.implyTime().toEpochSecond();
                queueToo = false;
                break;
            }
            else if (queueToo)
            {
                throw new InvalidParameterException("TaskFitter.Task#getTask: @from token not followed by @to");
            }

            throw new InvalidParameterException("TaskFitter.Task#getTask: token with invalid keyword: " + keyword);
        }

        return t;
    }
}
