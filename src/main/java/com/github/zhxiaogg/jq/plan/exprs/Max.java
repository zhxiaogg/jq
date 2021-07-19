package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
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
    public Value eval(Record record) {
        List<Value> values = children.stream().map(e -> e.eval(record)).collect(Collectors.toList());
        switch (this.getDataType()) {
            case Int:
                int i = values.stream().mapToInt(v -> (int) v.getValue()).max().getAsInt();
                return new LiteralValue(i, DataType.Int);
            case Float:
                double d = values.stream().mapToDouble(v -> (double) v.getValue()).max().getAsDouble();
                return new LiteralValue(d, DataType.Float);
            default:
                throw new IllegalStateException("unsupported data type: " + this.getDataType());
        }
    }

    @Override
    public Object evaluate(Record record) {
        return eval(record).getValue();
    }

    @Override
    public DataType getDataType() {
        return children.get(0).getDataType();
    }
}
