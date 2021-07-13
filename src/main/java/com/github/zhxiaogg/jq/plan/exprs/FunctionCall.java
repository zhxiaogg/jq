package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.List;

@Data
public class FunctionCall implements Expression{
    private final String functionName;
    private final List<Expression> arguments;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return arguments;
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new FunctionCall(functionName,children);
    }

    @Override
    public Value eval(Record record) {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public DataType getDataType() {
        return null;
    }
}
