package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@Data
public class EmptyAttributeSet implements AttributeSet {
    private final String[] names;

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

    @Override
    public AttributeSet withName(String alias) {
        require(Objects.nonNull(alias) && !alias.trim().isEmpty(), "invalid alias.");
        if (names.length == 1 && names[0].equalsIgnoreCase(alias)) {
            return this;
        } else {
            return new EmptyAttributeSet(new String[]{alias});
        }
    }

    @Override
    public AttributeSet withAttributes(List<ResolvedAttribute> attributes) {
        return AttributeSet.create(names, attributes.toArray(new ResolvedAttribute[0]));
    }

    @Override
    public AttributeSet mergeWith(AttributeSet target) {
        return target;
    }
}
