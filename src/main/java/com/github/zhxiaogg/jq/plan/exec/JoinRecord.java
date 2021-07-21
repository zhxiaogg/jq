package com.github.zhxiaogg.jq.plan.exec;

import lombok.Data;

@Data
public class JoinRecord implements Record {
    private final Record left;
    private final Record right;

    @Override
    public Object indexOf(int ordinal) {
        if (ordinal >= left.size()) {
            return right.indexOf(ordinal - left.size());
        } else {
            return left.indexOf(ordinal);
        }
    }

    @Override
    public int size() {
        return left.size() + right.size();
    }

    @Override
    public MutableRecord toMutable() {
        throw new IllegalStateException("not supported by now!");
    }
}
