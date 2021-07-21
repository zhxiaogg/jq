package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.AvgAgg;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.MaxAgg;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.MinAgg;
import com.github.zhxiaogg.jq.plan.exprs.aggregators.SumAgg;
import com.github.zhxiaogg.jq.schema.DataType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.github.zhxiaogg.jq.utils.Requires.require;

@Data
@RequiredArgsConstructor
public class FunctionCall implements NonLeafExprNode {
    private final String functionName;
    private final List<Expression> arguments;
    private final String id;

    public FunctionCall(String functionName, List<Expression> arguments) {
        this(functionName, arguments, UUID.randomUUID().toString());
    }

    public static Expression create(String functionName, List<Expression> arguments) {
        switch (functionName.toUpperCase()) {
            case "SUM":
                return new SumAgg(arguments.get(0));
            case "MAX":
                require(arguments.size() > 0, "MAX functions requires at least one argument!");
                if (arguments.size() > 1) {
                    return new Max(arguments);
                } else {
                    return new MaxAgg(arguments.get(0));
                }
            case "MIN":
                require(arguments.size() > 0, "MIN functions requires at least one argument!");
                if (arguments.size() > 1) {
                    return new Min(arguments);
                } else {
                    return new MinAgg(arguments.get(0));
                }
            case "AVG":
                return new AvgAgg(arguments.get(0));
            default:
                return new FunctionCall(functionName, arguments);
        }
    }

    @Override
    public List<Expression> getChildren() {
        return arguments;
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new FunctionCall(functionName, children, id);
    }

    @Override
    public Object evaluate(Record record) {
        return null;
    }

    @Override
    public DataType getDataType() {
        return null;
    }
}
