package com.binance.platform.monitor.pinpoint;

import java.util.HashMap;
import java.util.Map;

public class Transaction {
    private String transName;
    private long start;
    private long current;
    private int state = 0;
    public Map<String, Integer> stageMap = new HashMap<>();

    private Transaction(String transName) {
        this.transName = transName;
        this.start = System.currentTimeMillis();
        this.current = start;
    }

    public static void newEvent(String tranName) {
        Transaction transaction = new Transaction(tranName);
        transaction.start();
        transaction.end();
    }

    public static void newEvent(String tranName, int state) {
        Transaction transaction = new Transaction(tranName);
        transaction.start();
        transaction.setState(state);
        transaction.end();
    }


    public static Transaction newTransaction(String tranName) {
        Transaction transaction = new Transaction(tranName);
        transaction.start();
        return transaction;
    }

    private void start() {
    }

    public void addStage(String stage) {
        Long tmp = System.currentTimeMillis();
        Long cost = tmp - this.current;
        this.current = tmp;
        stageMap.put(stage, cost.intValue());
    }

    public void setState(int state) {
        this.state = state;
    }

    public void end() {

    }
}
