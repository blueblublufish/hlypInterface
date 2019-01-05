package com.hlyp.api.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhuzt on 2018/6/14.
 */
public class MoneyUtil {

    public final static BigDecimal RATIO = new BigDecimal(100);

    public final static Pattern MONEY_PATTERN = Pattern.compile("-?[0-9]+\\.?[0-9]*");

    public static Long toCent(double money) {
        BigDecimal bigDecimal = BigDecimal.valueOf(money);
        return bigDecimal.multiply(RATIO).longValue();
    }

    public static Long toCent(String money) {
        if (!isMoney(money)) {
            return 0L;
        }
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal.multiply(RATIO).longValue();
    }

    public static Long toCent(BigDecimal money) {
        return money.multiply(RATIO).longValue();
    }

    public static String toYuan(long money) {
        NumberFormat nf = new DecimalFormat("###0.00");

        BigDecimal bigDecimal = BigDecimal.valueOf(money);
        return nf.format(bigDecimal.divide(RATIO));
    }


    public static Integer toPercentage(double number) {
        BigDecimal bigDecimal = BigDecimal.valueOf(number);
        return bigDecimal.multiply(RATIO).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue();
    }

    public static Integer toPercentage(BigDecimal number) {
        return number.multiply(RATIO).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue();
    }

    public static Long countCommission(long money, int ratio) {
        BigDecimal bigDecimal = BigDecimal.valueOf(money * ratio);
        return bigDecimal.divide(RATIO).setScale(0, BigDecimal.ROUND_HALF_DOWN).longValue();
    }

    public static Long countTax(long money, int ratio) {
        BigDecimal bigDecimal = BigDecimal.valueOf(money * ratio);
        return bigDecimal.divide(RATIO).setScale(0, BigDecimal.ROUND_HALF_DOWN).longValue();
    }

    public static boolean isMoney(String money) {
        if (money == null) {
            return false;
        }
        Matcher isNum = MONEY_PATTERN.matcher(money);
        return isNum.matches();
    }
}
