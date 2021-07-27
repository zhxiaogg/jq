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
public class Max implements NonLeafExprNode {
    private final String id;
    private final List<Expression> children;

    public Max(String id, List<Expression> children) {
        require(children.size() > 0, "empty children!");
        this.children = children;
        this.id = id;
    }

    public Max(List<Expression> children) {
        this(UUID.randomUUID().toString(), children);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Max(id, children);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof Max &&
                children.size() == ((Max) other).children.size() &&
                ListUtils.zip(children, ((Max) other).children).stream().allMatch(p -> p.getLeft().semanticEqual(p.getRight()));
    }

    @Override
    public Object evaluate(Record record) {
        List<Object> values = children.stream().map(e -> e.evaluate(record)).collect(Collectors.toList());
        switch (this.getDataType()) {
            case Int:
                return values.stream().mapToInt(v1 -> (int) v1).max().getAsInt();
            case Float:
            case Any:
                return values.stream().mapToDouble(v -> ((Number) v).doubleValue()).max().getAsDouble();
            default:
                throw new IllegalStateException("unsupported data type: " + this.getDataType());
        }
    }

    @Override
    public DataType getDataType() {
        return children.get(0).getDataType();
    }
}
