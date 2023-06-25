package com.ssg.utils;

import com.ssg.database.models.User;

public class RuntimeData {
    public static boolean NOSPLASH;
    public static User USER = new User(0, "", "", "", "", "", true, null);
    public static boolean CREATEDATABASE;
    public static boolean MANAGEXAMPP;
    public static boolean FILLDATA;
    public static int SCHOOLYEAR;
    public static boolean STARTXAMPP;
    public static String XAMPPLOCATION;
}
