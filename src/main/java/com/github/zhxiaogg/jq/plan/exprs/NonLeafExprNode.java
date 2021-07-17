package com.github.zhxiaogg.jq.plan.exprs;

public interface NonLeafExprNode extends Expression {

    @Override
    default boolean leafNode() {
        return false;
    }

    @Override
    default boolean isResolved() {
        return getChildren().stream().allMatch(Expression::isResolved);
    }
}
