package com.turf.common.util;

import lombok.experimental.UtilityClass;

import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class TransactionNoUtil {

    /**
     * This function returns a unique transaction id for all billing related actions
     *
     * @Method :getTransactionNo()
     * @Brief :For getting Unique transaction id for all billing related actions
     * @InputParam :NA
     * @Return :long
     **/
    public long getTransactionNo() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * This function returns a unique transaction id generated with date
     *
     * @Method :getTransactionNo(Date inDate)
     * @Brief :For getting Unique transaction generated with date
     * @InputParam :inDate
     * @Return :long
     **/
    public long getTransactionNo(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DATE, calendar.get(Calendar.DATE));
        instance.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        instance.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        return instance.getTimeInMillis();
    }
}