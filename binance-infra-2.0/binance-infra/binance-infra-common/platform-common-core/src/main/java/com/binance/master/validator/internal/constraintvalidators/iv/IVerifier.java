package com.binance.master.validator.internal.constraintvalidators.iv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface IVerifier<T> {

    public boolean valid(T value, ValidResult result);

    public static class ValidResult {

        private Map<String, String> fieldMap = new HashMap<>();
        private List<String> list = new ArrayList<>();

        public void addFieldError(String fieldName, String message) {
            fieldMap.put(fieldName, message);
        }

        public void addError(String message) {
            this.list.add(message);
        }

        public boolean isEmpty() {
            return this.fieldMap.isEmpty() && this.list.isEmpty();
        }

        public Set<Entry<String, String>> fieldErrors() {
            return this.fieldMap.entrySet();
        }

        public List<String> errors() {
            return this.list;
        }
    }
}
