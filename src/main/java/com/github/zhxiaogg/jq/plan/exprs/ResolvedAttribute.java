package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.utils.AttributeSearchUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Data
@RequiredArgsConstructor
public class ResolvedAttribute implements LeafExprNode {
    private final String id;
    private final String[] names;
    private final DataType dataType;
    private final int[] ordinals;
    private final AttributeSet inner;

    public ResolvedAttribute(String name, DataType dataType, int ordinal) {
        this(UUID.randomUUID().toString(), new String[]{name}, dataType, new int[]{ordinal}, AttributeSet.empty(new String[0]));
    }

    public ResolvedAttribute(String name, DataType dataType, AttributeSet children, int ordinal) {
        this(UUID.randomUUID().toString(), new String[]{name}, dataType, new int[]{ordinal}, children);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof ResolvedAttribute &&
                (Objects.equals(id, other.getId()) || Arrays.equals(names, ((ResolvedAttribute) other).getNames()));
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public Object evaluate(Record record) {
        Object result = record.indexOf(ordinals[0]);

        for (int i = 1; i < ordinals.length && result != null; i++) {
            result = ((Record) result).indexOf(ordinals[i]);
        }
        return result;
    }

    public ResolvedAttribute withOrdinal(int value) {
        if (this.ordinals.length > 0 && value == this.ordinals[0]) return this;
        return new ResolvedAttribute(id, names, dataType, new int[]{value}, inner);
    }

    public ResolvedAttribute withNames(String[] names) {
        return new ResolvedAttribute(id, names, dataType, ordinals, inner);
    }

    public ResolvedAttribute withOrdinals(int[] value) {
        if (this.ordinals.length > 0 && value[0] == this.ordinals[0]) return this;
        return new ResolvedAttribute(id, names, dataType, value, inner);
    }

    public int[] byId(String id) {
        List<Integer> ordinals = new ArrayList<>();
        for (int ordinal : this.ordinals) {
            ordinals.add(ordinal);
        }
        if (this.id.equalsIgnoreCase(id)) {
            return ordinals.stream().mapToInt(v -> v).toArray();
        } else if (!this.id.equalsIgnoreCase(id)) {
            int[] idsFromChildren = inner.byId(id);
            if (idsFromChildren.length > 0) {
                for (int i : idsFromChildren) {
                    ordinals.add(i);
                }
                return ordinals.stream().mapToInt(v -> v).toArray();
            } else {
                return new int[0];
            }
        } else {
            return new int[0];
        }

    }

    public int[] byName(String[] names, int offset) {
        List<Integer> ordinals = new ArrayList<>();
        if (AttributeSearchUtil.prefixMatches(names, this.names, offset)) {
            for (int ordinal : this.ordinals) {
                ordinals.add(ordinal);
            }
            if (names.length - offset == this.names.length) {
                return ordinals.stream().mapToInt(v -> v).toArray();
            } else if (names.length - offset > this.names.length) {
                int[] ids = this.inner.byName(names, offset + this.names.length);
                if (ids.length == 0) {
                    return ids;
                } else {
                    for (int i : ids) {
                        ordinals.add(i);
                    }
                }
            }
        }
        return ordinals.stream().mapToInt(v -> v).toArray();
    }

    /**
     * Expand names and create nested attributes if possible.
     *
     * @return
     */
    public ResolvedAttribute expand() {
        if (names.length > 1) {
            AttributeSet children = AttributeSet.empty(new String[0]);
            for (int i = names.length - 1; i > 0; i--) {
                DataType dt = i == names.length - 1 ? DataType.Any : DataType.Struct;
                ResolvedAttribute resolvedAttribute = new ResolvedAttribute(names[i], dt, children, 0);
                children = AttributeSet.create(Collections.singletonList(resolvedAttribute));
            }
            return new ResolvedAttribute(id, new String[]{names[0]}, DataType.Struct, new int[0], children);
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return String.join(".", names);
    }

    public ResolvedAttribute mergeWith(ResolvedAttribute target) {
        DataType dataType = this.dataType;
        if (dataType.equals(DataType.UnKnown)) {
            dataType = target.getDataType();
        }
        AttributeSet children;
        if (this.inner.equals(AttributeSet.empty())) {
            children = target.inner;
        } else if (target.inner.equals(AttributeSet.empty())) {
            children = this.inner;
        } else {
            children = this.inner.mergeWith(target.inner);
        }
        return new ResolvedAttribute(id, names, dataType, ordinals, children);
    }
}
