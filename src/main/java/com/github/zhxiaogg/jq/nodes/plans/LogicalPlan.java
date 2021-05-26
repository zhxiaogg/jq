package com.github.zhxiaogg.jq.nodes.plans;

import com.github.zhxiaogg.jq.DataSource;
import com.github.zhxiaogg.jq.nodes.Node;
import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.plans.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;

import java.util.List;

public interface LogicalPlan extends Node<LogicalPlan> {
    RecordBag partialEval(DataSource dataSource);

    LogicalPlan withExpressions(List<Expression> expressions);

    List<Expression> getExpressions();

    List<Attribute> getAttributes(DataSource dataSource);
}
