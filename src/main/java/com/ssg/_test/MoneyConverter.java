package com.ssg._test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Scanner;

public class MoneyConverter {
    public static String formatMoney(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        return "₱" + decimalFormat.format(amount);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the amount: ");
        double money = scanner.nextDouble();
        scanner.close();

        String formattedMoney = formatMoney(money);
        System.out.println("Formatted amount: " + formattedMoney);
    }
}
