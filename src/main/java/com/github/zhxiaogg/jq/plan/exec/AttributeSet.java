package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;

import java.util.ArrayList;
import java.util.List;

public interface AttributeSet {
    static AttributeSet create(List<ResolvedAttribute> left, List<ResolvedAttribute> right) {
        List<ResolvedAttribute> outputs = new ArrayList<>(left);
        outputs.addAll(right);
        return create(new String[0], outputs.toArray(new ResolvedAttribute[0]));
    }

    static AttributeSet create(String[] relationNames, ResolvedAttribute[] attributes) {
        return new SimpleAttributeSet(relationNames, attributes);
    }

    static AttributeSet create(List<ResolvedAttribute> attributes) {
        return new SimpleAttributeSet(attributes);
    }

    static AttributeSet create(ResolvedAttribute[] attributes) {
        return new SimpleAttributeSet(attributes);
    }

    static AttributeSet createMerged(List<AttributeSet> attributeSets) {
        if (attributeSets.size() == 1) {
            return attributeSets.get(0);
        } else {
            return new MergedAttributeSet(attributeSets);
        }
    }

    static AttributeSet empty() {
        return new EmptyAttributeSet();
    }

    ResolvedAttribute getAttribute(int[] ordinals, int offset);

    List<ResolvedAttribute> allAttributes();

    int[] byId(String id);

    int[] byName(String[] names, int offset);

    int numAttributes();
}
