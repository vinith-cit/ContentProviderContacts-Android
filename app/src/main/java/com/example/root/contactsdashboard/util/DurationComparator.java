package com.example.root.contactsdashboard.util;

import java.util.Comparator;

/**
 * Created by root on 26/10/16.
 */

/**
 * DurationComparator using Comparator for Sorting Desending order  Functionality for pojo type list
 */
public class DurationComparator implements Comparator<CallLogDetail> {

    //Compare Function Sorting descending order by totalCallDuration
    @Override
    public int compare(CallLogDetail o1, CallLogDetail o2) {
        if(o1.totalCallDuration==o2.totalCallDuration)
            return 0;
        else if(o1.totalCallDuration< o2.totalCallDuration)
            return 1;
        else
            return -1;
    }


}
