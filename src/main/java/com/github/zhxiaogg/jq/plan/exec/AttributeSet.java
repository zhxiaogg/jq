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

    static AttributeSet create(String[] names, ResolvedAttribute[] attributes) {
        return new SimpleAttributeSet(names, attributes);
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
            return new MergedAttributeSet(new String[0], attributeSets);
        }
    }

    static AttributeSet empty(String[] names) {
        return new EmptyAttributeSet(names);
    }

    static AttributeSet empty() {
        return new EmptyAttributeSet(new String[0]);
    }

    ResolvedAttribute getAttribute(int[] ordinals, int offset);

    List<ResolvedAttribute> allAttributes();

    int[] byId(String id);

    int[] byName(String[] names, int offset);

    int numAttributes();

    AttributeSet withName(String alias);

    AttributeSet withAttributes(List<ResolvedAttribute> attributes);

    /**
     * Merge target AttributeSet into current AttributeSet.
     *
     * @param target
     * @return
     */
    AttributeSet mergeWith(AttributeSet target);
}
