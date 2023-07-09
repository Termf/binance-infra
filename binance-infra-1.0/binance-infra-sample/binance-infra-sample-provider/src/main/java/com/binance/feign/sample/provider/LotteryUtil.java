package com.binance.feign.sample.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LotteryUtil {
    // 已经选中的尾号的数量
    // public static long chooseNum = 0;
    public static void main(String[] args) {
        // 起始申购序列号
        long start = 1;
        // 实际申购数量
        long purchaseNum = 34678;
        // 实际发行
        long distributeNum = 10000;
        String seed = "0000000000000000000e2bd83130bc65cea4541749c686e0a5b9b4b4a8de7387";
        Map<String, Integer> distributeMap = getLottery(purchaseNum, distributeNum, seed);
        int total = 0;
        Iterator iterator = distributeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
            total += (int)entry.getValue();
        }
        System.out.println("中签数量：" + total);
    }

    /**
     * 求随机数种子
     * 
     * @param seed
     * @return
     */
    private static long transferSeedToLong(String seed) {
        long a = 0;
        byte[] bs = seed.getBytes();
        for (int i = 0; i < bs.length; i++) {
            a = a + bs[i];
        }
        return a;
    }

    /**
     * 得到各个尾数的中签数量
     *
     * @param purchaseNum
     * @param distributeNum
     * @return
     */
    public static Map<String, Integer> getLottery(long purchaseNum, long distributeNum, String seed) {
        Random random = new Random(transferSeedToLong(seed));
        // 中签尾数及数量
        Map<String, Integer> distributeMap = new LinkedHashMap<>();
        if (purchaseNum <= distributeNum) {
            int n1 = (int)(purchaseNum % 10);
            int n2 = (int)(purchaseNum / 10);
            for (int i = 0; i < 10; i++) {
                if (i >= n1) {
                    distributeMap.put(i + "", n2);
                } else {
                    distributeMap.put(i + "", n2 + 1);
                }
            }
            return distributeMap;
        }
        long chooseNum = 0;
        double allocationRate = distributeNum * 1.0 / purchaseNum;// 0.001204177...
        System.out.println("中签率：" + allocationRate);
        int len = getDigitNum(purchaseNum);
        long distributeX = (long)(allocationRate * Math.pow(10, len));// 1204177
        List<Integer> digitList = getEachDigit(distributeX, len);// 1,2,0,4,1,7,7
        int lenX = getDigitNum(distributeX);
        List<Long> distributeList = new ArrayList<>();
        for (int i = 0; i < digitList.size(); i++) {
            int rate = digitList.get(i);
            // 尾号余数如232,158 也可以中奖
            long temp = (long)(purchaseNum % Math.pow(10, len - lenX + 1 + i));
            for (int j = 0; j < rate; j++) {
                if (chooseNum == distributeNum) {
                    return distributeMap;
                }
                // 该随机号有多少个
                String lotteryNum = getRandom(random, distributeList, len - lenX + 1 + i);
                int number = (int)(purchaseNum * Math.pow(10, -(len - lenX + 1 + i)));
                long lotteryLong = Long.parseLong(lotteryNum);
                if (lotteryLong <= temp && lotteryLong > 0) {
                    number++;
                }
                if (chooseNum + number <= distributeNum) {
                    chooseNum += number;
                } else {
                    break;
                }
                distributeList.add(lotteryLong);
                distributeMap.put(lotteryNum, number);
            }
        }
        int left = (int)(distributeNum - chooseNum);
        while (left > 0)// 每次产生一个号码
        {
            String lotteryNum = getRandom(random, distributeList, len);
            long lotteryLong = Long.parseLong(lotteryNum);
            if (lotteryLong > purchaseNum || lotteryLong == 0) {
                continue;
            }
            distributeList.add(lotteryLong);
            distributeMap.put(lotteryNum, 1);
            left--;
        }
        return distributeMap;
    }

    /**
     * 得到一个数的位数
     *
     * @param value
     * @return
     */
    public static int getDigitNum(long value) {
        return String.valueOf(value).length();
    }

    /**
     * 得到一个num位的随机数
     *
     * @param except
     * @param num
     * @return
     */
    public static String getRandom(Random random, List<Long> except, int num) {
        boolean confict = true;
        long obj = 0L;
        while (confict) {
            obj = (long)(random.nextDouble() * Math.pow(10, num));
            while (except.contains(obj) || obj == 0) {// obj肯定不在except中
                obj = (long)(random.nextDouble() * Math.pow(10, num));
            }
            confict = false;
            int len = getLen(obj);
            for (long temp : except) {
                int len2 = getLen(temp);
                if (len2 == len) {
                    continue;
                }
                if (Math.abs(obj - temp) % Math.pow(10, len2) == 0) // 有冲突
                {
                    confict = true;
                    break;
                }
            }
        }
        return String.format("%0" + num + "d", obj);
    }

    /**
     * 得到一个整数的位数
     *
     * @param num
     * @return
     */
    public static int getLen(long num) {
        int len = 0;
        while (num != 0) {
            num /= 10;
            len++;
        }
        return len;
    }

    /**
     * 得到每位的中签比率
     *
     * @param value
     * @param len
     * @return
     */
    public static List<Integer> getEachDigit(long value, int len) {
        String valueS = String.valueOf(value);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < valueS.length() - 1; i++) {
            result.add(Integer.parseInt(valueS.charAt(i) + ""));
        }
        return result;
    }

}
