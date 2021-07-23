package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AttributeSet {
    private final ResolvedAttribute[] attributes;

    public AttributeSet(List<ResolvedAttribute> attributes) {
        this(attributes.toArray(new ResolvedAttribute[0]));
    }

    public static AttributeSet create(List<ResolvedAttribute> left, List<ResolvedAttribute> right) {
        List<ResolvedAttribute> outputs = new ArrayList<>(left);
        outputs.addAll(right);
        return new AttributeSet(outputs.toArray(new ResolvedAttribute[0]));
    }

    public ResolvedAttribute getAttribute(int ordinal) {
        return attributes[ordinal];
    }

    public List<ResolvedAttribute> allAttributes() {
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
