package com.example.timeapplication.timeview;

import android.text.TextUtils;

public class TimeUtils {



    public static int[] getDateAndTime(String dateAndTime) {
        if (TextUtils.isEmpty(dateAndTime)) return null;
        String[] dateAndTimeStrs = dateAndTime.split(" ");
        String dateStr = dateAndTimeStrs[0];
        String timeStr = dateAndTimeStrs[1];
        String[] dateStrs = dateStr.split("-");
        String[] timeStrs = timeStr.split(":");
        String[] returnStrs = new String[(dateStrs.length + timeStrs.length)];
        for (int i = 0; i < dateStrs.length; i++) {
            returnStrs[i] = dateStrs[i];
        }
        for (int i = 0; i < timeStrs.length; i++) {
            returnStrs[(dateStrs.length + i)] = timeStrs[i];
        }
        int[] returnInts = new int[returnStrs.length];
        for (int i = 0; i < returnStrs.length; i++) {
            if (returnStrs[i].matches("[0-9]+")) {
                returnInts[i] = Integer.parseInt(returnStrs[i]);
            } else {
                returnInts[i] = 0;
            }
        }
        return returnInts;

    }
}
