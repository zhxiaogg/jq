package com.github.zhxiaogg.jq.utils;

public class AttributeSearchUtil {
    /**
     * Matches input against target, checking if input prefix can fully match the target.
     *
     * @param input
     * @param prefix
     * @return
     */
    public static boolean prefixMatches(String[] input, String[] prefix, int offset) {
        boolean matched = true;
        if (input.length - offset >= prefix.length) {
            for (int i = 0; i < prefix.length; i++) {
                if (!input[i + offset].equalsIgnoreCase(prefix[i])) {
                    matched = false;
                    break;
                }
            }
        } else {
            matched = false;
        }
        return matched;
    }
}
