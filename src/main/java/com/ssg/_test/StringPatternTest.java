package com.ssg._test;
import java.util.regex.*;

public class StringPatternTest {
    public static void main(String[] args) {
        String input = "int myVariable = 42.2;"; // Replace with your input string

        // Define the pattern
        String pattern = "int [a-zA-Z_][a-zA-Z0-9_]* = \\d+;";

        // Create a Pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a Matcher object
        Matcher matcher = regex.matcher(input);

        // Check if the input string matches the pattern
        if (matcher.matches()) {
            System.out.println("String matches the pattern.");
        } else {
            System.out.println("String does not match the pattern.");
        }
    }
}

