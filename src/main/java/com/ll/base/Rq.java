package com.ll.base;

import lombok.Getter;
import standard.util.Ut;

import java.util.HashMap;
import java.util.Map;

public class Rq {
    @Getter
    private String action;
    private final Map<String, String> paramsMap;

    public Rq(String cmd) {
        paramsMap = new HashMap<>();

        String[] cmdBits = cmd.split("\\?", 2);
        action = cmdBits[0].trim();

        if (cmdBits.length == 1) {
            return;
        }

        String queryString = cmdBits[1].trim();

        String[] queryStringBits = queryString.split("&");

        for (String queryParamStr : queryStringBits) {
            String[] queryParamStrBits = queryParamStr.split("=", 2);

            String paramName = queryParamStrBits[0];
            String paramValue = queryParamStrBits[1];

            paramsMap.put(paramName, paramValue);
        }
    }

    public int getParamAsInt(String paramName, int defaultValue) {
        return Ut.str.parseInt(paramsMap.get(paramName), defaultValue);
    }

    public long getParamAsLong(String paramName, long defaultValue) {
        return Ut.str.parseLong(paramsMap.get(paramName), defaultValue);
    }
}
