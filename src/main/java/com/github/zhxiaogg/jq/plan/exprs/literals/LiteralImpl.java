package com.github.zhxiaogg.jq.plan.exprs.literals;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class LiteralImpl implements Literal {
    private final Object value;
    private final DataType dataType;
    private final String id = UUID.randomUUID().toString();

    @Override
    public String toString() {
        return String.format("Literal(%s)", value);
    }

    @Override
    public boolean semanticEqual(Expression other) {
        return other instanceof LiteralImpl &&
                Objects.equals(value, ((LiteralImpl) other).value) &&
                Objects.equals(dataType, other.getDataType());
    }
}
