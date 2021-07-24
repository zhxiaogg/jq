package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.utils.AttributeSearchUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleAttributeSet implements AttributeSet {
    /**
     * relation names if any.
     */
    private final String[] relationNames;

    private final ResolvedAttribute[] attributes;

    public SimpleAttributeSet(String[] relationNames, ResolvedAttribute[] attributes) {
        this.relationNames = relationNames;
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

    public SimpleAttributeSet withName(String name) {
        if (name == null) {
            return this;
        } else {
            return new SimpleAttributeSet(new String[]{name}, attributes);
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
     * Matches input against target starts from offset, check if they fully matches.
     *
     * @param input
     * @param target
     * @param offset
     * @return
     */
    private boolean fullyMatches(String[] input, String[] target, int offset) {
        boolean matched = true;
        if (input.length - offset == target.length) {
            for (int i = 0; i < target.length; i++) {
                if (!input[i + offset].equalsIgnoreCase(target[i])) {
                    matched = false;
                    break;
                }
            }
        } else {
            matched = false;
        }
        return matched;
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
        if (AttributeSearchUtil.prefixMatches(names, this.relationNames, offset)) {
            offsetAfterRelationNames = offset + this.relationNames.length;
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
