package com.app.driver.TimeAgo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ChatsTimeAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int WEEK_MILLIS = 7 * DAY_MILLIS ;

    public static String getTimeAgo(long time) {

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now =System.currentTimeMillis();;

        long diff = now - time;
        if(diff>0) {

            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a min ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " mins ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hr ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                String pattern = "hh:mm a";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return ""+simpleDateFormat.format(time);
            } else if (diff < 48 * HOUR_MILLIS) {
                String pattern = "hh:mm a";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return "yesterday "+simpleDateFormat.format(time);            }
            else if (diff < 7 * DAY_MILLIS) {
                String pattern = "E, dd MMM";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            } else if (diff < 2 * WEEK_MILLIS) {
                String pattern = "E, dd MMM";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return simpleDateFormat.format(time);
            } else if (diff < WEEK_MILLIS * 3) {
                String pattern = "E, dd MMM";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return simpleDateFormat.format(time);
            } else {
                String date = DateFormat.getDateInstance().format(time);
                return date.toString();
            }

        }
        else {

            diff=time-now;
            if (diff < MINUTE_MILLIS) {
                return "this minute";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute later";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes later";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour later";
            } else if (diff < 24 * HOUR_MILLIS) {
                String pattern = "hh:mm a";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return ""+simpleDateFormat.format(time);
            } else if (diff < 48 * HOUR_MILLIS) {
                String pattern = "hh:mm a";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return "yesterday "+simpleDateFormat.format(time);            }
            else if (diff < 7 * DAY_MILLIS) {
                String pattern = "E, dd MMM";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return simpleDateFormat.format(time);
            } else if (diff < 2 * WEEK_MILLIS) {
                String pattern = "E, dd MMM";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return simpleDateFormat.format(time);
            } else if (diff < WEEK_MILLIS * 3) {
                String pattern = "E, dd MMM";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                return simpleDateFormat.format(time);
            } else {
                String date = DateFormat.getDateInstance().format(time);
                return date.toString();
            }
        }
        String date = DateFormat.getDateInstance().format(time);
        return date.toString();
    }























}