package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AttributeSet {
    private final Attribute[] attributes;

    public Attribute getAttribute(int ordinal) {
        return attributes[ordinal];
    }

    public int byId(String id) {
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public int byName(String name) {
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
