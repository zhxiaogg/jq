package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.Record;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@Data
public class Cast implements NonLeafExprNode {
    private final Expression child;
    private final DataType dataType;
    private final String id;

    public Cast(Expression child, DataType dataType, String id) {
        require(child.getDataType().canCastTo(dataType), "expression cannot be cast to specific data type!");
        this.child = child;
        this.dataType = dataType;
        this.id = id;
    }

    public Cast(Expression child, DataType dataType) {
        this(child, dataType, UUID.randomUUID().toString());
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.singletonList(child);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Cast(children.get(0), dataType, id);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof Cast &&
                Objects.equals(dataType, other.getDataType()) &&
                child.semanticEqual(((Cast) other).getChild());
    }

    @Override
    public Object evaluate(Record record) {
        Object value = child.evaluate(record);
        return child.getDataType().castTo(getDataType(), value);
    }

    @Override
    public String toString() {
        return String.format("(%s as %s)", child, dataType);
    }
}
