package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.schema.Attribute;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AttributeSet {
    private final Attribute[] attributes;

    public static AttributeSet create(List<Attribute> left, List<Attribute> right) {
        List<Attribute> outputs = new ArrayList<>(left);
        outputs.addAll(right);
        return new AttributeSet(outputs.toArray(new Attribute[0]));
    }

    public Attribute getAttribute(int ordinal) {
        return attributes[ordinal];
    }

    public List<Attribute> allAttributes() {
        return Arrays.stream(attributes).collect(Collectors.toList());
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
