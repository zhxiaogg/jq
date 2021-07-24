package com.github.zhxiaogg.jq.plan.exec;

import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;

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
    public ResolvedAttribute getAttribute(int ordinal) {
        return attributes[ordinal];
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
    public int byId(String id) {
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Matches input against target, checking if input prefix can fully match the target.
     *
     * @param input
     * @param target
     * @return
     */
    private boolean prefixMatches(String[] input, String[] target) {
        boolean matched = true;
        if (input.length > target.length) {
            for (int i = 0; i < target.length; i++) {
                if (!input[i].equalsIgnoreCase(target[i])) {
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
    public int byName(String[] names) {
        int offsetAfterRelationNames = 0;
        if (prefixMatches(names, this.relationNames)) {
            offsetAfterRelationNames = this.relationNames.length;
        }

        for (int i = 0; i < attributes.length; i++) {
            if (fullyMatches(names, attributes[i].getNames(), offsetAfterRelationNames)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int numAttributes() {
        return attributes.length;
    }
}
