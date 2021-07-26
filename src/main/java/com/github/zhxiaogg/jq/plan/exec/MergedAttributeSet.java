package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@Data
public class MergedAttributeSet implements AttributeSet {
    private final String[] names;
    private final List<AttributeSet> attributeSets;

    @Override
    public ResolvedAttribute getAttribute(int[] ordinals, int offset) {
        int idx = 0;
        for (AttributeSet attributeSet : attributeSets) {
            if (ordinals[offset] < attributeSet.numAttributes() + idx) {
                int[] newOrdinals = new int[ordinals.length];
                for (int i = 0; i < ordinals.length; i++) {
                    newOrdinals[i] = ordinals[i];
                    if (i == offset) newOrdinals[i] += idx;
                }
                return attributeSet.getAttribute(newOrdinals, offset);
            }
            idx += attributeSet.numAttributes();
        }
        throw new IllegalStateException("cannot find attribute at " + Arrays.toString(ordinals));
    }

    @Override
    public List<ResolvedAttribute> allAttributes() {
        List<ResolvedAttribute> result = new ArrayList<>(this.numAttributes());
        int ordinal = 0;
        for (AttributeSet attributeSet : this.attributeSets) {
            for (ResolvedAttribute attribute : attributeSet.allAttributes()) {
                result.add(attribute.withOrdinal(ordinal++));
            }
        }
        return result;
    }

    @Override
    public int[] byId(String id) {
        int[] ordinals = new int[0];
        int offset = 0;
        for (AttributeSet attributeSet : attributeSets) {
            if ((ordinals = attributeSet.byId(id)).length > 0) {
                ordinals[0] += offset;
                break;
            }
            offset += attributeSet.numAttributes();
        }
        return ordinals;
    }

    @Override
    public int[] byName(String[] names, int offset) {
        int[] ordinals = new int[0];
        int idx = 0;
        for (AttributeSet attributeSet : attributeSets) {
            if ((ordinals = attributeSet.byName(names, offset)).length > 0) {
                ordinals[0] += idx;
                break;
            }
            idx += attributeSet.numAttributes();
        }
        return ordinals;
    }

    @Override
    public int numAttributes() {
        return attributeSets.stream().mapToInt(AttributeSet::numAttributes).sum();
    }

    @Override
    public AttributeSet withName(String alias) {
        require(Objects.nonNull(alias) && !alias.trim().isEmpty(), "invalid alias.");
        if (names.length == 1 && names[0].equalsIgnoreCase(alias)) {
            return this;
        } else {
            return new MergedAttributeSet(new String[]{alias}, attributeSets);
        }
    }

    @Override
    public AttributeSet withAttributes(List<ResolvedAttribute> attributes) {
        throw new IllegalStateException("not supported!");
    }

    @Override
    public AttributeSet mergeWith(AttributeSet target) {
        throw new IllegalStateException("not supported!");
    }
}
