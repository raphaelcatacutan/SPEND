package com.ssg._test;

import java.util.Arrays;

public class MultipleMethod {
    public static void main(String[] args) {
        test("Raphael", "James");
        test();
    }
    public static void test(String... args) {
        System.out.println(Arrays.toString(args));
        System.out.println(String.join(" ", args));
    }
}
