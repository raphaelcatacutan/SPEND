package com.ssg._test;

class NameParser {
    public static void main(String[] args) {
        parseName("Raphael James C. Catacutan");
        parseName("Catacutan, Raphael James C.");
        parseName("Catacutan, Raphael");
        parseName("Raphael");
    }
    public static void parseName(String fullName) {
        String[] nameParts = fullName.split(" ");
        StringBuilder firstName = new StringBuilder();
        StringBuilder middleInitial = new StringBuilder();
        StringBuilder lastName = new StringBuilder();

        int addTo = (fullName.contains(",")) ? 0: 1;
        for (String x: nameParts) {
            if (x.matches("[A-Z](\\.|\\.[A-Z]\\.)?")) addTo = 2;
            if (addTo == 0) { // 0 Last Name
                boolean hasComma = x.contains(",");
                lastName.append(hasComma ? x.substring(0, x.length() - 1) : x + " ");
                if (hasComma) addTo = 1;
            } else if (addTo == 1) { // 1 First Name
                firstName.append(x).append(" ");
            } else { // 2 Middle Initial
                middleInitial.append(x);
                addTo = 0;
            }
        }

        System.out.println("----\nPrompt: " + fullName);
        System.out.println("First Name: " + firstName);
        System.out.println("Middle Initial: " + middleInitial);
        System.out.println("Last Name: " + lastName);
    }
    public static void testRegex(String middleInitial) {
        System.out.println(middleInitial.matches("[A-Z](\\.|\\.[A-Z]\\.)?"));
    }
}