package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.utils.ListUtils;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@Data
public class Min implements NonLeafExprNode {
    private final List<Expression> children;
    private final String id;

    public Min(String id, List<Expression> children) {
        require(children.size() > 0, "empty children!");
        this.children = children;
        this.id = id;
    }

    public Min(List<Expression> children) {
        this(UUID.randomUUID().toString(), children);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Min(id, children);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof Min &&
                children.size() == ((Min) other).children.size() &&
                ListUtils.zip(children, ((Min) other).children).stream().allMatch(p -> p.getLeft().semanticEqual(p.getRight()));
    }

    @Override
    public Object evaluate(Record record) {
        List<Object> values = children.stream().map(e -> e.evaluate(record)).collect(Collectors.toList());
        switch (this.getDataType()) {
            case Int:
                return values.stream().mapToInt(v1 -> (int) v1).min().getAsInt();
            case Float:
                return values.stream().mapToDouble(v -> (double) v).min().getAsDouble();
            default:
                throw new IllegalStateException("unsupported data type: " + this.getDataType());
        }
    }

    @Override
    public DataType getDataType() {
        return children.get(0).getDataType();
    }
}

