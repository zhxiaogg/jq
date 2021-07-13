package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exprs.aggregators.Avg;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.Max;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.Min;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.Sum;
import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.List;

@Data
public class FunctionCall implements Expression {
    private final String functionName;
    private final List<Expression> arguments;

    public static Expression create(String functionName, List<Expression> arguments) {
        switch (functionName.toUpperCase()) {
            case "SUM":
                return new Sum(arguments.get(0));
            case "MAX":
                return new Max(arguments.get(0));
            case "MIN":
                return new Min(arguments.get(0));
            case "AVG":
                return new Avg(arguments.get(0));
            default:
                return new FunctionCall(functionName, arguments);
        }
    }

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
    public String toString() {
        return null;
    }

    @Override
    public DataType getDataType() {
        return null;
    }
}
