package com.ssg._test;

import com.ssg.database.SpendBConnection;

public class MainTest {
    public static void main(String[] args) {
        System.out.println(SpendBConnection.getColumnNames("OFFICERS"));
    }
}
