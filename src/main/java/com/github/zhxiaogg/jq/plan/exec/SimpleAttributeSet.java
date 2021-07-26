package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.utils.AttributeSearchUtil;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.zhxiaogg.jq.utils.Requires.require;

public class SimpleAttributeSet implements AttributeSet {
    /**
     * relation names if any.
     */
    private final String[] names;

    private final ResolvedAttribute[] attributes;

    public SimpleAttributeSet(String[] names, ResolvedAttribute[] attributes) {
        this.names = names;
        this.attributes = attributes;
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = attributes[i].withOrdinal(i);
        }
    }

    public SimpleAttributeSet(List<ResolvedAttribute> attributes) {
        this(new String[0], attributes.toArray(new ResolvedAttribute[0]));
    }

    public SimpleAttributeSet(ResolvedAttribute[] attributes) {
        this(new String[0], attributes);
    }

    @Override
    public ResolvedAttribute getAttribute(int[] ordinals, int offset) {
        ResolvedAttribute attribute = attributes[ordinals[offset]];
        if (ordinals.length - offset > 1) {
            attribute = attribute.getInner().getAttribute(ordinals, offset + 1);
        }
        return attribute;
    }

    @Override
    public List<ResolvedAttribute> allAttributes() {
        return Arrays.stream(attributes).collect(Collectors.toList());
    }

    @Override
    public SimpleAttributeSet withName(String alias) {
        require(Objects.nonNull(alias) && !alias.trim().isEmpty(), "invalid alias.");
        if (names.length == 1 && names[0].equalsIgnoreCase(alias)) {
            return this;
        } else {
            return new SimpleAttributeSet(new String[]{alias}, attributes);
        }
    }

    @Override
    public AttributeSet withAttributes(List<ResolvedAttribute> attributes) {
        return new SimpleAttributeSet(names, attributes.toArray(new ResolvedAttribute[0]));
    }

    @Override
    public AttributeSet mergeWith(AttributeSet target) {
        if (target instanceof EmptyAttributeSet) {
            return this;
        } else if (target instanceof SimpleAttributeSet) {
            List<ResolvedAttribute> expandedAttributes = Arrays.stream(((SimpleAttributeSet) target).attributes).map(ResolvedAttribute::expand).collect(Collectors.toList());
            Map<String, ResolvedAttribute> targetAttributes = new HashMap<>();
            for (ResolvedAttribute a : expandedAttributes) {
                targetAttributes.put(a.getNames()[0], a);
            }
            List<ResolvedAttribute> mergedAttributes = new ArrayList<>();
            for (ResolvedAttribute attribute : this.attributes) {
                ResolvedAttribute expanded = attribute.expand();
                ResolvedAttribute targetAttribute = targetAttributes.get(expanded.getNames()[0]);
                if (targetAttribute != null) {
                    ResolvedAttribute merged = expanded.mergeWith(targetAttribute);
                    targetAttributes.remove(expanded.getNames()[0]);
                    mergedAttributes.add(merged);
                } else {
                    mergedAttributes.add(expanded);
                }
            }
            return new SimpleAttributeSet(names, mergedAttributes.toArray(new ResolvedAttribute[0]));
        } else {
            throw new IllegalStateException("not supported by now!");
        }
    }

    @Override
    public int[] byId(String id) {
        int[] ordinals = new int[0];
        for (ResolvedAttribute attribute : attributes) {
            if ((ordinals = attribute.byId(id)).length > 0) {
                break;
            }
        }
        return ordinals;
    }

    /**
     * Get attribute oridnal by matching input names with attribute names.
     * <p>
     * Strategy:
     * 1. first try match the names with relation name, if all remaining matches successfully, then happy
     * 2. otherwise, treat the names as without relation name, and match against attribute names directly.
     *
     * @param names
     * @return
     */
    @Override
    public int[] byName(String[] names, int offset) {
        int offsetAfterRelationNames = 0;
        if (AttributeSearchUtil.prefixMatches(names, this.names, offset)) {
            offsetAfterRelationNames = offset + this.names.length;
        }
        int[] ordinals = new int[0];
        for (int i = 0; i < attributes.length; i++) {
            if ((ordinals = attributes[i].byName(names, offsetAfterRelationNames)).length > 0) {
                break;
            }
        }
        return ordinals;
    }

    @Override
    public int numAttributes() {
        return attributes.length;
    }
}
