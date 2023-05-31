package com.ssg._test;

public class MultipleTypes {
    public static void main(String[] args) {
        String[] raphael = {"james", "catacutan"};
        multipleType("rpahale", raphael);
        simpleType(raphael);
    }

    public static <T> void multipleType(T... args) {
        for (T arg : args) {
            System.out.println(arg);
        }
    }
    public static void simpleType(String... args) {
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}