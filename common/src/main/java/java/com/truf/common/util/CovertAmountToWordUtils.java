package java.com.truf.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@lombok.experimental.UtilityClass
public class CovertAmountToWordUtils {

    /**
     * This function returns Amount in words
     *
     * @Method :convertAmountToWord(long number)
     * @Brief :To get Amount in words
     * @InputParam :number(amount)
     * @Return :String
     **/

    public static String convertAmountToWord(long num) {
        int length = String.valueOf(num).length();
        if (length > 14)
            return "(Number of digits in Amount Exceeded)";
        if (num == 0)
            return "zero";
        if (length > 9) {
            long last7Digits = num % 10000000;
            long remainingDigits = num / 10000000;

            StringBuilder amountInWords = new StringBuilder(amountToWords(remainingDigits)).append(" Crore ");
            if (last7Digits > 0)
                amountInWords.append(amountToWords(last7Digits));

            return String.valueOf(amountInWords);
        } else
            return amountToWords(num);
    }

    private String amountToWords(long num) {
        BigDecimal bigDecimal = new BigDecimal(num);
        long number;
        long no = bigDecimal.longValue();
        int digitsLength = String.valueOf(no).length();
        int i = 0;
        ArrayList<String> str = new ArrayList<>();
        HashMap<Integer, String> words = new HashMap<>();
        words.put(0, "");
        words.put(1, "One");
        words.put(2, "Two");
        words.put(3, "Three");
        words.put(4, "Four");
        words.put(5, "Five");
        words.put(6, "Six");
        words.put(7, "Seven");
        words.put(8, "Eight");
        words.put(9, "Nine");
        words.put(10, "Ten");
        words.put(11, "Eleven");
        words.put(12, "Twelve");
        words.put(13, "Thirteen");
        words.put(14, "Fourteen");
        words.put(15, "Fifteen");
        words.put(16, "Sixteen");
        words.put(17, "Seventeen");
        words.put(18, "Eighteen");
        words.put(19, "Nineteen");
        words.put(20, "Twenty");
        words.put(30, "Thirty");
        words.put(40, "Forty");
        words.put(50, "Fifty");
        words.put(60, "Sixty");
        words.put(70, "Seventy");
        words.put(80, "Eighty");
        words.put(90, "Ninety");
        String[] digits = {"", "Hundred", "Thousand", "Lakh", "Crore"};
        while (i < digitsLength) {
            int divider = (i == 2) ? 10 : 100;
            number = no % divider;
            no = no / divider;
            i += divider == 10 ? 1 : 2;
            if (number > 0) {
                int counter = str.size();
                String plural = (counter > 0 && number > 9) ? "s" : "";
                String tmp = (number < 21) ? words.get((int) number) + " " + digits[counter] +
                        plural : words.get((int) (number / 10) * 10) + " " + words.get((int) (number % 10)) +
                        " " + digits[counter] + plural;
                str.add(tmp);
            } else {
                str.add("");
            }
        }
        Collections.reverse(str);
        return String.join(" ", str).trim();
    }

}