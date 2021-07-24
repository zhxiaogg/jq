package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MergedAttributeSet implements AttributeSet {
    private final List<AttributeSet> attributeSets;

    @Override
    public ResolvedAttribute getAttribute(int ordinal) {
        int offset = 0;
        for (AttributeSet attributeSet : attributeSets) {
            if (ordinal < attributeSet.numAttributes() + offset) {
                return attributeSet.getAttribute(ordinal - offset);
            }
            offset += attributeSet.numAttributes();
        }
        throw new ArrayIndexOutOfBoundsException(ordinal);
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
    public int byId(String id) {
        int ordinal = -1;
        int offset = 0;
        for (AttributeSet attributeSet : attributeSets) {
            if ((ordinal = attributeSet.byId(id)) > -1) {
                ordinal = offset + ordinal;
                break;
            }
            offset += attributeSet.numAttributes();
        }
        return ordinal;
    }

    @Override
    public int byName(String[] names) {
        int ordinal = -1;
        int offset = 0;
        for (AttributeSet attributeSet : attributeSets) {
            if ((ordinal = attributeSet.byName(names)) > -1) {
                ordinal = offset + ordinal;
                break;
            }
            offset += attributeSet.numAttributes();
        }
        return ordinal;
    }

    @Override
    public int numAttributes() {
        return attributeSets.stream().mapToInt(AttributeSet::numAttributes).sum();
    }
}
