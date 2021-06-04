package com.github.zhxiaogg.jq.nodes.logical;

import com.github.zhxiaogg.jq.Catalog;
import com.github.zhxiaogg.jq.nodes.Node;
import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.nodes.logical.interpreter.RecordBag;
import com.github.zhxiaogg.jq.schema.Attribute;

import java.util.List;

public interface LogicalPlan extends Node<LogicalPlan> {
    RecordBag partialEval(Catalog dataSource);

    LogicalPlan withExpressions(List<Expression> expressions);

    List<Expression> getExpressions();

    List<Attribute> getAttributes(Catalog dataSource);
}
