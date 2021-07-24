package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;

import java.util.Collections;
import java.util.List;

public class EmptyAttributeSet implements AttributeSet {
    @Override
    public ResolvedAttribute getAttribute(int[] ordinals, int offset) {
        throw new ArrayIndexOutOfBoundsException("get Attribute from EmptyAttributeSet");
    }

    @Override
    public List<ResolvedAttribute> allAttributes() {
        return Collections.emptyList();
    }

    @Override
    public int[] byId(String id) {
        return new int[0];
    }

    @Override
    public int[] byName(String[] names, int offset) {
        return new int[0];
    }

    @Override
    public int numAttributes() {
        return 0;
    }
}
