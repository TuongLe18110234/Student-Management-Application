package com.example.unionline.Sorter;

import com.example.unionline.DTO.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class NotificationDateSorter implements Comparator<Notification> {

    @Override
    public int compare(Notification o1, Notification o2) {
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o1.getCreateDate());
            date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o2.getCreateDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2.compareTo(date1);
    }
}
